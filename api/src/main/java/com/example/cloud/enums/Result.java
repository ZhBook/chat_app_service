package com.example.cloud.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private T data;
    private Integer code;
    private String msg;

    public static <T> Result<T> succeed(String msg) {
        return of(null, ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return of(model, ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return of(model, ResultCode.SUCCESS.getCode(), "");
    }

    public static <T> Result<T> of(T data, Integer code, String msg) {
        return new Result<>(data, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return of(null, ResultCode.FAILED.getCode(), msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return of(model, ResultCode.FAILED.getCode(), msg);
    }

    public static boolean isSuccess(Result<?> result) {
        return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
    }
}
