package com.example.backend.controller;

import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${version}")
public class CheckJwt {

    @PutMapping("/check-refreshToken")
    public ResponseEntity<?> checkRefreshToken() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/check-token")
    public ResponseEntity<?> checkAccessToken() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
