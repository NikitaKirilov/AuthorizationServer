package org.example.backend.models;

import lombok.Getter;

@Getter
public enum CooldownAction {

    NEW_CODE_REQUEST("new_code_request", 60L);

    private final String name;
    private final long cooldown;

    CooldownAction(String name, long cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }
}
