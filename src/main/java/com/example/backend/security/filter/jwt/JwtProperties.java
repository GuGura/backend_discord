package com.example.backend.security.filter.jwt;

public interface JwtProperties {

    int ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 12;
    int REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    //60000*10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_ACCESS = "accessJwt";
    String HEADER_REFRESH = "refreshJwt";
    String EXPIRED_DATE = "Expires_Date";
    String TOKEN_STATUS = "status";
    String TOKEN_STATUS_EXPIRED = "TokenExpiredException";
    String TOKEN_STATUS_NOT_BEAR = "isNotBearToken";

    String TOKEN_STATUS_OK = "PASS";
}
