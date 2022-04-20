package com.yaoting.utf.common.exception;

public class AuthException extends BaseException{
    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
