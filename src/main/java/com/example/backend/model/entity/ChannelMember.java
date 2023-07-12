package com.example.backend.model.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChannelMember {
    private int channel_uid;
    private int user_uid;
    private String channel_user_authority;
    private Timestamp channel_user_join_date;
}
