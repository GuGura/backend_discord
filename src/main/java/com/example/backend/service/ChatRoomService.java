package com.example.backend.service;

import com.example.backend.mapper.ChatRoomMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entity.ChatRoom;
import com.example.backend.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomMapper chatRoomMapper;
    private final UserMapper userMapper;


    public void createChatRoom(ChatRoom chatRoom) {
        chatRoomMapper.save(chatRoom);
    }

    public Map<String,Object> getChatRooms(int channelUID){
        List<ChatRoom> textRoom = chatRoomMapper.findTextRoomList(channelUID);
        List<ChatRoom> voiceRoom = chatRoomMapper.findVoiceRoomList(channelUID);
        Map<String,Object> list = new HashMap<>();
        list.put("textRoom",textRoom);
        list.put("voiceRoom", voiceRoom);
        return list;
    }

    public User createUser(String username, String state) {
        return User.builder()
                .username(username)
                .state(state)
                .connect_date(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public List<User> sendToOnlineUsers(String username) {
        return userMapper.sendToOnlineUser(username);
    }

}
