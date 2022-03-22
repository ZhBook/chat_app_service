package com.example.cloud.data.request.blog;

import com.example.cloud.system.BlogUserRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhooke
 * @since 2022/3/21 14:37
 **/
@Data
public class BlogTagCreateRequest extends BlogUserRequest {
    /**
     *
     */
    @NotNull(message = "标签id不能为空")
    private Long tagId;

    /**
     *
     */
    @NotBlank(message = "标签name不能为空")
    private String tagName;

    /**
     *
     */
    @NotNull(message = "博客id不能为空")
    private Long blogId;
}
