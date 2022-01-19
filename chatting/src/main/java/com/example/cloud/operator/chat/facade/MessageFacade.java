package com.example.cloud.operator.chat.facade;

import com.example.cloud.enums.HaveReadStateEnum;
import com.example.cloud.enums.MessageStateEnum;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.chat.entity.ChatMessage;
import com.example.cloud.operator.chat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zhooke
 * @since 2022/1/19 10:17
 **/
@Service
public class MessageFacade {
    @Autowired
    private ChatMessageService messageService;

    /**
     * 发送消息反馈
     * @param msgId
     * @return
     */
    public Boolean sendConfirmMsg(Long msgId) {
        ChatMessage message = messageService.getById(msgId);
        if (Objects.isNull(message)){
            throw new BusinessException("消息不存在");
        }
        message.setState(MessageStateEnum.SUCCESS.getState());
        messageService.updateById(message);
        return Boolean.TRUE;
    }

    /**
     * 接收消息反馈
     * @param msgId
     * @return
     */
    public Boolean receiveConfirmMsg(Long msgId) {
        ChatMessage message = messageService.getById(msgId);
        if (Objects.isNull(message)){
            throw new BusinessException("消息不存在");
        }
        message.setHaveRead(HaveReadStateEnum.REDA.getCode());
        messageService.updateById(message);
        return Boolean.TRUE;
    }
}
