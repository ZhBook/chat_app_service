package com.example.cloud.enums;

import com.example.cloud.exception.IErrorCode;

/**
 * 枚举了一些常用API操作码
 * Created by macro on 2019/4/19.
 */
public enum ResultCodeEnum implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    CAPTCHA_INVALID(400, "验证码初始化失败"),


    NOT_LOGIN(40001, "未登陆"),
    NOT_PERMISSION(40003, "未授权"),
    TOKEN_EXPIRED(40005, "Token失效或过期"),
    LOGIN_FAILURE(40006, "登陆失败"),
    USER_EXPIRED(40007,"账户过期"),
    USER_BAD_CREDENTIALS(40008,"密码不正确"),
    USER_USERNAME_NOT_FOUND(40009,"用户不存在"),
    USER_LOCKED(40010,"用户锁定"),
    USER_NOT_ENABLE(40011,"用户未启用"),
    LOGIN_FAILURE_INTERNAL_ERROR(40012,"登陆失败，内部异常"),
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
