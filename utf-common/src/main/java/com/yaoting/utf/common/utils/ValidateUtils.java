package com.yaoting.utf.common.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ValidateUtils {

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean anyNull(Object... objects) {
        return Arrays.stream(objects).anyMatch(ValidateUtils::isNull);
    }

    public static boolean noneNull(Object... objects) {
        return !anyNull(objects);
    }

    public static boolean notNull(Object object) {
        return !isNull(object);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean anyBlank(String... params) {
        return Arrays.stream(params).anyMatch(ValidateUtils::isBlank);
    }

    public static boolean noneBlank(String... params) {
        return !anyBlank(params);
    }

    public static boolean isExistBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalList(List<Object> seq1, List<Object> seq2) {
        if (isNull(seq1) && isNull(seq2)) {
            return true;
        } else if (isNull(seq1) || isNull(seq2) || seq1.size() != seq2.size()) {
            return false;
        }
        for (Object elem: seq1) {
            if (!seq2.contains(elem)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }



    public static boolean isNullOrNegative(Long value) {
        return value == null || value < 0;
    }

    public static boolean isNullOrNegative(Integer value) {
        return value == null || value < 0;
    }

    public static boolean isNullOrNegative(Double value) {
        return value == null || value < 0;
    }
}