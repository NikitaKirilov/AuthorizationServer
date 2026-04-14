package org.example.backend.models;

import lombok.Getter;

@Getter
public enum CooldownAction {

    REQUEST_CODE("request_code", 60L);

    private final String name;
    private final long cooldown;

    CooldownAction(String name, long cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }
}
