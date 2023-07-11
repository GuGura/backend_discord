package com.example.backend.service;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.mapper.EmailMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.User;
import com.example.backend.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final BCryptPasswordEncoder encoder;

    public void SignUp(UserDTO userDTO){
        boolean userExist = findUserByUsername(userDTO.getUsername()).isPresent();
        if (userExist) throw new CustomException(ErrorType.USER_ALREADY_EXISTS);
        String bCryptPassword = encoder.encode(userDTO.getPassword());
        userDTO.setPassword(bCryptPassword);
        userResourceSave(userDTO);
    }

    private void userResourceSave(UserDTO userDTO){
        emailMapper.findAuth(userDTO.getUsername(), userDTO.getEmailAuthCode()).orElseThrow(()->new CustomException(ErrorType.MAIL_AUTHENTICATE_CODE_FAIL));
        userMapper.save(userDTO);
        userMapper.findUserByUsernameO(userDTO.getUsername()).ifPresent(user->{
            userMapper.saveResource(user.getId(),userDTO.getNickname());
        });
    }

    public Optional<User> findUserByUsername(String username){
        return userMapper.findUserByUsernameO(username);
    }
    public Optional<User> findUserByUID(int userUID){
        return userMapper.findUserByUserUIDO(userUID);
    }
    public boolean matchesPassword(String input, String db){
        return encoder.matches(input, db);
    }
}
