package com.example.cloud.chat.controller;

import com.example.cloud.chat.entity.ChatMessage;
import com.example.cloud.chat.service.PushService;
import com.example.cloud.enums.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send")
public class PushController {

    @Autowired
    private PushService pushService;


    /**
     * 推送给指定用户
     *
     * @param receiveId
     * @param msg
     */
    @PostMapping("/friend")
    public BaseResult pushMsgToOne(@RequestParam("receiveId") String receiveId, @RequestBody ChatMessage msg) {
        return BaseResult.succeed(pushService.pushMsgToOne(receiveId, msg));
    }

}