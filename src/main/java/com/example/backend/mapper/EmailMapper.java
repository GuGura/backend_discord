package com.example.backend.mapper;

import com.example.backend.model.AuthenticationCode;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface EmailMapper {

    @Insert("insert into authentication_code values(#{username},#{auth_code} )")
    void save(@Param("username") String username, @Param("auth_code") String auth_code);
    @Update("Update authentication_code set auth_code = #{auth_code} where email = #{username}")
    void update(@Param("username") String username, @Param("auth_code") String auth_code);

    @Select("select * from authentication_code where email = #{username}")
    Optional<AuthenticationCode> findAuthenticationCode(@Param("username") String username);

    @Select("select * from authentication_code where email = #{username} and auth_code=#{emailAuthCode}")
    Optional<AuthenticationCode> findAuth(@Param("username") String username,@Param("emailAuthCode") String emailAuthCode);

}
