package com.example.cloud.data.request.blog;

import lombok.Data;

/**
 * @author zhooke
 * @since 2022/4/12 10:34
 **/
@Data
public class BlogPageRequest {

    /**
     * 默认第一页
     */
    Integer pageIndex = 1;

    /**
     * 默认每页10条
     */
    Integer pageSize = 10;
}
