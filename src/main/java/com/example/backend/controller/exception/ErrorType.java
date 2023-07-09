package com.example.backend.controller.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ErrorType {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    MAIL_SEND_FAIL(HttpStatus.BAD_REQUEST,"메일발송 실패"),
    USER_INPUT_DATA_FAIL(HttpStatus.BAD_REQUEST,"UserDTO inputData() 에러"),
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    MAIL_AUTHENTICATE_CODE_FAIL(HttpStatus.UNAUTHORIZED,"인증번호가 불일치 합니다."),
    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다."),
    USER_EMPTY(HttpStatus.NOT_FOUND,"입력칸이 비어 있습니다."),
    USER_PASSWORD_WRONG(HttpStatus.NOT_FOUND,"잘못된 패스워드입니다."),
    USER_PASSWORD_EMPTY(HttpStatus.NOT_FOUND,"패스워드가 입력되지 않았습니다."),
    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT,"기존 유저가 존재합니다."),

    ;

    private final HttpStatus status;
    private final String message;


    public static Map<String,Object> findErrorTypeByMessage(String message){
        Map<String,Object> resultError = new LinkedHashMap<>();
        Arrays.stream(ErrorType.values()).forEach(errorType -> {
            if (errorType.message.equals(message)){
                resultError.put("status",errorType.getStatus().value());
                resultError.put("error",errorType.getStatus().name());
                resultError.put("code",errorType);
                resultError.put("message",errorType.getMessage());
            }
        });
        return resultError;

    }
}
