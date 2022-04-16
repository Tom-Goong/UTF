package com.yaoting.utf.infrastructure.exception;


/**
 * 权限不足以进行当前的业务操作。比如没有登录，或普通账号进行管理员账号的操作
 *
 * 这种情况下，应该引导去进行登录或申请权限
 */
public class AuthException extends BaseException{
    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
