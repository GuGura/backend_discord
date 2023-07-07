package com.example.backend.service;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ExceptionCode;
import com.example.backend.mapper.SignMapper;
import com.example.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService {

    private final SignMapper signMapper;

    public void SignUp(User user){
        boolean userExist = signMapper.findUserByUsername(user.getUsername()).isPresent();
        if (userExist)
            throw new CustomException(ExceptionCode.USER_ALREADY_EXISTS);
        signMapper.save(user);
    }
}
