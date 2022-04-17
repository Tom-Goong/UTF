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
        Preconditions.checkState(notNull(lockInfoVO), "lockInfo 为空");
        Preconditions.checkState(!lockInfoVO.hasExpired(), "给定 lockInfo 锁已过期，不需要锁");

        LockInfoVO lockInfoFromDB = lockInfoRepo.selectByServiceAndLockKey(lockInfoVO.getService(), lockInfoVO.getLockKey());

        if (isNull(lockInfoFromDB)) {
            return Optional.ofNullable(lockInfoRepo.getLock(lockInfoVO));
        } else if (hasExpired(lockInfoFromDB)) {
            if (lockInfoRepo.deleteById(lockInfoFromDB.getId())) {
                // 删除过期的后，重新插入
                return tryLock(lockInfoVO);
            }
        } else if (sameInstance(lockInfoFromDB, lockInfoVO)) {
            // 重用锁 @TODO 需要考虑所时间不同问题
            return Optional.of(lockInfoFromDB);
        }

        return Optional.empty();
    }

    @Override
    public boolean releaseLock(LockInfoVO lockInfoVO) {
        LockInfoVO lockInfoFromDB = lockInfoRepo.selectByServiceAndLockKey(lockInfoVO.getService(), lockInfoVO.getLockKey());

        if (isNull(lockInfoFromDB)) {
            return true;
        } else if (hasExpired(lockInfoFromDB)) {
            lockInfoRepo.deleteById(lockInfoFromDB.getId());
            return true;
        }

        Preconditions.checkBizStatus(sameInstance(lockInfoFromDB, lockInfoVO), "有效锁只能由持有者释放");
        return lockInfoRepo.deleteById(lockInfoFromDB.getId());
    }

    @Override
    public List<LockInfoVO> listAll() {
        return lockInfoRepo.listAll();
    }

    @Override
    public void forceRelease(String service, String lockKey) {
        LockInfoVO lockInfoVO = lockInfoRepo.selectByServiceAndLockKey(service, lockKey);
        if (notNull(lockInfoVO)) {
            lockInfoRepo.deleteById(lockInfoVO.getId());
        }
    }

    private boolean hasExpired(LockInfoVO lockInfo) {
        return System.currentTimeMillis() >= lockInfo.getExpireTime().getTime();
    }

    private boolean sameInstance(LockInfoVO lockInfo_1, LockInfoVO lockInfo_2) {
        return lockInfo_1.getInstance().equals(lockInfo_2.getInstance());
    }
}
