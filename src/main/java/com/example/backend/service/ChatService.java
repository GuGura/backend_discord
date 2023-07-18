package com.example.backend.service;

import com.example.backend.mapper.RedisToMariaDBMapper;
import com.example.backend.model.entity.ChatMessage;
import com.example.backend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChannelTopic channelTopic;
    private final RedisToMariaDBMapper redisToMariaDBMapper;

    public void saveChatMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public void publishMessage(ChatMessage message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

    public List<ChatMessage> getChatMessagesFromMariaDB(String roomId) {
        List<ChatMessage> chatMessages = redisToMariaDBMapper.getChatMessagesFromMariaDB(roomId);
        chatMessages.addAll(chatMessageRepository.getAllChatMessage());
        return chatMessages;
    }
}
