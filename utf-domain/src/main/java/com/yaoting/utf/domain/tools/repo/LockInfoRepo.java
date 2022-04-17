package com.yaoting.utf.domain.tools.repo;


import com.yaoting.utf.domain.tools.lock.LockInfoVO;

import java.util.List;

public interface LockInfoRepo {
    LockInfoVO selectByServiceAndLockKey(String namespace, String lockKey);


    LockInfoVO getLock(LockInfoVO lockInfoVO);

    boolean deleteById(String namespace, String lockKey);

    List<LockInfoVO> listAll();
}
