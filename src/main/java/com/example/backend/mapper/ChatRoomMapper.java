package com.example.backend.mapper;

import com.example.backend.model.entity.ChatRoom;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatRoomMapper {

    @Insert("insert into chat_room (room_uid,channel_uid,room_name,room_type) values(#{room_uid},#{channel_uid},#{room_name},#{room_type})")
    void save(ChatRoom chatRoom);

    @Select("select * from chat_room where channel_uid = #{channelUID} and room_type = 'Text'")
    Optional<ChatRoom> findTextRoom(@Param("channelUID") int channelUID);

    @Select("select * from chat_room where channel_uid = #{channelUID} and room_type = 'Voice'")
    Optional<ChatRoom> findVoiceRoom(@Param("channelUID") int channelUID);

    @Select("select * from chat_room where channel_uid = #{channelUID} and room_type = 'Text'")
    List<ChatRoom> findTextRoomList(@Param("channelUID") int channelUID);

    @Select("select * from chat_room where channel_uid = #{channelUID} and room_type = 'Voice'")
    List<ChatRoom> findVoiceRoomList(@Param("channelUID") int channelUID);

    @Delete("Delete from chat_room where channel_uid = #{channelUID}")
    void deleteRooms(@Param("channelUID") int channelUID);
}
