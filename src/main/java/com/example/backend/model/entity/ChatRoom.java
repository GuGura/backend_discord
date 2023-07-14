package com.example.backend.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
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
}
