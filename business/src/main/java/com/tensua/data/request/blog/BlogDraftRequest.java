package com.tensua.data.request.blog;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhooke
 * @since 2022/4/12 10:34
 **/
@Data
public class BlogDraftRequest {

    /**
     * 默认第一页
     */
    Integer pageIndex = 1;

    /**
     * 默认每页10条
     */
    Integer pageSize = 10;

    /**
     * 作者id
     */
    @NotNull(message = "作者id不能为空")
    private Long userId;
}
