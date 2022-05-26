package com.example.cloud.data.request.blog;

import com.example.cloud.system.BlogUserRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: zhooke
 * @create: 2022-03-20 17:31
 * @description:
 **/
@Data
public class BlogCommentRequest extends BlogUserRequest {

    private static final long serialVersionUID = -5032324493730868810L;
    /**
     *
     */
    @NotNull(message = "博客id不能为空")
    private Long blogId;

    /**
     *
     */
    @NotBlank(message = "评论内容不能为空")
    private String comment;

    /**
     *
     */
    @NotNull(message = "博主id不能为空")
    private Long blogAuthorId;

    /**
     * 浏览器标识
     */
    private String browserModel;

    /**
     * 评论类型
     */
    @NotNull(message = "评论类型不能为空")
    private Integer commentType;
}
