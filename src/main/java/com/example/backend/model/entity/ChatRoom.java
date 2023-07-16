package com.example.backend.model.entity;

import com.example.backend.mapper.ChatRoomMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    private String room_uid;
    private int channel_uid;
    private String room_name;
    private String room_type;

    @Builder
    public ChatRoom(String channel_uid, String room_name, String room_type) {
        this.room_uid = UUID.randomUUID().toString();
        this.channel_uid = Integer.parseInt(channel_uid);
        this.room_name = room_name;
        this.room_type = room_type;
    }
    public static void initRoom(ChatRoomMapper mapper,int channel_uid){
        String[] room_type = {"Text","Voice"};
        for (String s : room_type) {
            ChatRoom chatRoom = ChatRoom.builder()
                    .channel_uid(String.valueOf(channel_uid))
                    .room_name("일반")
                    .room_type(s)
                    .build();
            mapper.save(chatRoom);
        }
    }

}
