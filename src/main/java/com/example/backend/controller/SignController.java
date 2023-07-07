package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final SignService signService;
    private final BCryptPasswordEncoder encoder;
    @PostMapping("/signUp")
    public ResponseEntity<?> singUp(@RequestBody User user){
        String bCryptPassword = encoder.encode(user.getPassword());
        user.setPassword(bCryptPassword);
        signService.SignUp(user);
        return new ResponseEntity<>("SIGNUP SUCCESS", HttpStatus.CREATED);
    }
}
