package com.yaoting.utf.domain.tools.lock;

import java.util.Date;

public record LockInfoVO(String namespace,
                         String instance,
                         String key,
                         Date expireTime) {


    public boolean hasExpired() {
        return System.currentTimeMillis() >= expireTime.getTime();
    }
}
