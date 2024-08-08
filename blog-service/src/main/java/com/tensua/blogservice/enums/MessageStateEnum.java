package com.tensua.blogservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhooke
 * @since 2022/1/19 10:29
 **/
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MessageStateEnum {
    SUCCESS(0,"发送成功"),
    FAIL(1,"发送失败"),
    ;
    Integer state;
    String Info;
}
