package com.example.cloud.operator.chat.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author:70968 Date:2021-10-23 23:02
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    Long id;
    Long userId;
    Long friendId;
    String context;
    String url;
    String headImgUrl;
    /**
     * 消息类型
     * 0：文本
     * 1：语音
     * 2：图片
     * 3：视频
     */
    Integer type;
    String createTime;
    Integer haveRead;
    /**
     * 消息状态
     * 0：发送成功
     * 1：发送失败
     * 2：删除
     */
    Integer state;
}
