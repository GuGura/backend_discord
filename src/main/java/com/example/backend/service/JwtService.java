package com.example.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.backend.mapper.TokenMapper;
import com.example.backend.model.JwtToken;
import com.example.backend.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

//   private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;

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

        Timestamp accessTokenExpires_Date = new Timestamp(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);
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
        Timestamp refreshTokenExpires_Date = new Timestamp(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME);
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

        Timestamp create_Date = new Timestamp(System.currentTimeMillis());
        Timestamp accessJwtExpires = (Timestamp) accessJwtMap.get(JwtProperties.EXPIRED_DATE);
        Timestamp refreshJwtExpires = (Timestamp) refreshJwtMap.get(JwtProperties.EXPIRED_DATE);

        return JwtToken.builder()
                .username(username)
                .accessJwt(accessJwt)
                .refreshJwt(refreshJwt)
                .create_date(create_Date)
                .accessJwtExpires_date(accessJwtExpires)
                .refreshJwtExpires_date(refreshJwtExpires)
                .build();
    }

    public void saveToken(JwtToken jwtToken) {
        tokenMapper.saveToken(jwtToken);
    }

//    public String findTokenType(HttpServletRequest request) {
//        Iterator<String> iterator = request.getHeaderNames().asIterator();
//        while (iterator.hasNext()) {
//            String index = iterator.next();
//            if (index.contains("jwt")) {
//                return index.replace("jwt", "Jwt");
//            }
//        }
//        throw new BadCredentialsException("토큰을 찾을 수 없습니다.");
//    }
//
//    public String getTokenFromHeader(HttpServletRequest request, String tokenType) {
//        String token = checkTokenPrefix(request.getHeader(tokenType)) ? request.getHeader(tokenType) : null;
//        if (token == null)
//            throw new BadCredentialsException("Bear 토큰이 아닙니다.");
//        return token;
//    }
//
//    private boolean checkTokenPrefix(String token) {
//        return token.startsWith(JwtProperties.TOKEN_PREFIX);
//    }
//
//    public DecodedJWT decodedJWT(String jwtToken) {
//        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
//                .build()
//                .verify(jwtToken);
//    }
//
//    public <T> T getClaim(DecodedJWT token, String key, Class<T> valueType) {
//        Map<String, Claim> claims = token.getClaims();
//        Claim claim = claims.get(key);
//        if (claim != null) {
//            if (valueType == String.class)
//                return valueType.cast(claim.asString());
//            else if (valueType == Integer.class)
//                return valueType.cast(claim.asInt());
//            else if (valueType == Date.class)
//                return valueType.cast(claim.asDate());
//        }
//        System.out.println("getClaim: 매개변수를 다시 확인해주세요");
//        return null; //Exception 핸들링해도될듯?
//    }
//
//    public User findUserByEmail(String email) {
//        return memberMapper.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + "::유저를 찾을 수 없습니다."));
//    }
//    public void updateToken(Token token){
//        tokenMapper.updateToken(token);
//    }
//    public ResultDTO getResult(boolean status, String message){
//        return ResultDTO.builder()
//                .status(status)
//                .message(message)
//                .build();
//    }

}
