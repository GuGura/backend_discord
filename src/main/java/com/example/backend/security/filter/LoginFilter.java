package com.example.backend.security.filter;

import com.example.backend.controller.exception.ErrorType;
import com.example.backend.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Class<? extends AuthenticationException> exceptionType = failed.getClass();
        setFailResponse(request,response, failed.getMessage());
    }

    private void setFailResponse(HttpServletRequest request,HttpServletResponse response, String message) throws IOException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String currentTimestampToString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
        Map<String, Object> exceptionInfo = ErrorType.findErrorTypeByMessage(message);

        ObjectMapper om = new ObjectMapper();
        ObjectNode responseJson = om.createObjectNode();
        responseJson.put("timestamp", currentTimestampToString);
        responseJson.put("status", (int) exceptionInfo.get("status"));
        responseJson.put("error", (String) exceptionInfo.get("error"));
        responseJson.put("code", ((ErrorType) exceptionInfo.get("code")).name());
        responseJson.put("message", message);
        responseJson.put("details",request.getRequestURI());

        String formattedJson = om.writer().withDefaultPrettyPrinter().writeValueAsString(responseJson);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // JSON 형식으로 설정
        response.setStatus((int) exceptionInfo.get("status"));
        response.getWriter().write(formattedJson);
    }

}
