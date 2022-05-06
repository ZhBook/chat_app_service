package com.example.cloud.data.request.blog;

import com.example.cloud.system.PagingBlogRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhooke
 * @since 2022/5/5 10:39
 **/
@Data
public class BlogCommentNewestRequest extends PagingBlogRequest {
    /**
     *
     */
    @NotBlank(message = "博主id不能为空")
    private Long blogAuthorId;
}
