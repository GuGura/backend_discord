package com.example.backend.handler;

import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entity.DisconnectEvent;
import com.example.backend.model.entity.User;
import com.example.backend.service.ChatRoomService;
import com.example.backend.service.RedisSubscriberService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class DisconnectEventHandler {

    private final UserMapper userMapper;
    private final ChatRoomService chatRoomService;
    private final RedisSubscriberService redisSubscriberService;
    private final Gson gson;

    @EventListener
    public void handleDisconnectEvent(DisconnectEvent event) {
        String username = event.getUsername();

        User disconnectUser = chatRoomService.createUser(username, "QUIT");
        userMapper.insertUserState(disconnectUser);
        log.info("DISCONNECTED {}, {}", username);

        List<User> onlineUsers = chatRoomService.sendToOnlineUsers(username);
        for (User user : onlineUsers) {
            Map<String, Object> alarmMessage = new HashMap<>();
            alarmMessage.put("nickname", user.getNickname());
            alarmMessage.put("channel_uid", user.getChannel_uid());
            alarmMessage.put("state", "QUIT");

            // Convert the message map to JSON string
            String publishAlarm = gson.toJson(alarmMessage);
            System.out.println("111" + publishAlarm);

            // Use the RedisSubscriberService to send the alarm message
            redisSubscriberService.sendAlarm(publishAlarm);
        }
    }
}
