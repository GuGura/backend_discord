package com.example.backend.service;

import com.example.backend.mapper.RedisToMariaDBMapper;
import com.example.backend.model.entity.ChatMessage;
import com.example.backend.model.entity.User;
import com.example.backend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;

    @Qualifier("chatChannelTopic")
    private final ChannelTopic chatChannelTopic;

    @Qualifier("alarmChannelTopic")
    private final ChannelTopic alarmChannelTopic;

    private final RedisToMariaDBMapper redisToMariaDBMapper;

    public void saveChatMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public void publishMessage(ChatMessage message) {
        redisTemplate.convertAndSend(chatChannelTopic.getTopic(), message);
    }

    public void publishAlarm(User alarm) {
        redisTemplate.convertAndSend(alarmChannelTopic.getTopic(), alarm);
    }

    public List<ChatMessage> getChatMessagesFromMariaDB(String roomId) {
        List<ChatMessage> chatMessages = redisToMariaDBMapper.getChatMessagesFromMariaDB(roomId);
        chatMessages.addAll(chatMessageRepository.getAllChatMessage());
        return chatMessages;
    }
}
