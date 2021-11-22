package com.example.cloud.data.response.friends;

import lombok.Data;

/**
 * @author:70968 Date:2021-11-21 10:11
 */
@Data
public class NewFriendResponse {
    /**
     *
     */
    private Long id;

    /**
     * 发送方用户id
     */
    private Long sendUserId;

    /**
     * 发送方用户名
     */
    private String sendUserUsername;

    /**
     * 发送方头像
     */
    private String sendHeadImgUrl;

    /**
     * 发送方昵称
     */
    private String sendUserNickname;

    /**
     * 请求消息
     */
    private String message;

    /**
     * 消息状态：0未接收，1已接收
     */
    private int infoState;

    /**
     * 是否同意
     */
    private int isAgree;
}
