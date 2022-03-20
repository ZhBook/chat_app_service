package com.example.cloud.data.request.blog;

import com.example.cloud.system.BlogUserRequest;
import lombok.Data;

/**
 * @author: zhooke
 * @create: 2022-03-20 17:31
 * @description:
 **/
@Data
public class BlogCommentRequest extends BlogUserRequest {
    /**
     *
     */
    private Long blogId;

    /**
     *
     */
    private String comment;
}
