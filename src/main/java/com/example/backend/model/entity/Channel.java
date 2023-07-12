package com.example.backend.model.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Channel {
    private int channel_uid;
    private String channel_title;
    private String channel_icon_url;
    private String channel_description;
    private int channel_owner;
    private String channel_invite_code;
    private boolean channel_invite_code_enable;
    private boolean channel_is_open;
    private Timestamp channel_create_date;
    private String channel_type;

}
