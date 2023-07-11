package com.example.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.mapper.TokenMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.JwtToken;
import com.example.backend.model.User;
import com.example.backend.security.filter.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

//   private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final UserMapper userMapper;
    /**
     * @return (" accessJwt ", jwt) <br> ("Expires_Date", accessTokenExpires_Date)
     */
    public Map<String, Object> createAccessToken(int id, String username, String userRole) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HMAC512");
        headerMap.put("type", "jwt");

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("username", username);
        payload.put("user_role", userRole);

        Date accessTokenExpires_Date = new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);
        String jwt = JWT.create()
                .withSubject("Login")
                .withHeader(headerMap)
                .withPayload(payload)
                .withExpiresAt(accessTokenExpires_Date)
                .sign(Algorithm.HMAC512(secretKey));

        Map<String, Object> list = new HashMap<>();
        list.put(JwtProperties.HEADER_ACCESS, jwt);
        list.put(JwtProperties.EXPIRED_DATE, accessTokenExpires_Date);
        return list;
    }

    /**
     * @return (" refreshJwt ", jwt) <br> ("Expires_Date", refreshTokenExpires_Date)
     */
    public Map<String, Object> createRefreshToken(String username, String userRole) {
        Date refreshTokenExpires_Date = new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME);
        String jwt = JWT.create()
                .withClaim("username", username)
                .withClaim("user_role", userRole)
                .withExpiresAt(refreshTokenExpires_Date)
                .sign(Algorithm.HMAC512(secretKey));
        Map<String, Object> list = new HashMap<>();
        list.put(JwtProperties.HEADER_REFRESH, jwt);
        list.put(JwtProperties.EXPIRED_DATE, refreshTokenExpires_Date);
        return list;
    }

    public JwtToken createTokenDto(String username, Map<String, Object> accessJwtMap, Map<String, Object> refreshJwtMap) {
        String accessJwt = (String) accessJwtMap.get(JwtProperties.HEADER_ACCESS);
        String refreshJwt = (String) refreshJwtMap.get(JwtProperties.HEADER_REFRESH);

        Date create_Date = new Date(System.currentTimeMillis());
        Date accessJwtExpires = (Date) accessJwtMap.get(JwtProperties.EXPIRED_DATE);
        Date refreshJwtExpires = (Date) refreshJwtMap.get(JwtProperties.EXPIRED_DATE);

        return JwtToken.builder()
                .username(username)
                .accessJwt(accessJwt)
                .refreshJwt(refreshJwt)
                .create_date(create_Date)
                .accessJwtExpires_date(accessJwtExpires)
                .refreshJwtExpires_date(refreshJwtExpires)
                .build();
    }

    public void saveJwt(JwtToken jwtToken) {
        boolean exist = tokenMapper.findJwtByUsername(jwtToken).isPresent();
        if (exist) updateToken(jwtToken);
        else tokenMapper.saveJwt(jwtToken);
    }

    public String findTokenType(HttpServletRequest request) throws CustomException{
        Iterator<String> iterator = request.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            String index = iterator.next();
            if (index.contains("jwt")) return index.replace("jwt", "Jwt");
        }
        return null;
    }

    public String getTokenFromHeader(HttpServletRequest request, String tokenType) throws CustomException{
        String token = checkTokenPrefix(request.getHeader(tokenType)) ? request.getHeader(tokenType) : null;
        if (token == null)
            throw new CustomException(ErrorType.TOKEN_NOT_BEARER);
        return token.replace(JwtProperties.TOKEN_PREFIX, "");
    }

    private boolean checkTokenPrefix(String token) {
        return token.startsWith(JwtProperties.TOKEN_PREFIX);
    }

    public DecodedJWT decodedJWT(String jwtToken) throws CustomException{
        try{
            return JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(jwtToken);
        }catch (Exception e){
            throw new CustomException(ErrorType.TOKEN_EXPIRED);
        }
    }

    public <T> T getClaim(DecodedJWT token, String key, Class<T> valueType) {
        Map<String, Claim> claims = token.getClaims();
        Claim claim = claims.get(key);
        if (claim != null) {
            if (valueType == String.class)
                return valueType.cast(claim.asString());
            else if (valueType == Integer.class)
                return valueType.cast(claim.asInt());
            else if (valueType == Date.class)
                return valueType.cast(claim.asDate());
        }
        throw new CustomException(ErrorType.TOKEN_NOT_EXIST_USERNAME);
    }

    public User findUserByUsername(String username) throws CustomException{
        return userMapper.findUserByUsernameO(username).orElseThrow(() -> new CustomException(ErrorType.TOKEN_NOT_FOUND));
    }
    public void updateToken(JwtToken jwtToken){
        tokenMapper.updateJwt(jwtToken);
    }
//    public ResultDTO getResult(boolean status, String message){
//        return ResultDTO.builder()
//                .status(status)
//                .message(message)
//                .build();
//    }

}
