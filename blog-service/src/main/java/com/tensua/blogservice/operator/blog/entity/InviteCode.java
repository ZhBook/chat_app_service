package com.tensua.blogservice.operator.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 邀请码
 * @TableName invite_code
 */
@TableName(value ="invite_code")
@Data
public class InviteCode implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String code;

    /**
     * code所绑定的用户id
     */
    private Long userId;

    /**
     * 
     */
    private Date createDatetime;

    /**
     * 
     */
    private Date updateDatetime;

    /**
     * 
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}