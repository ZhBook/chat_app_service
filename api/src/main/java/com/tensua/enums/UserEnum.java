package com.tensua.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhooke
 * @since 2023/1/3 16:00
 **/
public class UserEnum {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum UserType {

        CHAT("聊天类型"),
        BLOG("博客类型");

        String desc;

    }
}
