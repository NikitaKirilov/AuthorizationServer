package org.example.backend.models.enums;

import lombok.Getter;

@Getter
public enum AttemptAction {

    LOGIN("login", 10, 60L);

    private final String name;
    private final int attempts;
    private final long cooldown;

    AttemptAction(String name, int attempts, long cooldown) {
        this.name = name;
        this.attempts = attempts;
        this.cooldown = cooldown;
    }
}
