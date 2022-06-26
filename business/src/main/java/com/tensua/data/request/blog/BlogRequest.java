package com.tensua.data.request.blog;

import com.tensua.system.UserBeanRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhooke
 * @since 2022/3/17 18:11
 **/
@Data
public class BlogRequest extends UserBeanRequest {

    /**
     * blogId
     */
    @NotNull(message = "博客id不能为空")
    private Long blogId;

    /**
     *
     */
    @NotNull(message = "作者id不能为空")
    private Long authorId;

    /**
     *
     */
    @NotBlank(message = "作者id名称为空")
    private String authorName;
    /**
     *
     */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /**
     *
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     *
     */
    private String picture;

    /**
     * tag集合 用逗号分割
     */
    private String tags;

    /**
     * 是否置顶：0 否 1是
     */
    private Integer isTop;

    /**
     * 是否草稿：0 否 1 是，默认0
     */
    private Integer isDraft;

    /**
     * 是否私密 0否 1是
     */
    private Integer isPrivate;

    /**
     * 是否原创 0否 1是
     */
    private Integer isOriginal;
}
