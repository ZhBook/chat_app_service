package com.example.cloud.operator.friends.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_relation
 */
@TableName(value ="user_relation")
@Data
public class UserRelation implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long friendId;

    /**
     * 
     */
    private Date createTime;

    /**
     * 是否关系：0有关系，1没有关系
     */
    private String isRelation;

    /**
     * 
     */
    private Date delTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}