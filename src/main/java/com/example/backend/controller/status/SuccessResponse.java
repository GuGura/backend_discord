package com.example.backend.controller.status;

import com.example.backend.controller.exception.ErrorType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
@Builder
public class SuccessResponse {
    private final String timestamp;
    private final int status;
    private final String message;
    private final Object data;

    public static ResponseEntity<SuccessResponse> toResponseEntity(SuccessType successType){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String currentTimestampToString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
        return ResponseEntity
                .status(successType.getStatus())
                .body(SuccessResponse.builder()
                        .timestamp(currentTimestampToString)
                        .status(successType.getStatus().value())
                        .message(successType.getMessage())
                        .build());
    }
    public static ResponseEntity<SuccessResponse> toResponseEntity(Object data,SuccessType successType){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String currentTimestampToString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
        return ResponseEntity
                .status(successType.getStatus())
                .body(SuccessResponse.builder()
                        .data(data)
                        .timestamp(currentTimestampToString)
                        .status(successType.getStatus().value())
                        .message(successType.getMessage())
                        .build());
    }
}
