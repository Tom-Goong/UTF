package com.yaoting.utf.infrastructure.authority;

public enum Role {
    Normal(0, "普通账号"),
    Manager(1, "管理员"),
    Super_Manager(2, "超级管理员"),
    ;

    private final int id;
    private final String name;

    Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int id() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static Role ofId(int id) {
        for (Role role : values()) {
            if (role.id == id) {
                return role;
            }
        }

        throw new IllegalArgumentException("不存在的 Role, Id = " + id);
    }

    public boolean hasManageRight() {
        return this == Role.Manager || this == Role.Super_Manager;
    }
}
