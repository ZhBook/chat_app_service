package com.tensua.operator.friend.facade;

import com.tensua.api.UserFeignClient;
import com.tensua.enums.MessageTypeEnum;
import com.tensua.operator.chat.entity.ChatMessage;
import com.tensua.operator.chat.service.PushService;
import com.tensua.operator.friend.entity.FriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author:70968 Date:2021-10-30 11:30
 */
@Service
public class FriendFacade {
    @Qualifier("userFeignFallbackClient")
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PushService pushService;

    public Boolean addFriend(FriendRequest request) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFriendId(request.getSendUserId());
        chatMessage.setUserId(request.getReceiveUserId());
        chatMessage.setCreateTime(request.getCreateTime().toString());
        chatMessage.setContext(request.getMessage());
        chatMessage.setHeadImgUrl(request.getSendHeadImgUrl());
        chatMessage.setType(MessageTypeEnum.FRIEND_REQUEST.getCode());
        chatMessage.setHaveRead(0);
        chatMessage.setState(0);
        chatMessage.setUrl("");
        chatMessage.setId(request.getId());
        pushService.pushMsgToOne(request.getReceiveUserId(), chatMessage);
        userFeignClient.addFriend(request.getReceiveUserId(), request.getMessage());
        //todo 解决好友不在线时，发送添加好友请求
        return Boolean.TRUE;
    }
}
