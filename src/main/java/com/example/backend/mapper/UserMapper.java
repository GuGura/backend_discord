package com.example.backend.mapper;

import com.example.backend.model.User;
import com.example.backend.model.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (username,password) VALUES (#{username},#{password})")
    void save(UserDTO userDTO);

    @Insert("INSERT INTO user_resource (user_id,nickname) VALUES(#{user_id},#{nickname})")
    void saveResource(@Param("user_id") int user_id, @Param("nickname") String nickname);

    @Select("SELECT * FROM user WHERE username = #{username}")
    Optional<UserDTO> findUserResource(@Param("username") String username);

    @Select("SELECT * FROM user WHERE username = #{username}")
    Optional<User> findUserByUsernameO(@Param("username") String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    Optional<User> findUserByUserUIDO(@Param("id") int id);

}