package com.tensua.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客评论
 * @TableName blog_comment
 */
@TableName(value ="blog_comment")
@Data
public class BlogComment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    private Long blogId;

    /**
     * 博客作者id
     */
    private Long blogAuthorId;

    /**
     * 
     */
    private String comment;

    /**
     * 
     */
    private Integer version;

    /**
     * 评论类型：1 普通博客 2 系统博客
     */
    private Integer type;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private Integer isDelete;

    /**
     * 
     */
    private String headImgUrl;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 浏览器标识
     */
    private String browserModel;

    /**
     * 
     */
    private Long createUserId;

    /**
     * 
     */
    private String createUserName;

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
    private Integer updateUserName;

    /**
     * 
     */
    private Date updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}