package com.tensua.data.request.blog;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

/**
 * @author: zhooke
 * @create: 2023-04-25 19:01
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogChatRequest {

    /**
     * chatGPT Key
     */
    String chatKey;

    /**
     * 消息
     */
    @NotBlank(message = "消息内容不能为空")
    String message;

    /**
     * 模型
     */
    String model = "gpt-3.5-turbo";

}
