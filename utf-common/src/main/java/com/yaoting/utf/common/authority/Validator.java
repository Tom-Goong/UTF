package com.yaoting.utf.common.authority;

public interface Validator {
    default boolean validate(Class<?>[] clazz) {
        return true;
    }
}
