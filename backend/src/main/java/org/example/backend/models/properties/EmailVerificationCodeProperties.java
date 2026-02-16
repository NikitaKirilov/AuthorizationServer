package org.example.backend.models.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email.code")
@Getter
@Setter
public class EmailVerificationCodeProperties {

    private int expirationSeconds = 360;
    private int codeLength = 6;
    private int maxAttempts = 5;
}
