package com.tensua.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhooke
 * @since 2022/5/26 10:02
 **/
public class BlogEnums {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum CommentType {

        BLOG_COMMENT(1, "博客评论"),
        SYSTEM_COMMENT(2, "系统评论");

        Integer type;
        String desc;

    }
}
