package com.tensua.data;

import com.tensua.enums.MessageTypeEnum;
import com.tensua.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketResult<T> implements Serializable {

    private T data;
    private Integer code;
    private Integer type;

    public static <T> SocketResult<T> succeed(Integer type) {
        return of(null, ResultCodeEnum.SUCCESS.getCode(), type);
    }

    public static <T> SocketResult<T> succeed(T model, Integer type) {
        return of(model, ResultCodeEnum.SUCCESS.getCode(), type);
    }

    public static <T> SocketResult<T> succeed(T model) {
        return of(model, ResultCodeEnum.SUCCESS.getCode(), MessageTypeEnum.TEXT.getCode());
    }

    public static <T> SocketResult<T> of(T data, Integer code, Integer type) {
        return new SocketResult<>(data, code, type);
    }

    public static <T> SocketResult<T> failed(Integer type) {
        return of(null, ResultCodeEnum.FAILED.getCode(), type);
    }

    public static <T> SocketResult<T> failed(T model, Integer type) {
        return of(model, ResultCodeEnum.FAILED.getCode(), type);
    }

    public static boolean isSuccess(SocketResult<?> socketResult) {
        return socketResult != null && ResultCodeEnum.SUCCESS.getCode().equals(socketResult.getCode());
    }
}
