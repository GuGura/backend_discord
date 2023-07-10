package com.example.backend.controller.status;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum SuccessType {
    /* 200	OK:	요청을 정상적으로 처리함 */
    /* 201	Created : 성공적으로 생성에 대한 요청을 받었으며 서버가 새 리소스를 작성함 (대개 POST, PUT일 때) */
    SIGNUP_SUCCESS(HttpStatus.CREATED,"회원가입 성공"),
    MAIL_SEND_SUCCESS(HttpStatus.CREATED,"메일 전송 성공"),
    MAIL_AUTHENTICATE_CODE_SUCCESS(HttpStatus.CREATED,"인증번호가 일치합니다."),
    REFRESH_TOKEN_SUCCESS_GET(HttpStatus.CREATED,"JWT 재발급완료"),
    ;

    private final HttpStatus status;
    private final String message;

}
