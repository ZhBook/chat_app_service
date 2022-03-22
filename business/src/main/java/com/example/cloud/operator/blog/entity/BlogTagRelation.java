package com.example.cloud.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * blog与tag参照表
 * @TableName blog_tag_relation
 */
@TableName(value ="blog_tag_relation")
@Data
public class BlogTagRelation implements Serializable {
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
    private Long tagId;

    /**
     * 
     */
    private String tagName;

    /**
     * 
     */
    private Long createUserId;

    /**
     * 
     */
    private Date createDatetime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}