package com.tensua.exception;

/**
 * @author zhouhd
 * @since 2021/10/9 09:23
 **/
public class BasicException extends RuntimeException {
    private int code = 5000;

    public BasicException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BasicException(String message) {
        super(message);
    }

    public int getCode() {
        return this.code;
    }
}
