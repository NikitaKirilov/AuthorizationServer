package org.example.backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ActionCooldownException extends RuntimeException {

    private static final String MESSAGE = "Action {%s} is in cooldown";
    private final long cooldown;

    public ActionCooldownException(String action, long cooldown) {
        super(MESSAGE.formatted(action));
        this.cooldown = cooldown;
    }
}
