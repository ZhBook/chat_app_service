package com.tensua.blogservice.enums;

import lombok.Getter;

/**
 * @author:70968 Date:2021-10-16 11:02
 */
public enum StateEnum {
    ENABLED(1,"正常"),
    DISABLED(0,"禁用");

    @Getter
    private int code;
    @Getter
    private String msg;

    StateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
