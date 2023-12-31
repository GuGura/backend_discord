package com.example.backend.mapper;


import com.example.backend.model.entity.Channel;
import com.example.backend.model.entity.ChannelMember;
import com.example.backend.model.MyChannelsDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChannelMapper {

    @Select("select u.channel_uid, u.user_uid, c.channel_title, c.channel_icon_url, c.channel_type, c.channel_invite_code " +
            "from channel_user u " +
            "left join channel c on u.channel_uid = c.channel_uid " +
            "where u.user_uid = #{userUID} order by channel_user_join_date desc")
    List<MyChannelsDTO> findChannelsByUserUID(@Param("userUID") int userUID);


    @Insert("Insert into channel (channel_title, channel_owner)" +
            "VALUES (#{channel_title}, #{channel_owner})")
    void saveChannel(@Param("channel_title") String channel_title, @Param("channel_owner") int channel_owner);

    //수정해야함
    @Select("select channel_uid from channel where channel_owner = #{channel_owner} " +
            "and channel_title = #{channel_title} " +
            " order by channel_uid desc limit 1")
    int findChannelUIDByUserUID(@Param("channel_title") String channel_title, @Param("channel_owner") int channel_owner);

    @Insert("Insert into channel_user (channel_uid, user_uid, channel_user_authority)" +
            "VALUES (#{channel_UID},#{userUID},#{authority})")
    void saveChannelUser(@Param("channel_UID") int channel_UID, @Param("userUID") int userUID, @Param("authority") String authority);

    @Update("Update channel set channel_icon_url = #{imgURL} where channel_uid = #{channel_UID}")
    void updateChannelIcon(@Param("imgURL") String imgURL, @Param("channel_UID") int channel_UID);

    @Select("select u.channel_uid, u.user_uid, c.channel_title, c.channel_icon_url, c.channel_type " +
            " from channel_user u left join channel c on u.channel_uid = c.channel_uid " +
            " where u.user_uid = #{userUID} order by channel_user_join_date desc limit 1")
    MyChannelsDTO findLastChannelByUserUID(@Param("userUID") int userUID);

    @Select("SELECT * FROM channel WHERE channel_invite_code = #{inviteCode} and channel_invite_code_activate = true")
    Optional<Channel> findChannelByInviteCode(@Param("inviteCode") String inviteCode);

    @Select("SELECT * FROM channel_user WHERE user_uid = #{userUID} and channel_uid = #{channelUID}")
    Optional<ChannelMember> findChannelUserByUserUID(@Param("userUID") int userUID, @Param("channelUID") int channelUID);

    @Update("UPDATE channel SET channel_invite_code = #{randomCode}, channel_invite_code_activate = true WHERE channel_uid = #{channelUID}")
    void updateInViteCode(@Param("channelUID") int channelUID,@Param("randomCode") String randomCode);

    @Select("SELECT channel_invite_code from channel where channel_uid = #{channelUID} and channel_invite_code_activate = true")
    Optional<String> findInviteCodeByChannelUID(@Param("channelUID") int channelUID);

    @Delete("delete FROM channel_user WHERE channel_uid = #{channelUID} and user_uid = #{userUID}")
    void deleteChannelMember(@Param("channelUID") int channelUID,@Param("userUID") int userUID);

    @Select("SELECT * FROM channel_user WHERE channel_uid = #{channelUID}")
    Optional<ChannelMember> findChannelMemberByChannelUID(@Param("channelUID") int channelUID);

    @Delete("delete from channel WHERE channel_uid = #{channelUID}")
    void deleteChannel(@Param("channelUID") int channelUID);
}
