package com.example.cloud.operator.blog.entity;

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
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long blogId;

    /**
     * 
     */
    private String comment;

    /**
     * 
     */
    private Integer version;

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