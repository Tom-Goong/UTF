package com.yaoting.utf.domain.tools.repo;


import com.yaoting.utf.domain.tools.lock.LockInfoVO;

import java.util.List;

public interface LockInfoRepo {
    LockInfoVO selectByServiceAndLockKey(String service, String lockKey);


    LockInfoVO getLock(LockInfoVO lockInfoVO);

    boolean deleteById(Long id);

    List<LockInfoVO> listAll();
}
