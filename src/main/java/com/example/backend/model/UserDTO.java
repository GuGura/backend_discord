package com.example.backend.model;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String emailAuthCode;

    public void InputData(String index, String data) {
        try {
            Field field = getClass().getDeclaredField(index);
            field.setAccessible(true);
            field.set(this, data);
        } catch (Exception e) {
            throw new CustomException(ErrorType.USER_INPUT_DATA_FAIL);
        }
    }
}