package com.tensua.blogservice.data;

import com.tensua.blogservice.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResult<T> implements Serializable {

    private T data;
    private Integer code;
    private String message;

    public static <T> BaseResult<T> succeed(String msg) {
        return of(null, ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> BaseResult<T> succeed(T model, String msg) {
        return of(model, ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> BaseResult<T> succeed(T model) {
        return of(model, ResultCodeEnum.SUCCESS.getCode(), "");
    }
    public static <T> BaseResult<T> succeed() {
        return succeed("success");
    }

    public static <T> BaseResult<T> of(T data, Integer code, String msg) {
        return new BaseResult<>(data, code, msg);
    }

    public static <T> BaseResult<T> failed(String msg) {
        return of(null, ResultCodeEnum.FAILED.getCode(), msg);
    }

    public static BaseResult fail(ResultCodeEnum resultCode) {
        return failed(resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> BaseResult<T> failed(T model, String msg) {
        return of(model, ResultCodeEnum.FAILED.getCode(), msg);
    }

    public static boolean isSuccess(BaseResult<?> baseResult) {
        return baseResult != null && ResultCodeEnum.SUCCESS.getCode().equals(baseResult.getCode());
    }
}
