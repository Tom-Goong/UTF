package com.yaoting.utf.common.exception;


/**
 * 服务端内部异常，导致流程处理异常
 *
 * 这种场景下，终端可以稍后重试
 */
public class ServerInnerException extends BaseException {
    public ServerInnerException(String message) {
        super(message);
    }

    public ServerInnerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
