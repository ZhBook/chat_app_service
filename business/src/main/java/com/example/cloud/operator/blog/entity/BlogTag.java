package com.example.cloud.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客标签
 * @TableName blog_tag
 */
@TableName(value ="blog_tag")
@Data
public class BlogTag implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    private String name;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}