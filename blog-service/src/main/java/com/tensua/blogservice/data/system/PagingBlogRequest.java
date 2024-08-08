package com.tensua.blogservice.data.system;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author zhooke
 * @since 2022/3/21 10:29
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingBlogRequest extends BlogUserRequest{
    /**
     * 默认第一页
     */
    Integer pageIndex = 1;

    /**
     * 默认每页10条
     */
    Integer pageSize = 10;


    public Integer getPageIndex() {
        return pageIndex == null ? 1 : pageIndex;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }
}
