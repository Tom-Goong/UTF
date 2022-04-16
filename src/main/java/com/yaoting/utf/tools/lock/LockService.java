package com.yaoting.utf.tools.lock;

import java.util.List;
import java.util.Optional;

public interface LockService {

    Optional<LockInfoVO> tryLock(LockInfoVO lockInfoDTO);

    boolean releaseLock(LockInfoVO lockInfoDTO);

    List<LockInfoVO> listAll();

    /**
     * 强制释放锁，作用管理，不对一般用户开放
     */
    void forceRelease(String service, String lockKey);
}
