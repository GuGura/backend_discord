package com.example.backend.model.entity;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DisconnectEvent extends ApplicationEvent {

    private String username;

    public DisconnectEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}