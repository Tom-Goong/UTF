package com.yaoting.utf.domain.func;

import com.yaoting.utf.common.base.BaseEnumInterface;
import lombok.Getter;

@Getter
public enum Namespace implements BaseEnumInterface {

    Default(0, "default"),
    ;

    private Integer id;
    private String desc;

    Namespace(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
