package com.tensua.operator.friend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 添加好友请求表
 * @author 70968
 * @TableName friend_request
 */
@TableName(value ="friend_request")
@Data
public class FriendRequest implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送方用户id
     */
    private Long sendUserId;

    /**
     * 
     */
    private String sendUserUsername;

    /**
     * 发送者头像
     */
    private String sendHeadImgUrl;

    /**
     * 发送者昵称
     */
    private String sendUserNickname;

    /**
     * 接收方用户id
     */
    private Long receiveUserId;

    /**
     * 
     */
    private String receiveUserUsername;

    /**
     * 请求消息
     */
    private String message;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 消息状态：0未接收，1已接收
     */
    private int infoState;

    /**
     * 是否同意
     */
    private int isAgree;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}