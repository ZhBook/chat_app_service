package com.example.cloud.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客
 * @TableName blog_list
 */
@TableName(value ="blog_list")
@Data
public class BlogList implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Long authorId;

    /**
     * 
     */
    private String authorName;

    /**
     * 
     */
    private String picture;

    /**
     * 更新次数
     */
    private Integer version;

    /**
     * 浏览次数，每次访问+1
     */
    private Integer blogBrowse;

    /**
     * 是否草稿：0 否 1 是，默认0
     */
    private Integer isDraft;

    /**
     * 是否置顶：0 否 1是
     */
    private Integer isTop;

    /**
     * 0正常 1删除
     */
    private Integer isDelete;

    /**
     * 是否私密 0否 1是
     */
    private Integer isPrivate;

    /**
     * 文章来源 1原创 2转摘 3翻译
     */
    private Integer isOriginal;

    /**
     * 
     */
    private Long createUserId;

    /**
     * 
     */
    private Date createDate;

    /**
     * 
     */
    private Long updateUserId;

    /**
     * 
     */
    private Date updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}