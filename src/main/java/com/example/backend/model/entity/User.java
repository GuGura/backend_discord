package com.example.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private int id;
    private String username;
    private String password;
    private String role;
    private Timestamp join_date;
    private String state;
    private Timestamp connect_date;
    private String nickname;
    private int channel_uid;
}
