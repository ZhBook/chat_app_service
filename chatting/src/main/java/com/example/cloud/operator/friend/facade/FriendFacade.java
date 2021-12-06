package com.example.cloud.operator.friend.facade;

import cn.hutool.json.JSONUtil;
import com.example.cloud.api.UserFeignClient;
import com.example.cloud.data.SocketResult;
import com.example.cloud.enums.MessageTypeEnum;
import com.example.cloud.handler.WebSocketHandler;
import com.example.cloud.operator.friend.entity.FriendRequest;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author:70968 Date:2021-10-30 11:30
 */
@Service
public class FriendFacade {
    @Qualifier("userFeignFallbackClient")
    @Autowired
    private UserFeignClient userFeignClient;

    public Boolean addFriend(FriendRequest request) {
        Map<String, Channel> channelMap = WebSocketHandler.getChannelMap();
        Map<Long,String > clientMap = WebSocketHandler.getClientMap();
        Long receiveUserId = request.getReceiveUserId();
        String channelId = clientMap.get(receiveUserId);
        if (StringUtils.isNotBlank(channelId)){
            Channel channel = channelMap.get(channelId);
            if (Objects.nonNull(channel)){
                String response = JSONUtil.toJsonStr(SocketResult.succeed(request, MessageTypeEnum.FRIEND_REQUEST.getCode()));
                channel.writeAndFlush(new TextWebSocketFrame(response));
            }
        }
        //todo 解决好友不在线时，发送添加好友请求
        return Boolean.TRUE;
    }
}
