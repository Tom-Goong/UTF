package com.yaoting.utf.infrastructure.http;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 仅限在 api 返回时使用，不允许使用在其他地方，尤其方法返回体
 */

@Data
@Accessors(chain = true)
public class APIResult<T>{
    private int code = 0;
    private String message;
    private String detail;
    private T data;

    public static APIResult<Void> voidData() {
        return new APIResult<Void>()
                .setData(null);
    }

    @SuppressWarnings("rawtypes")
    public static <T> APIResult<T> of(T data) {
        return new APIResult()
                .setData(data);
    }


    @SuppressWarnings("rawtypes")
    public static <T> APIResult<T> of(int code, String message) {
        return new APIResult()
                .setCode(code)
                .setMessage(message);
    }

    public boolean isSuccess() {
        return code == 0;
    }
}
