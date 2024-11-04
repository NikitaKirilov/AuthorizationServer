package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static org.example.backend.models.OAuth2UserAttributes.FIRST_NAME;
import static org.example.backend.models.OAuth2UserAttributes.LAST_NAME;
import static org.example.backend.models.enums.OAuth2ProviderType.MAILRU;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NAME;

@Component
@RequiredArgsConstructor
public class MailruOAuth2UserHandler implements OAuth2UserHandler {

    private final UserService userService;

    @Override
    public CustomOAuth2User handleUser(OAuth2UserRequest request, OAuth2User idpUser, IdpRegistration idpRegistration) {
        Timestamp timestampNow = Timestamp.from(Instant.now());

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .idpRegistration(idpRegistration)
                .email(idpUser.getAttribute(EMAIL))
                .emailVerified(TRUE)
                .name(idpUser.getAttribute(NAME))
                .givenName(idpUser.getAttribute(FIRST_NAME))
                .familyName(idpUser.getAttribute(LAST_NAME))
                .lastLogin(timestampNow)
                .createdAt(timestampNow)
                .updatedAt(timestampNow)
                .build();

        return new CustomOAuth2User(idpUser.getAttributes(), userService.saveUserOnIdpLogin(user));
    }

    @Override
    public OAuth2ProviderType getHandlerType() {
        return MAILRU;
    }
}
