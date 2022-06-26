package com.tensua.operator.chat.service.impl;

import cn.hutool.json.JSONUtil;
import com.tensua.data.SocketResult;
import com.tensua.enums.HaveReadStateEnum;
import com.tensua.enums.MessageTypeEnum;
import com.tensua.handler.WebSocketHandler;
import com.tensua.operator.chat.entity.ChatMessage;
import com.tensua.operator.chat.service.ChatMessageService;
import com.tensua.operator.chat.service.PushService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class PushServiceImpl implements PushService {

    @Autowired
    private ChatMessageService messageService;

    /**
     * 所有发送的消息，都要在以TextWebSocketFrame()作为载体发送
     **/

    @Override
    public String pushMsgToOne(Long receiveId, ChatMessage msg) {
        //获取所有连接的客户端,如果是集群环境使用redis的hash数据类型存储即可
        Map<String, Channel> channelMap = WebSocketHandler.getChannelMap();
        //获取与用户主键绑定的channel,如果是集群环境使用redis的hash数据类型存储即可
        Map<Long,String > clientMap = WebSocketHandler.getClientMap();
        //解决问题六,websocket集群中一个客户端向其他客户端主动发送消息，如何实现？
        if (!clientMap.containsValue(receiveId)){
            //如果接收方不在线，将消息存入数据库中，当该用户登陆的时候，初始化时，将这些未读消息发送给用户
            msg.setHaveRead(HaveReadStateEnum.UNREAD.getCode());
            messageService.save(msg);
            log.info("用户不在线，消息将保存在数据库中：", msg);
            return "发送成功";
        }
        String channelStr = clientMap.get(receiveId);
        if (StringUtils.isNotBlank(channelStr)){
            String response = JSONUtil.toJsonStr(SocketResult.succeed(msg, MessageTypeEnum.TEXT.getCode()));
            Channel channel = channelMap.get(channelStr);
            if (Objects.nonNull(channel)){
                channel.eventLoop().execute(() -> channel.writeAndFlush(new TextWebSocketFrame(response)));
                //值得注意的是这个executor的类是我们初始化eventloopgroup时分配好的一个ThreadPerTaskExecutor
//                channel.eventLoop().execute(() -> channel.writeAndFlush(new TextWebSocketFrame(Thread.currentThread().getName() + "服务器时间" + LocalDateTime.now() + "wdy")));
                //将消息入库
                msg.setHaveRead(HaveReadStateEnum.REDA.getCode());
                messageService.save(msg);
                log.info("消息发送成功:", msg);
            }
        }

        return "发送成功";
    }
}
