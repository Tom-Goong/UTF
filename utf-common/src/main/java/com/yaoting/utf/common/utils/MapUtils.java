package com.yaoting.utf.common.utils;


import com.yaoting.utf.common.base.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class MapUtils {

    public static <K, V> Map<K, V> of(Tuple2<K, V>... tuples) {
        Preconditions.notNull(tuples, "args can't been null");

        Map<K, V> result = new HashMap<>();

        Arrays.stream(tuples).forEach(ele -> result.put(ele.getT1(), ele.getT2()));

        return result;
    }
}
