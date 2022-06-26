package com.tensua.operator.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tensua.operator.chat.entity.ChatMessage;
import com.tensua.operator.chat.mapper.ChatMessageMapper;
import com.tensua.operator.chat.service.ChatMessageService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
implements ChatMessageService{

}
