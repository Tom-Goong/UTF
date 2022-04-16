package com.yaoting.utf.infrastructure.exception;


/**
 * 因业务原因，造成的异常流程，非参数等原因
 *
 * 这种情况，调整参数也无法正常处理，终端不应该重试
 */
public class BizForbidException extends BaseException {
    public BizForbidException(String message) {
        super(message);
    }

    public BizForbidException(String message, Throwable throwable) {
        super(message, throwable);
    }
}