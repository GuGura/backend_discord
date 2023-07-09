package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    private int id;
    private String username;
    private String accessJwt;
    private String refreshJwt;
    private Timestamp create_date;
    private Timestamp accessJwtExpires_date;
    private Timestamp refreshJwtExpires_date;

    @Builder
    public JwtToken(String username, String accessJwt, String refreshJwt, Timestamp create_date, Timestamp accessJwtExpires_date, Timestamp refreshJwtExpires_date) {
        this.username = username;
        this.accessJwt = accessJwt;
        this.refreshJwt = refreshJwt;
        this.create_date = create_date;
        this.accessJwtExpires_date = accessJwtExpires_date;
        this.refreshJwtExpires_date = refreshJwtExpires_date;
    }
}
