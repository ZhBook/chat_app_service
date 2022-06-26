package com.tensua.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouhd
 * @since 2021/10/25 13:52
 **/
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum HaveReadStateEnum {

    REDA(1,"已读"),
    UNREAD(0,"未读");

    int code;
    String msg;

}
