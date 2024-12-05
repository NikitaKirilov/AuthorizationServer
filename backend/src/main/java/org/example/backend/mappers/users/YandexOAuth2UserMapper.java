package org.example.backend.mappers.users;

import org.example.backend.models.entities.ClientRegistrationWrapper;
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
public class YandexOAuth2UserMapper implements OAuth2UserMapper {

    @Override
    public User mapToUser(OAuth2UserRequest request, OAuth2User oAuth2User, ClientRegistrationWrapper wrapper) {
        Timestamp timestampNow = getCurrentTimestamp();

        return new User()
                .setId(UUID.randomUUID().toString())

                .setClientRegistrationWrapper(wrapper)

                .setEmail(oAuth2User.getAttribute(DEFAULT_EMAIL))
                .setEmailVerified(TRUE)

                .setName(oAuth2User.getAttribute(LOGIN))
                .setGivenName(oAuth2User.getAttribute(FIRST_NAME))
                .setFamilyName(oAuth2User.getAttribute(LAST_NAME))

                .setLastLogin(timestampNow)
                .setCreatedAt(timestampNow)
                .setUpdatedAt(timestampNow);
    }

    @Override
    public OAuth2ProviderType getProviderType() {
        return YANDEX;
    }
}
