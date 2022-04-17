package com.yaoting.utf.common.exception;


/**
 * 终端请求参数错误，造成的异常流程
 *
 * 这种情况下如果调整参数，可以重试，否则仍然失败。
 */
public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
