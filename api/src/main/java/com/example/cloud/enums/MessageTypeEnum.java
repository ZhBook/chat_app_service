package com.example.cloud.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouhd
 * @since 2021/10/26 14:31
 **/
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MessageTypeEnum {
    PONG(0,"心跳测试"),
    TEXT(1,"聊天消息");

    int code;
    String msg;
}
