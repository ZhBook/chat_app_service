package com.tensua.blogservice.data.request.blog;

import lombok.Data;

/**
 * @author zhooke
 * @since 2022/3/17 17:39
 **/
@Data
public class BlogListRequest{
    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 作者id
     */
    private Long userId;

    /**
     *
     */
    private String authorName;

    /**
     * 是否置顶：0 否 1是
     */
    private Integer isTop;

    /**
     * 是否草稿：0 否 1 是，默认0
     */
    private Integer isDraft;

    /**
     *
     */
    private Integer isDelete;

    /**
     * 是否私密 0否 1是
     */
    private Integer isPrivate;

    /**
     * 是否原创 0否 1是
     */
    private Integer isOriginal;

    /**
     * 默认第一页
     */
    Integer pageIndex = 1;

    /**
     * 默认每页10条
     */
    Integer pageSize = 10;
}
