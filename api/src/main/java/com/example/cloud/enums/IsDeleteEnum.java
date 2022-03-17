package com.example.cloud.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhooke
 * @since 2022/3/17 18:01
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum IsDeleteEnum {

    YES(1,"删除"),
    NO(0,"正常");

    @Getter
    private int code;
    @Getter
    private String msg;


}
