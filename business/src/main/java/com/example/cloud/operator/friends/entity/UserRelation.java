package com.example.cloud.operator.friends.entity;

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
    private String nickname;

    /**
     *
     */
    private String headImgUrl;

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
}