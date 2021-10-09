package com.example.cloud.enums;

/**
 * @author zhouhd
 * @since 2021/10/9 10:15
 **/
public enum CodeEnum {
    SUCCESS(200),
    ERROR(500);

    private Integer code;
    CodeEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
