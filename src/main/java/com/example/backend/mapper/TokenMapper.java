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
    void saveToken(JwtToken jwtToken);

    @Select("SELECT * FROM token where EMAIL = #{email}")
    Optional<JwtToken> findTokenByEmail(JwtToken jwtToken);

    @Update("UPDATE token SET ACCESS_TOKEN = #{accessToken}, REFRESH_TOKEN = #{refreshToken},CREATE_DATE = #{create_Date},ACCESS_TOKEN_EXPIRES_DATE=#{accessTokenExpires_Date} ,REFRESH_TOKEN_EXPIRES_DATE = #{refreshTokenExpires_Date} WHERE EMAIL = #{email}")
    void updateToken(JwtToken jwtToken);

    @Select("SELECT * FROM token where EMAIL = #{email} and REFRESH_TOKEN = #{refreshToken} and REFRESH_TOKEN_EXPIRES_DATE = #{refreshTokenExpires_Date}")
    Optional<JwtToken> findTokenByALL(JwtToken jwtToken);
}
