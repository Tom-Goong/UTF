package com.yaoting.utf.domain.common.entity;

import java.io.Serializable;

/**
 * uid of entity
 * @param <T>
 */
public interface Identity<T> extends Serializable {
    T id();
}
