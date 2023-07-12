package com.example.backend.model;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.sql.Timestamp;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String role;
    private Timestamp join_date;

    private String nickname;
    private String icon_url;
    private String description;

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
