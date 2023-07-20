package com.example.backend.service;

import com.example.backend.mapper.RedisToMariaDBMapper;
import com.example.backend.model.entity.ChatMessage;
import com.example.backend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisToMariaDBService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisToMariaDBMapper redisToMariaDBMapper;

    @Scheduled(fixedDelay = 100000)
    private void saveChatList() {
        List<ChatMessage> chatMessages = chatMessageRepository.getAllChatMessage();
        for (ChatMessage chatMessage : chatMessages) {
            boolean isDuplicate = checkDuplicateChatMessage(chatMessage);
            if (isDuplicate) {
                continue;
            }
            redisToMariaDBMapper.insertChatMessage(chatMessage);
        }
        System.out.println("Redis save DB");
        chatMessageRepository.deleteChatMessages();
    }
    // 중복된 값 제외
    private boolean checkDuplicateChatMessage(ChatMessage chatMessage) {
        if(chatMessage.getSendDate() == null) {
            return true;
        }
        int count = redisToMariaDBMapper.countChatMessage(chatMessage.getRoomId(), chatMessage.getUserName(), chatMessage.getSender(), chatMessage.getMessage(), chatMessage.getSendDate());
        return count > 0;
    }
}
