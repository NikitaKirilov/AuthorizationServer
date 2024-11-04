package org.example.backend.services;

import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static org.example.backend.models.OAuth2UserAttributes.USERNAME;
import static org.example.backend.models.OAuth2UserAttributes.VERIFIED;
import static org.example.backend.models.enums.OAuth2ProviderType.DISCORD;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;

@Service
public class DiscordOAuth2UserHandler implements OAuth2UserHandler {

    @Override
    public User getUser(OAuth2UserRequest request, OAuth2User oAuth2User, IdpRegistration idpRegistration) {
        boolean emailVerified = TRUE.equals(oAuth2User.getAttribute(VERIFIED));
        Timestamp timestampNow = getCurrentTimestamp();

        return User.builder()
                .id(UUID.randomUUID().toString())
                .idpRegistration(idpRegistration)
                .email(oAuth2User.getAttribute(EMAIL))
                .emailVerified(emailVerified)
                .name(oAuth2User.getAttribute(USERNAME))
                .lastLogin(timestampNow)
                .createdAt(timestampNow)
                .updatedAt(timestampNow)
                .build();
    }

    @Override
    public OAuth2ProviderType getHandlerType() {
        return DISCORD;
    }
}
