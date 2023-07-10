package com.example.backend.mapper;

import com.example.backend.model.JwtToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface TokenMapper {

    @Insert("INSERT INTO jwt (username,accessJwt,refreshJwt, create_date, accessJwtExpires_date, refreshJwtExpires_date)" +
            "VALUES (#{username} ,#{accessJwt} ,#{refreshJwt} ,#{create_date} ,#{accessJwtExpires_date},#{refreshJwtExpires_date})")
    void saveJwt(JwtToken jwtToken);

    @Select("SELECT * FROM jwt where username = #{username}")
    Optional<JwtToken> findJwtByUsername(JwtToken jwtToken);

    @Update("UPDATE jwt SET accessJwt = #{accessJwt}," +
            " refreshJwt = #{refreshJwt}," +
            "create_date = #{create_date}," +
            "accessJwtExpires_date=#{accessJwtExpires_date} " +
            ",refreshJwtExpires_date = #{refreshJwtExpires_date} WHERE username = #{username}")
    void updateJwt(JwtToken jwtToken);

    @Select("SELECT * FROM jwt where EMAIL = #{email} and REFRESH_TOKEN = #{refreshToken} and REFRESH_TOKEN_EXPIRES_DATE = #{refreshTokenExpires_Date}")
    Optional<JwtToken> findTokenByALL(JwtToken jwtToken);
}
