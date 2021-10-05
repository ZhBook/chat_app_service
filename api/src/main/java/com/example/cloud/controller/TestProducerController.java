package com.example.cloud.controller;

import com.example.cloud.pojo.MyMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 生产者
 **/
@RestController
@RequestMapping("/api/testRocketMQ")
public class TestProducerController {

    /**
     * 用于发送消息到 RocketMQ 的api
     */
    //@Resource
    //public RocketMQTemplate rocketMQTemplate;
    @GetMapping("/sendMsg")
    public String testSendMsg() {
        String topic = "test-topic";
        MyMessage message = new MyMessage();
        message.setId(1);
        message.setName("王霄");
        message.setStatus("default");
        message.setCreateTime(new Date());
        // 发送消息
        //rocketMQTemplate.convertAndSend(topic, message);

        return "send message success";
    }
}