package com.yaoting.utf.common.exception;

public class ServerInnerException extends BaseException {
    public ServerInnerException(String message) {
        super(message);
    }

    public ServerInnerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
