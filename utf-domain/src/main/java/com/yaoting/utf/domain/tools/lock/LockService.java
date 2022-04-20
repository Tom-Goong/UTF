package com.yaoting.utf.domain.tools.lock;

import java.util.List;
import java.util.Optional;

public interface LockService {

    Optional<LockInfoVO> tryLock(LockInfoVO lockInfoDTO);

    boolean releaseLock(LockInfoVO lockInfoDTO);

    List<LockInfoVO> listAll();

    void forceRelease(String service, String lockKey);
}
