package com.example.backend.mapper;

import com.example.backend.model.entity.User;
import com.example.backend.model.UserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (username,password) VALUES (#{username},#{password})")
    void save(UserDTO userDTO);

    @Insert("INSERT INTO user_resource (user_id,nickname) VALUES(#{user_id},#{nickname})")
    void saveResource(@Param("user_id") int user_id, @Param("nickname") String nickname);

    @Select("SELECT * FROM user WHERE username = #{username}")
    Optional<UserDTO> findUserResource(@Param("username") String username);
    @Select("SELECT * FROM user_resource WHERE user_id = #{userUID}")
    UserDTO findUserResourceByUserUID(@Param("userUID") int userUID);

    @Select("SELECT * FROM user WHERE username = #{username}")
    Optional<User> findUserByUsernameO(@Param("username") String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    Optional<User> findUserByUserUIDO(@Param("id") int id);

    @Select("select u.username, u.role, u.join_date, ur.nickname, ur.icon_url, ur.description " +
            "from user u " +
            "LEFT JOIN user_resource ur on u.id = ur.user_id " +
            "where u.id = #{userUID}")
    Optional<UserDTO> findUserBasicInfoByUserUID(@Param("userUID") int userUID);

    @Update("Update user_resource SET nickname = #{params.nickname},icon_url = #{params.icon_url} , description = #{params.description} where user_id = #{userUID}")
    void updateUserResource(@Param("params") Map<String, String> params,@Param("userUID") int userUID);

    @Select("SELECT nickname,icon_url,description FROM user_resource WHERE user_id = #{userUID}")
    Map<String,String> findUserResourceByUserUIDM(@Param("userUID") int userUID);

    @Insert("INSERT INTO user_state (username, state, connect_date) VALUES (#{username}, #{state}, #{connect_date}) ON DUPLICATE KEY UPDATE state = #{state}, connect_date = #{connect_date}")
    void insertUserState(User user);

    @Select("SELECT u.username, ur.nickname, us.state, us.connect_date, cu.channel_uid " +
            "FROM user u " +
            "JOIN user_resource ur ON u.id = ur.user_id " +
            "JOIN channel_user cu ON u.id = cu.user_uid " +
            "JOIN user_state us ON u.username = us.username " +
            "WHERE us.connect_date = ( " +
            "SELECT MAX(connect_date) " +
            "FROM user_state " +
            "WHERE username = u.username)")
    List<User> onlineUser();

    @Select("SELECT u.username, ur.nickname, us.state, us.connect_date, cu.channel_uid " +
            "FROM user u " +
            "JOIN user_resource ur ON u.id = ur.user_id " +
            "JOIN channel_user cu ON u.id = cu.user_uid " +
            "JOIN user_state us ON u.username = us.username " +
            "WHERE us.connect_date = ( " +
            "SELECT MAX(connect_date) " +
            "FROM user_state " +
            "WHERE username = u.username) " +
            "AND u.username = #{username}")
    List<User> sendToOnlineUser(String username);


}
