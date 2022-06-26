package com.tensua.handler;

import cn.hutool.json.JSONUtil;
import com.tensua.data.SocketResult;
import com.tensua.enums.MessageTypeEnum;
import com.tensua.operator.chat.entity.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Slf4j
public class WebsocketRunnable implements Runnable {

    private ChannelHandlerContext channelHandlerContext;

    private ChatMessage messageRequest;

    private HashMap<String, Object> hashMap = new HashMap<>();

    public WebsocketRunnable(ChannelHandlerContext channelHandlerContext, ChatMessage messageRequest) {
        this.channelHandlerContext = channelHandlerContext;
        this.messageRequest = messageRequest;
    }

    @Override
    public void run() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(dtf);
            hashMap.put("PONG", now);
            log.info(Thread.currentThread().getName() + "--" + now);
            String response = JSONUtil.toJsonStr(SocketResult.succeed(hashMap, MessageTypeEnum.PONG.getCode()));
            channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(response));
        } catch (Exception e) {
            log.error("websocket服务器推送消息发生错误：", e);
        }
    }
}
