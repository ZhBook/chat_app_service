package com.example.cloud.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.chat.entity.ChatMessage;
import com.example.cloud.chat.mapper.ChatMessageMapper;
import com.example.cloud.chat.service.ChatMessageService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
implements ChatMessageService{

}
