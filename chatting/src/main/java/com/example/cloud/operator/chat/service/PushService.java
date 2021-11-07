package com.example.cloud.operator.chat.service;

import com.example.cloud.operator.chat.entity.ChatMessage;

/**
 * @author 70968
 */
public interface PushService {
    /**
     * 推送给指定用户
     * @param userId
     * @param msg
     */
    String pushMsgToOne(Long userId, ChatMessage msg);

}