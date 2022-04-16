package com.yaoting.utf.tools.repo;


import com.yaoting.utf.tools.lock.LockInfoVO;

import java.util.List;

public interface LockInfoRepo {
    LockInfoVO selectByServiceAndLockKey(String service, String lockKey);


    LockInfoVO getLock(LockInfoVO lockInfoVO);

    boolean deleteById(Long id);

    List<LockInfoVO> listAll();
}
