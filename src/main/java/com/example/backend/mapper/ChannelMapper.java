package com.example.backend.mapper;


import com.example.backend.model.Channel;
import com.example.backend.model.ChannelMember;
import com.example.backend.model.MyChannelsDTO;
import org.apache.ibatis.annotations.*;

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

//    @Delete("delete FROM channel_user WHERE CHANNEL_UID = #{channelUID} and MEMBER_UID = #{memberUID}")
//    void deleteChannelMember(int channelUID, int memberUID);
//
//    @Select("SELECT * FROM channel_user WHERE CHANNEL_UID = #{channelUID}")
//    Optional<ChannelMember> findChannelMemberByChannelUID(int channelUID);
//
//    @Delete("delete from channel WHERE CHANNEL_UID = #{channelUID}")
//    void deleteChannel(int channelUID);
}
//select m.CHANNEL_UID, m.MEMBER_UID, c.CHANNEL_TITLE, c.CHANNEL_ICON_URL
//from channelmember m
//left join channel c on m.CHANNEL_UID = c.CHANNEL_UID
//where m.MEMBER_UID = 3