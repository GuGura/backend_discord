package com.example.backend.controller;

import com.example.backend.model.entity.ChatMessage;
import com.example.backend.model.entity.User;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerProperties.API_VERSION)
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        chatService.saveChatMessage(message);
        chatService.publishMessage(message);
    }

    @MessageMapping("/alarm")
    public void alarm(User alarm){
        chatService.publishAlarm(alarm);
    }

    @GetMapping("/chat/message/list/{roomId}")
    public List<ChatMessage> getChatMessages(@PathVariable String roomId) {
        return chatService.getChatMessagesFromMariaDB(roomId);
    }
}