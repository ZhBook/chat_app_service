package com.example.cloud.handler;

import cn.hutool.json.JSONUtil;
import com.example.cloud.data.SocketResult;
import com.example.cloud.enums.MessageTypeEnum;
import com.example.cloud.operator.chat.entity.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class WebsocketRunnable implements Runnable {

    private ChannelHandlerContext channelHandlerContext;

    private ChatMessage messageRequest;

    public WebsocketRunnable(ChannelHandlerContext channelHandlerContext,ChatMessage messageRequest) {
        this.channelHandlerContext = channelHandlerContext;
        this.messageRequest = messageRequest;
    }

    @Override
    public void run() {
        try {
            log.info(Thread.currentThread().getName()+"--"+ LocalDateTime.now());
            String response = JSONUtil.toJsonStr(SocketResult.succeed("PONG-"+LocalDateTime.now(), MessageTypeEnum.PONG.getCode()));
            channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(response));
        } catch (Exception e) {
            log.error("websocket服务器推送消息发生错误：",e);
        }
    }
}
