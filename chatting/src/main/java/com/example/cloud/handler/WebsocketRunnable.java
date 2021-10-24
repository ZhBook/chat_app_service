package com.example.cloud.handler;

import com.example.cloud.chat.entity.ChatMessage;
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
            System.out.println(Thread.currentThread().getName()+"--"+LocalDateTime.now().toString());
            channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(LocalDateTime.now().toString()));
        } catch (Exception e) {
            log.error("websocket服务器推送消息发生错误：",e);
        }
    }
}
