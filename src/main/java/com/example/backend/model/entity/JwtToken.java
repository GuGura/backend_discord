package com.example.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    private int id;
    private String username;
    private String accessJwt;
    private String refreshJwt;
    private Date create_date;
    private Date accessJwtExpires_date;
    private Date refreshJwtExpires_date;

    @Builder
    public JwtToken(String username, String accessJwt, String refreshJwt, Date create_date, Date accessJwtExpires_date, Date refreshJwtExpires_date) {
        this.username = username;
        this.accessJwt = accessJwt;
        this.refreshJwt = refreshJwt;
        this.create_date = create_date;
        this.accessJwtExpires_date = accessJwtExpires_date;
        this.refreshJwtExpires_date = refreshJwtExpires_date;
    }
}
