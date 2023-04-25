package com.tensua.data.response.blog;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author: zhooke
 * @create: 2023-04-25 19:05
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogChatResponse {

    /**
     * 返回内容
     */
    String context;
}
