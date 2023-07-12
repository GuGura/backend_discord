package com.example.backend.mapper;

import com.example.backend.model.UserDTO;
import com.example.backend.model.entity.Friend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Select("SELECT ur.nickname, ur.icon_url " +
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

//    @Select("SELECT ID, USERNAME, USER_ICON_URL " +
//            "FROM member " +
//            "WHERE USERNAME LIKE '%' #{username} '%' " +
//            "  AND ID NOT IN (SELECT FRIEND_RECEIVER " +
//            "                 FROM friend " +
//            "                 WHERE FRIEND_SENDER = #{memberUID} ) " +
//            "AND ID != #{memberUID} ")
//    List<FriendDTO2> findSearchUsers(@Param("username") String username, @Param("memberUID") int memberUID);
//
//    @Insert("INSERT INTO friend (FRIEND_SENDER,FRIEND_RECEIVER,FRIEND_CHECKED) values (#{memberUID},#{sendMemberUID},true)")
//    void saveRequest(@Param("memberUID") int memberUID, @Param("sendMemberUID") int sendMemberUID);
//
//    @Select("SELECT count(*) FROM friend where FRIEND_SENDER=#{memberUID} and FRIEND_RECEIVER=#{sendMemberUID} and FRIEND_CHECKED = true")
//    int findData(@Param("memberUID") int memberUID, @Param("sendMemberUID") int sendMemberUID);
//
//    @Insert("INSERT INTO friend (FRIEND_SENDER,FRIEND_RECEIVER) values (#{sendMemberUID},#{memberUID})")
//    void saveResponse(@Param("sendMemberUID") int sendMemberUID, @Param("memberUID") int memberUID);
//
//
//    @Select("SELECT ID, USERNAME, USER_ICON_URL\n" +
//            "FROM member\n" +
//            "WHERE ID IN\n" +
//            "      (SELECT FRIEND_RECEIVER\n" +
//            "       FROM friend\n" +
//            "       WHERE FRIEND_SENDER = #{memberUID} \n" +
//            "         and FRIEND_CHECKED = false)")
//    List<FriendDTO2> fineRequestUsers(@Param("memberUID") int memberUID);
//
//    @Update("update friend set FRIEND_CHECKED = true where FRIEND_SENDER = #{memberUID} and FRIEND_RECEIVER = #{friendUID}")
//    void updateFriend(@Param("memberUID") int memberUID,@Param("friendUID") int friendUID);
}
