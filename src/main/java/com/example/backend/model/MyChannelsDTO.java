package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MyChannelsDTO {

    private int channel_UID;
    private int user_UID;
    private String channel_title;
    private String channel_icon_url;
    private String channel_type;
    private String channel_invite_code;
}
