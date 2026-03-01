package org.example.backend.models;

import lombok.Getter;

@Getter
public enum CooldownAction {

    CODE_REQUEST("email_code_request", 60L);

    private final String name;
    private final long cooldown;

    CooldownAction(String name, long cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }
}
