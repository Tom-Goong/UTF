package com.yaoting.utf.domain.tools.lock;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class LockInfoVO {
    private Long id;
    private String service;
    private String instance;
    private String lockKey;
    private Date expireTime;
    private Date createTime;
    private Date updateTime;

    public boolean hasExpired() {
        return System.currentTimeMillis() >= expireTime.getTime();
    }
}
