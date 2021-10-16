package com.example.cloud.enums;

import lombok.Getter;

/**
 * @author:70968 Date:2021-10-16 09:10
 */

public enum UserStateEnum {
    NORMAL(0,"正常"),
    DELETE(1,"已删除"),

    ENABLED(0,"启用"),
    DISABLED(1,"禁用");

    @Getter
    private int code;
    @Getter
    private String msg;

    UserStateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
