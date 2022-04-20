package com.yaoting.utf.domain.tools.lock.impl;

import com.yaoting.utf.common.utils.Preconditions;
import com.yaoting.utf.domain.tools.lock.LockInfoVO;
import com.yaoting.utf.domain.tools.lock.LockService;
import com.yaoting.utf.domain.tools.repo.LockInfoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.yaoting.utf.common.utils.ValidateUtils.isNull;
import static com.yaoting.utf.common.utils.ValidateUtils.notNull;


@RequiredArgsConstructor
@Service
@Slf4j
public class MySqlLockServiceImpl implements LockService {

    private final LockInfoRepo lockInfoRepo;

    @Override
    public Optional<LockInfoVO> tryLock(LockInfoVO lockInfoVO) {
        Preconditions.checkState(notNull(lockInfoVO), "LockInfo can't been blank");
        Preconditions.checkState(!lockInfoVO.hasExpired(), "LockInfo has been expired，can't been locked");

        LockInfoVO lockInfoFromDB = lockInfoRepo.selectByServiceAndLockKey(lockInfoVO.namespace(), lockInfoVO.key());

        if (isNull(lockInfoFromDB)) {
            return Optional.ofNullable(lockInfoRepo.getLock(lockInfoVO));
        } else if (hasExpired(lockInfoFromDB)) {
            if (lockInfoRepo.deleteById(lockInfoFromDB.namespace(), lockInfoFromDB.key())) {
                // retry
                return tryLock(lockInfoVO);
            }
        } else if (sameInstance(lockInfoFromDB, lockInfoVO)) {
            // refuse @TODO consider to handle the unequal time
            return Optional.of(lockInfoFromDB);
        }

        return Optional.empty();
    }

    @Override
    public boolean releaseLock(LockInfoVO lockInfoVO) {
        LockInfoVO lockInfoFromDB = lockInfoRepo.selectByServiceAndLockKey(lockInfoVO.namespace(), lockInfoVO.key());

        if (isNull(lockInfoFromDB)) {
            return true;
        } else if (hasExpired(lockInfoFromDB)) {
            lockInfoRepo.deleteById(lockInfoFromDB.namespace(), lockInfoFromDB.key());
            return true;
        }

        Preconditions.checkBizStatus(sameInstance(lockInfoFromDB, lockInfoVO), "Only the holder can release the lock which is still locking");
        return lockInfoRepo.deleteById(lockInfoFromDB.namespace(), lockInfoFromDB.key());
    }

    @Override
    public List<LockInfoVO> listAll() {
        return lockInfoRepo.listAll();
    }

    @Override
    public void forceRelease(String service, String lockKey) {
        LockInfoVO lockInfoVO = lockInfoRepo.selectByServiceAndLockKey(service, lockKey);
        if (notNull(lockInfoVO)) {
            lockInfoRepo.deleteById(lockInfoVO.namespace(), lockInfoVO.key());
        }
    }

    private boolean hasExpired(LockInfoVO lockInfo) {
        return System.currentTimeMillis() >= lockInfo.expireTime().getTime();
    }

    private boolean sameInstance(LockInfoVO lockInfo_1, LockInfoVO lockInfo_2) {
        return lockInfo_1.instance().equals(lockInfo_2.instance());
    }
}
