package com.example.backend.service;

import com.example.backend.mapper.ChatRoomMapper;
import com.example.backend.model.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomMapper chatRoomMapper;
    public void createChatRoom(ChatRoom chatRoom) {

    }

    public Map<String,Object> getChatRooms(int channelUID){
        List<ChatRoom> textRoom = chatRoomMapper.findTextRoomList(channelUID);
        List<ChatRoom> voiceRoom = chatRoomMapper.findVoiceRoomList(channelUID);
        Map<String,Object> list = new HashMap<>();
        list.put("textRoom",textRoom);
        list.put("voiceRoom", voiceRoom);
        return list;
    }
}
