package com.example.cloud.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName blog_config
 */
@TableName(value ="blog_config")
@Data
public class BlogConfig implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 
     */
    private String typeName;

    /**
     * 
     */
    private String typeValue;

    /**
     * 
     */
    private String descriptionText;

    /**
     * 
     */
    private String createUserName;

    /**
     * 
     */
    private Long createUserId;

    /**
     * 
     */
    private Date createDatetime;

    /**
     * 
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}