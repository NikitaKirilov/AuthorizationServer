package org.example.backend.mappers.registrations;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import static org.example.backend.models.TokenClaimNames.ID;
import static org.example.backend.models.enums.OAuth2ProviderType.YANDEX;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

@Component
public class YandexOAuth2RegistrationMapper extends OAuth2RegistrationMapper {

    @Override
    public ClientRegistration mapToClientRegistration(ClientRegistrationWrapper wrapper) {
        return getBuilder(wrapper.getRegistrationId(), CLIENT_SECRET_BASIC)
                .clientId(wrapper.getClientId())
                .clientSecret(wrapper.getClientSecret())
                .clientName(wrapper.getClientName())

                .authorizationUri("https://oauth.yandex.ru/authorize")
                .tokenUri("https://oauth.yandex.ru/token")
                .userInfoUri("https://login.yandex.ru/info")

                .userNameAttributeName(ID)
                .build();
    }

    @Override
    public OAuth2ProviderType getProviderType() {
        return YANDEX;
    }
}
