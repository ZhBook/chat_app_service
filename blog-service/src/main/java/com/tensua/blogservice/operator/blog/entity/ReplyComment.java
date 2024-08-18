package com.tensua.blogservice.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复评论
 * @TableName reply_comment
 */
@TableName(value ="reply_comment")
@Data
public class ReplyComment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    private Long commentId;

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
    private String updateUserName;

    /**
     * 
     */
    private Date updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}