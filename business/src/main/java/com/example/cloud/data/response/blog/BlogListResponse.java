package com.example.cloud.data.response.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author zhooke
 * @since 2022/3/17 17:55
 **/
@Data
public class BlogListResponse {
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
    @Size(max = 300)
    private String content;

    /**
     *
     */
    private String authorName;

    /**
     * 是否置顶：0 否 1是
     */
    private Integer isTop;

    /**
     * 浏览次数，每次访问+1
     */
    private Integer blogBrowse;

    /**
     * 评论条数
     */
    private Integer commentNum;

    /**
     * 是否私密 0否 1是
     */
    private Integer isPrivate;

    /**
     * 是否原创 0否 1是
     */
    private Integer isOriginal;

    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
