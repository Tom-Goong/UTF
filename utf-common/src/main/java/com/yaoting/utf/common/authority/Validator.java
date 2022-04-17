package com.yaoting.utf.common.authority;

/**
 * 暂时不启用指定校验器，先简单实现
 */
public interface Validator {
    default boolean validate(Class<?>[] clazz) {
        return true;
    }
}
