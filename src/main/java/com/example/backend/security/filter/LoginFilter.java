package com.example.backend.security.filter;

import com.example.backend.controller.exception.ErrorType;
import com.example.backend.model.JwtToken;
import com.example.backend.model.User;
import com.example.backend.properties.JwtProperties;
import com.example.backend.security.PrincipalDetails;
import com.example.backend.service.JwtService;
import com.example.backend.service.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SignService signService;
    private final JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);

            Optional<User> result = signService.findUserByUsername(user.getUsername());
            if (result.isEmpty()) throw new UsernameNotFoundException(ErrorType.USER_NOT_FOUND.getMessage());
            result.ifPresent(userDTO -> {
                boolean isMatch = signService.matchesPassword(user.getPassword(), userDTO.getPassword());
                if (!isMatch) throw new BadCredentialsException(ErrorType.USER_PASSWORD_WRONG.getMessage());
            });

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        ObjectMapper om = new ObjectMapper();
        ObjectNode responseJson = om.createObjectNode();
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        User user = principalDetails.getUser();
        System.out.println(user);
        Map<String, Object> accessJwtMap = jwtService.createAccessToken(user.getId(), user.getUsername(), user.getRole());
        Map<String, Object> refreshJwtMap = jwtService.createRefreshToken(user.getUsername(), user.getRole());

        JwtToken jwtToken = jwtService.createTokenDto(user.getUsername(), accessJwtMap, refreshJwtMap);
        //삭제 로직을 넣거나 업데이트 시켜야함
        jwtService.saveToken(jwtToken);

        response.addHeader(JwtProperties.HEADER_ACCESS, JwtProperties.TOKEN_PREFIX + jwtToken.getAccessJwt());
        response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + jwtToken.getRefreshJwt());

        response.setStatus(HttpStatus.CREATED.value());
        responseJson.put("message", "LOGIN SUCCESS");
        response.getWriter().write(om.writeValueAsString(responseJson));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String currentTimestampToString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
        Map<String, Object> exceptionInfo = ErrorType.findErrorTypeByMessage(failed.getMessage());

        ObjectMapper om = new ObjectMapper();
        ObjectNode responseJson = om.createObjectNode();
        responseJson.put("timestamp", currentTimestampToString);
        responseJson.put("status", (int) exceptionInfo.get("status"));
        responseJson.put("error", (String) exceptionInfo.get("error"));
        responseJson.put("code", ((ErrorType) exceptionInfo.get("code")).name());
        responseJson.put("message", failed.getMessage());
        responseJson.put("details", request.getRequestURI());

        String formattedJson = om.writer().withDefaultPrettyPrinter().writeValueAsString(responseJson);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // JSON 형식으로 설정
        response.setStatus((int) exceptionInfo.get("status"));
        response.getWriter().write(formattedJson);
    }

}
