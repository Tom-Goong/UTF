package com.yaoting.utf.common.exception;

public class BizForbidException extends BaseException {
    public BizForbidException(String message) {
        super(message);
    }

    public BizForbidException(String message, Throwable throwable) {
        super(message, throwable);
    }
}