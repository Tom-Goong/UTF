package com.yaoting.utf.domain.tools.job;


import com.yaoting.utf.common.context.AppContext;
import com.yaoting.utf.common.utils.Preconditions;
import com.yaoting.utf.domain.tools.lock.LockInfoVO;
import com.yaoting.utf.domain.tools.lock.LockService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;


@Slf4j
public abstract class DistributedJob {
    @Resource
    protected AppContext appContext;

    @Resource
    protected LockService lockService;

    protected String name() {
        return this.getClass().getName();
    }

    protected Optional<LockInfoVO> tryLock(String lockKey, Long expiredIn) {
        Preconditions.notBlank(lockKey, "LockKey can't been blank");
        Preconditions.checkState(expiredIn > 0, "expiredIn needs to been positive");

        log.info("Try to get lock: {}, expiredIn: {}", lockKey, expiredIn);

        LockInfoVO lockInfo = getLockInfo(lockKey, expiredIn);
        return lockService.tryLock(lockInfo);
    }

    protected void releaseLock(LockInfoVO lockInfoDTO) {
        log.info("Start to release lock: {}", lockInfoDTO);
        lockService.releaseLock(lockInfoDTO);
    }

    private LockInfoVO getLockInfo(String lockKey, Long expiredIn) {
        Date expiredDate = new Date(System.currentTimeMillis() + expiredIn);
        return new LockInfoVO(appContext.getName(),
                lockKey,
                appContext.getHostname(),
                expiredDate);
    }
}
