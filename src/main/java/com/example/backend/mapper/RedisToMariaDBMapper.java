package com.example.backend.mapper;

import com.example.backend.model.entity.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface RedisToMariaDBMapper {

    @Insert("INSERT INTO chat_message (room_uid, username, sender, message, send_date) VALUES (#{roomId}, #{userName}, #{sender}, #{message}, #{sendDate})")
    void insertChatMessage(ChatMessage chatMessage);

    @Select("SELECT COUNT(*) FROM chat_message WHERE room_uid = #{roomId} AND username = #{userName} AND sender = #{sender} AND message = #{message} AND send_date = #{sendDate}")
    int countChatMessage(@Param("roomId") String roomId,
                         @Param("userName") String userName,
                         @Param("sender") String sender,
                         @Param("message") String message,
                         @Param("sendDate") Date sendDate);


    @Select("SELECT * FROM chat_message WHERE room_uid = #{roomId}")
    @Results({
            @Result(property = "roomId", column = "room_uid"),
            @Result(property = "sendDate", column = "send_date")
    })
    List<ChatMessage> getChatMessagesFromMariaDB(String roomId);

}
