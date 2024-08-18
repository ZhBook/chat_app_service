package com.tensua.blogservice.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客后台配置
 * @TableName blog_menus
 */
@TableName(value ="blog_menus")
@Data
public class BlogMenus implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String menuName;

    /**
     * code
     */
    private String menuCode;

    /**
     * 父级id
     */
    private Integer fatherId;

    /**
     * 角标
     */
    private Integer index;

    /**
     * 0:未启用，1:启用
     */
    private Integer isDisable;

    /**
     * 描述
     */
    private String description;

    /**
     * 
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}