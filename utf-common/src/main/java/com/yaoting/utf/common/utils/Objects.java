package com.yaoting.utf.common.utils;

public abstract class Objects {
    public static String blankOr(String value, String defaultV) {
        return ValidateUtils.isNotBlank(value) ? value : defaultV;
    }

    public static <T> T nullOr(T value, T defaultV) {
        return ValidateUtils.notNull(value) ? value : defaultV;
    }
}
