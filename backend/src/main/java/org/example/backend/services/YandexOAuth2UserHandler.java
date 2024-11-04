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
import static org.example.backend.models.OAuth2UserAttributes.DEFAULT_EMAIL;
import static org.example.backend.models.OAuth2UserAttributes.FIRST_NAME;
import static org.example.backend.models.OAuth2UserAttributes.LAST_NAME;
import static org.example.backend.models.OAuth2UserAttributes.LOGIN;
import static org.example.backend.models.enums.OAuth2ProviderType.YANDEX;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;

@Service
public class YandexOAuth2UserHandler implements OAuth2UserHandler {

    @Override
    public User getUser(OAuth2UserRequest request, OAuth2User idpUser, IdpRegistration idpRegistration) {
        Timestamp timestampNow = getCurrentTimestamp();

        return User.builder()
                .id(UUID.randomUUID().toString())
                .idpRegistration(idpRegistration)
                .email(idpUser.getAttribute(DEFAULT_EMAIL))
                .emailVerified(TRUE)
                .name(idpUser.getAttribute(LOGIN))
                .givenName(idpUser.getAttribute(FIRST_NAME))
                .familyName(idpUser.getAttribute(LAST_NAME))
                .lastLogin(timestampNow)
                .createdAt(timestampNow)
                .updatedAt(timestampNow)
                .build();
    }

    @Override
    public OAuth2ProviderType getHandlerType() {
        return YANDEX;
    }
}
