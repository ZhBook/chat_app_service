package com.tensua.operator.chat.controller;

import com.tensua.data.BaseResult;
import com.tensua.operator.chat.facade.MessageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhooke
 * @since 2022/1/19 10:16
 **/
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageFacade messageFacade;

    /**
     * 发送消息反馈
     * @param msgId
     * @return
     */
    @PostMapping("/send/confirm/{msgId}")
    public BaseResult<Boolean> sendConfirmMsg(@PathVariable("msgId")Long msgId){
        return BaseResult.succeed(messageFacade.sendConfirmMsg(msgId));
    }

    /**
     * 接收消息反馈
     * @param msgId
     * @return
     */
    @PostMapping("/receive/confirm/{msgId}")
    public BaseResult<Boolean> receiveConfirmMsg(@PathVariable("msgId")Long msgId){
        return BaseResult.succeed(messageFacade.receiveConfirmMsg(msgId));
    }
}
