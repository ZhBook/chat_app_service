package com.example.cloud.chat.service;

import com.example.cloud.chat.entity.ChatMessage;

/**
 * @author 70968
 */
public interface PushService {
    /**
     * 推送给指定用户
     * @param userId
     * @param msg
     */
    String pushMsgToOne(String userId, ChatMessage msg);

}