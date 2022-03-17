package com.example.cloud.data.request.blog;

import com.example.cloud.system.PagingUserBaseRequest;
import lombok.Data;

/**
 * @author zhooke
 * @since 2022/3/17 17:39
 **/
@Data
public class BlogListRequest extends PagingUserBaseRequest {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String authorName;

    /**
     * 是否置顶：0 否 1是
     */
    private Integer isTop;

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
}
