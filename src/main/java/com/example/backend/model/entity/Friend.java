package com.example.backend.model.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Friend {
    private int friend_sender;
    private int friend_receiver;
    private boolean friend_checked;
    private Timestamp friend_date;
};
