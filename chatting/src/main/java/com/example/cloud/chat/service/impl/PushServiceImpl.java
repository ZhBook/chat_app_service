package com.example.cloud.chat.service.impl;

import com.example.cloud.chat.entity.ChatMessage;
import com.example.cloud.chat.service.PushService;
import com.example.cloud.handler.WebSocketHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PushServiceImpl implements PushService {

    @Override
    public String pushMsgToOne(String receiveId, ChatMessage msg) {
        //获取所有连接的客户端,如果是集群环境使用redis的hash数据类型存储即可
        Map<String, Channel> channelMap = WebSocketHandler.getChannelMap();
        //获取与用户主键绑定的channel,如果是集群环境使用redis的hash数据类型存储即可
        Map<String, Long> clientMap = WebSocketHandler.getClientMap();
        //解决问题六,websocket集群中一个客户端向其他客户端主动发送消息，如何实现？
        Long friendId = msg.getFriendId();
        Channel channel = channelMap.get(friendId.toString());
        channel.eventLoop().execute(() -> channel.writeAndFlush(new TextWebSocketFrame(Thread.currentThread().getName() + "服务器时间" + LocalDateTime.now() + "wdy")));
        return "发送成功";
    }

}
