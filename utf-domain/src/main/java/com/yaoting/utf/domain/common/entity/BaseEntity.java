package com.yaoting.utf.domain.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class BaseEntity {

    private Long id;

    public void persist() {

    }

    public void delete() {

    }
}
