package com.example.cloud.data.request.blog;

import com.example.cloud.system.BlogUserRequest;
import lombok.Data;

/**
 * @author zhooke
 * @since 2022/3/21 14:04
 **/
@Data
public class BlogTagRequest extends BlogUserRequest {

    /**
     *
     */
    private String name;

}
