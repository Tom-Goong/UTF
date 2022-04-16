package com.yaoting.utf.tools.job;


import com.yaoting.utf.infrastructure.context.AppContext;
import com.yaoting.utf.infrastructure.utils.Preconditions;
import com.yaoting.utf.tools.lock.LockInfoVO;
import com.yaoting.utf.tools.lock.LockService;
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
        Preconditions.notBlank(lockKey, "LockKey 不能为空");
        Preconditions.checkState(expiredIn > 0, "expiredIn 需要大于零");

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
        return new LockInfoVO()
                .setService(appContext.getName())
                .setLockKey(lockKey)
                .setInstance(appContext.getHostname())
                .setExpireTime(expiredDate);
    }
}
