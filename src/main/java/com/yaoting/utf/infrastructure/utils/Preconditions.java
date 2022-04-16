package com.yaoting.utf.infrastructure.utils;

import com.yaoting.utf.infrastructure.exception.BadRequestException;
import com.yaoting.utf.infrastructure.exception.BizForbidException;

public class Preconditions {
    public static void checkBizStatus(boolean expression, String errorMsg) {
        if (!expression) {
            throw new BizForbidException(errorMsg);
        }
    }

    /**
     * 因参数错误导致的异常状态，
     */
    public static void checkRequestStatus(boolean expression, String errorMsg) {
        if (!expression) {
            throw new BadRequestException(errorMsg);
        }
    }

    public static void notNull(Object obj, String errorMsg) {
        if (obj == null) {
            throw new NullPointerException(errorMsg);
        }
    }

    public static void notBlank(String str, String errorMsg) {
        if (ValidateUtils.isBlank(str)) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void checkState(boolean expression, String errorMsg) {
        if (!expression) {
            throw new IllegalStateException(errorMsg);
        }
    }
}
