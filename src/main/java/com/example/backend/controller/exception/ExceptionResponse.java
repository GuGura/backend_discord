package com.example.backend.controller.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
@Builder
public class ExceptionResponse {
    private final String timestamp;
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String details;

    public static ResponseEntity<ExceptionResponse> toResponseEntity(ErrorType errorType, WebRequest request){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String currentTimestampToString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
        return ResponseEntity
                .status(errorType.getStatus())
                .body(ExceptionResponse.builder()
                        .timestamp(currentTimestampToString)
                        .status(errorType.getStatus().value())
                        .error(errorType.getStatus().name())
                        .code(errorType.name())
                        .message(errorType.getMessage())
                        .details(request.getDescription(false))
                        .build());
    }
}
