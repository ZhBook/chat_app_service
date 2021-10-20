package com.example.cloud.system;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class PagingUserBaseRequest extends UserBeanRequest {

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
