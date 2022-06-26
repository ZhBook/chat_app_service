package com.tensua.operator.chat.controller;

import com.tensua.operator.chat.entity.ChatMessage;
import com.tensua.operator.chat.service.PushService;
import com.tensua.data.BaseResult;
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
    public BaseResult pushMsgToOne(@RequestParam("receiveId") Long receiveId, @RequestBody ChatMessage msg) {
        return BaseResult.succeed(pushService.pushMsgToOne(receiveId, msg));
    }

}