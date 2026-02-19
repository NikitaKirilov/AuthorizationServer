package org.example.backend.models;

import lombok.Getter;

@Getter
public enum Action {

    CODE_REQUEST("email_code_request", 60L);

    private final String name;
    private final Long cooldown;

    Action(String name, Long cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }
}
