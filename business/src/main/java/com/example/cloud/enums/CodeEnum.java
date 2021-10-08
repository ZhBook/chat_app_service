package com.example.cloud.enums;

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
