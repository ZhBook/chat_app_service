package com.example.cloud.data.request.blog;

import com.example.cloud.system.BlogUserRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

/**
 * @author zhooke
 * @since 2022/5/12 15:27
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogReplyCommentRequest extends BlogUserRequest {

    /**
     *
     */
    @NotNull(message = "博客id不能为空")
    Long blogId;

    /**
     *
     */
    @NotNull(message = "评论不能为空")
    Long commentId;

    /**
     *
     */
    @NotNull(message = "回复内容不能为空")
    String comment;

    /**
     * blog所属作者id
     */
    @NotNull(message = "博主id不能为空")
    Long blogAuthorId;

    /**
     * ip地址
     */
    String ipAddress;

    /**
     * 浏览器标识
     */
    String browserModel;
}
