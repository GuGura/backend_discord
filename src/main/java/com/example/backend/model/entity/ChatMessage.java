package com.example.backend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ChatMessage implements Serializable {

    public ChatMessage() {
    }

    @Builder
    public ChatMessage(MessageType type, String roomId,String userName, String sender, String message) {
        this.type = type;
        this.roomId = roomId;
        this.userName = userName;
        this.sender = sender;
        this.message = message;
        this.sendDate = new Date();
    }

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String userName; // 메세지 보낸 사람 (email)
    private String sender; // 메시지 보낸 사람 (nickName)
    private String message; // 메시지
    private Date sendDate; // 받은 시각
}
