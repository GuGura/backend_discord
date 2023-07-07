package com.example.backend.mapper;

import com.example.backend.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface SignMapper {
    @Insert("INSERT INTO user (USERNAME,PASSWORD) VALUES (#{username},#{password})")
    void save(User user);

    @Select("SELECT * FROM user WHERE USERNAME = #{username}")
    Optional<User> findUserByUsername(@Param("username") String username);
}
