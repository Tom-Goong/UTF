package com.yaoting.utf.common.utils;


import com.yaoting.utf.common.base.BaseEnumInterface;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Enums {
    public static <T extends BaseEnumInterface> T of(T[] enums, Integer id) {
        return Arrays.stream(enums)
                .filter(type -> type.is(id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("non-existence id: " + id));
    }

    public static <T extends BaseEnumInterface> T of(T[] enums, String desc) {
        return Arrays.stream(enums)
                .filter(type -> type.is(desc))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("non-existence desc: " + desc));
    }

    public static Map<String, String> toDesc(BaseEnumInterface[] descInterfaces) {
        return Arrays.stream(descInterfaces)
                .collect(Collectors.toMap(BaseEnumInterface::name, BaseEnumInterface::getDesc));
    }
}
