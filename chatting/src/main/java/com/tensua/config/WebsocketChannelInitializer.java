package com.tensua.config;

import com.tensua.handler.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebsocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketHandler webSocketHandler;

    /** * webSocket协议名 */
    private static final String WEBSOCKET_PROTOCOL = "WebSocket";

    @Value("${webSocket.netty.path}")
    private String path;

    @Value("${webSocket.netty.maxContentLength}")
    private Integer maxContentLength;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //获取pipeline通道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //因为基于http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());
        //是以块方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());

        //针对客户端，若10s内无读事件则触发心跳处理方法HeartBeatHandler#userEventTriggered
//        pipeline.addLast(new IdleStateHandler(10 , 0 , 0));
        //自定义空闲状态检测(自定义心跳检测handler)
//        pipeline.addLast(new HeartBeatHandler());
        /*
          说明
          1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
          2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
        */
        pipeline.addLast(new HttpObjectAggregator(maxContentLength));
        /* 说明
          1. 对应websocket ，它的数据是以 帧(frame) 形式传递
          2. 可以看到WebSocketFrame 下面有六个子类
          3. 浏览器请求时 ws://localhost:7000/msg 表示请求的uri
          4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
          5. 是通过一个 状态码 101
        */
        /* 说明： 一、对应webSocket，它的数据是以帧（frame）的形式传递 二、浏览器请求时 ws://localhost:58080/xxx 表示请求的uri 三、核心功能是将http协议升级为ws协议，保持长链接 */
        pipeline.addLast(new WebSocketServerProtocolHandler(path, WEBSOCKET_PROTOCOL, true, 65536 * 10));
        //自定义的handler ，处理业务逻辑
        pipeline.addLast(webSocketHandler);
    }
}
