package com.yaoting.utf.domain.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public abstract class BaseEntity {
    private Long id;
    private Date createTime;
    private Date updateTime;

    protected void persist() {

    }

    protected void delete() {

    }

    public boolean haveSaved() {
        return id != null;
    }
}
