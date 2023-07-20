package com.example.backend.service;

import com.example.backend.model.entity.ChatMessage;
import com.example.backend.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriberService {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    public void sendAlarm(String publishAlarm){
        try{
            // AlarmMessage 객체로 맵핑
            User alarmMessage = objectMapper.readValue(publishAlarm, User.class);
            // 알람을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/alarm/" + alarmMessage.getChannel_uid(), alarmMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

}