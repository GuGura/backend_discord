package com.example.backend.mapper;

import com.example.backend.model.UserDTO;
import com.example.backend.model.entity.Friend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Select("SELECT ur.nickname, ur.icon_url, ur.user_id " +
            "FROM friend f1 \n" +
            "         JOIN user_resource ur ON f1.friend_receiver = ur.user_id \n" +
            "WHERE f1.friend_sender = #{userUID} \n" +
            "  AND f1.friend_checked = true \n" +
            "  AND EXISTS ( \n" +
            "    SELECT #{userUID} \n" +
            "    FROM friend f2\n" +
            "    WHERE f2.friend_sender = f1.friend_receiver\n" +
            "      AND f2.friend_receiver = f1.friend_sender\n" +
            "      AND f2.friend_checked = true \n" +
            ")")
    List<UserDTO> findMyFriendByUserUID(@Param("userUID") int userUID);

    @Select("SELECT user_id ,nickname, icon_url " +
            "FROM user_resource " +
            "WHERE nickname LIKE '%' #{nickname} '%' " +
            "  AND user_id NOT IN (SELECT friend_receiver " +
            "                 FROM friend " +
            "                 WHERE friend_sender = #{userUID} ) " +
            "AND user_id != #{userUID} ")
    List<UserDTO> findSearchUsers(@Param("nickname") String nickname, @Param("userUID") int userUID);

    @Insert("INSERT INTO friend (friend_sender,friend_receiver,friend_checked) values (#{userUID},#{sendUserUID},true)")
    void saveRequest(@Param("userUID") int userUID, @Param("sendUserUID") int sendUserUID);

    @Select("SELECT count(*) FROM friend where friend_sender=#{userUID} and friend_receiver=#{sendUserUID} and friend_checked = true")
    int findData(@Param("userUID") int userUID, @Param("sendUserUID") int sendUserUID);

    @Insert("INSERT INTO friend (friend_sender,friend_receiver) values (#{sendUserUID},#{userUID})")
    void saveResponse(@Param("userUID") int userUID, @Param("sendUserUID") int sendUserUID);

    @Select("SELECT user_id, nickname, icon_url " +
            "FROM user_resource " +
            "WHERE user_id IN " +
            "      (SELECT friend_receiver " +
            "       FROM friend " +
            "       WHERE friend_sender = #{userUID} \n" +
            "         and friend_checked = false)")
    List<UserDTO> fineRequestUsers(@Param("userUID") int userUID);

    @Update("update friend set friend_checked = true where friend_sender = #{userUID} and friend_receiver = #{friendUID}")
    void updateFriend(@Param("userUID") int userUID,@Param("friendUID") int friendUID);
}
