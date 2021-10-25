package com.example.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class WebsocketApplication {

    @Resource
    private WebsocketInitialization websocketInitialization;

    @PostConstruct
    public void start() {
        //需要开启一个新的线程来执行netty server 服务器
        new Thread(() -> {
            try {
                log.info(Thread.currentThread().getName() + ":websocket启动中......");
                websocketInitialization.init();
                log.info(Thread.currentThread().getName() + ":websocket启动成功！！！");
            } catch (InterruptedException e) {
                log.error("websocket发生错误：", e);
            }
        }).start();
    }
}

