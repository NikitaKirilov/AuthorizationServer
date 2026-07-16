package org.example.backend.configs.oauth2;

import lombok.RequiredArgsConstructor;
import org.example.backend.services.JwtCustomizerService;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final JwtCustomizerService jwtCustomizerService;

    @Override
    public void customize(JwtEncodingContext context) {
        jwtCustomizerService.customize(context);
    }
}
