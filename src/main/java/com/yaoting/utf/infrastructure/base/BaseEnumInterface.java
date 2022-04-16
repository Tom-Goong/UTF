package com.yaoting.utf.infrastructure.base;

public interface BaseEnumInterface {
    String name();

    default boolean is(Integer id) {
        return getId().equals(id);
    }

    default boolean is(String desc) {
        return getDesc().equals(desc);
    }

    String getDesc();

    Integer getId();
}
