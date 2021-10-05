package com.example.cloud.service.Impl;




import org.springframework.stereotype.Component;

/**
 * 消费者监听器
 **/
//@Slf4j
//@Component
// topic需要和生产者的topic一致，consumerGroup属性是必须指定的，内容可以随意
//@RocketMQMessageListener(topic = "test-topic", consumerGroup = "consumer-group")
//public class TestConsumerListener implements RocketMQListener<MyMessage> {

    /**
     * 监听到消息的时候就会调用该方法
     */
    /*@Override
    public void onMessage(MyMessage message) {
        log.info("从test-topic中监听到消息");
        log.info(JSON.toJSONString(message));
    }*/
//}