package com.yaoting.utf.common.exception;


public class ResNotFoundException extends BaseException {
    public ResNotFoundException(String message) {
        super(message);
    }

    public ResNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
