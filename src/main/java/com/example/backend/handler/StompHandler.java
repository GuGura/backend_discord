package com.example.backend.handler;

import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entity.DisconnectEvent;
import com.example.backend.model.entity.User;
import com.example.backend.service.ChatRoomService;
import com.example.backend.service.RedisSubscriberService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler extends ChannelInterceptorAdapter {

    private final UserMapper userMapper;
    private final ChatRoomService chatRoomService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private Map<String, String> sessionIdUsernameMap = new ConcurrentHashMap<>();

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        String username;

        switch (accessor.getCommand()) {
            case CONNECT:
                // 유저가 Websocket으로 connect()를 한 뒤 호출됨
                username = accessor.getFirstNativeHeader("username");
                sessionIdUsernameMap.put(sessionId, username);
                User connectUser = chatRoomService.createUser(username, "ENTER");
                userMapper.insertUserState(connectUser);
                log.info("SUBSCRIBED {}, {}", username);
                break;
            case DISCONNECT:
                username = sessionIdUsernameMap.get(sessionId);
                sessionIdUsernameMap.remove(sessionId); // Remember to clean up after disconnect
                User disconnectUser = chatRoomService.createUser(username, "QUIT");
                userMapper.insertUserState(disconnectUser);
                log.info("DISCONNECTED {}, {}", username);


                // 발행할 이벤트 객체 생성
                DisconnectEvent disconnectEvent = new DisconnectEvent(this, username);

                // 이벤트 발행
                applicationEventPublisher.publishEvent(disconnectEvent);
                break;


            default:
                break;
        }
    }
}
