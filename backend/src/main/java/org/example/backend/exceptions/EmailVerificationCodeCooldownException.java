package org.example.backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailVerificationCodeCooldownException extends RuntimeException {

    private static final String MESSAGE = "Token is in cooldown";
    private final Instant nextTokenAt;

    public EmailVerificationCodeCooldownException(Instant nextTokenAt) {
        super(MESSAGE);
        this.nextTokenAt = nextTokenAt;
    }
}
