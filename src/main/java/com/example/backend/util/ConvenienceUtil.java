package com.example.backend.util;

import com.example.backend.controller.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ConvenienceUtil {

    public static String currentTimestamp(){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
    }
    public static void jwtExceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentTimestampToString = ConvenienceUtil.currentTimestamp();
        Map<String, Object> exceptionInfo = ErrorType.findErrorTypeByMessage(e.getMessage());
        ObjectMapper om = new ObjectMapper();
        ObjectNode responseJson = om.createObjectNode();
        responseJson.put("timestamp", currentTimestampToString);
        responseJson.put("status", (int) exceptionInfo.get("status"));
        responseJson.put("error", (String) exceptionInfo.get("error"));
        responseJson.put("code", ((ErrorType) exceptionInfo.get("code")).name());
        responseJson.put("message", e.getMessage());
        responseJson.put("details", request.getRequestURI());

        String formattedJson = om.writer().withDefaultPrettyPrinter().writeValueAsString(responseJson);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // JSON 형식으로 설정
        response.setStatus((int) exceptionInfo.get("status"));
        response.getWriter().write(formattedJson);
    }
}
