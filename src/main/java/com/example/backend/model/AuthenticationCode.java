package com.example.backend.model;

import lombok.Data;

@Data
public class AuthenticationCode {
    private String email;
    private String auth_code;
}
