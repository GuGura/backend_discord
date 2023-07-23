package com.example.backend.model.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {
    private int id;
    private int post_owner_id;
    private String post_title;
    private String post_content;
    private String upload_date;
    private boolean visible;
    private String post_img_url;
    private int original_writer;
    private boolean is_post_scrapped;
    private boolean is_img_in;
    private int scrapping_id;
    private String userName;
    private String userIcon;
    private String post_owner_name;




}
