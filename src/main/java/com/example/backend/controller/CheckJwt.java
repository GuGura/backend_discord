package com.example.backend.controller;

import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${version}")
public class CheckJwt {

    @PutMapping("/check-refreshToken")
    public ResponseEntity<?> checkRefreshToken() {
        return SuccessResponse.toResponseEntity(SuccessType.REFRESH_TOKEN_SUCCESS_GET);
    }

    @PutMapping("/check-token")
    public ResponseEntity<?> checkAccessToken() {
        return SuccessResponse.toResponseEntity(SuccessType.REFRESH_TOKEN_SUCCESS_GET);
    }
}
