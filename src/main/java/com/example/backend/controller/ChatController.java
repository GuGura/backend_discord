package com.example.backend.controller;

import com.example.backend.model.entity.ChatMessage;
import com.example.backend.repository.ChatMessageRepository;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        chatService.saveChatMessage(message);
        chatService.publishMessage(message);
    }

//    @GetMapping("/enter/{roomId}")
//    @ResponseBody
//    public List<ChatMessage> getChatMessages(@PathVariable String roomId) {
//        return chatService.getChatMessagesFromDB(roomId);
//    }
}

