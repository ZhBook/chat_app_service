package com.tensua.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Slf4j
@Component
public class WebsocketInitialization {

    @Resource
    private WebsocketChannelInitializer websocketChannelInitializer;

    /**
     * webSocket的端口
     */
    @Value("${webSocket.netty.port}")
    private Integer port;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @Async
    public void init() throws InterruptedException {

        //bossGroup连接线程组，主要负责接受客户端连接，一般一个线程足矣
        bossGroup = new NioEventLoopGroup(1);
        //workerGroup工作线程组，主要负责网络IO读写
        workerGroup = new NioEventLoopGroup();
        try {
            //启动辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //bootstrap绑定两个线程组
            serverBootstrap.group(bossGroup, workerGroup);
            //设置通道为NioChannel
            serverBootstrap.channel(NioServerSocketChannel.class);
            //可以对入站\出站事件进行日志记录，从而方便我们进行问题排查。
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            //设置自定义的通道初始化器，用于入站操作
            serverBootstrap.childHandler(websocketChannelInitializer);
            // 设置监听端口
            serverBootstrap.localAddress(new InetSocketAddress(port));
            //启动服务器,本质是Java程序发起系统调用，然后内核底层起了一个处于监听状态的服务，生成一个文件描述符FD
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            //异步
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    /** 释放资源
     * @throws InterruptedException
     * */
    @PreDestroy
    public void destroy() throws InterruptedException {
        if(bossGroup != null){
            bossGroup.shutdownGracefully().sync();
        }
        if(workerGroup != null){
            workerGroup.shutdownGracefully().sync();
        }
    }
}

