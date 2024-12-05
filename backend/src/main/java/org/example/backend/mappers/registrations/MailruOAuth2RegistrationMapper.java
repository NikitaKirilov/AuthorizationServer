package org.example.backend.mappers.registrations;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import static org.example.backend.models.TokenClaimNames.ID;
import static org.example.backend.models.enums.OAuth2ProviderType.MAILRU;
import static org.springframework.security.oauth2.core.AuthenticationMethod.QUERY;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

@Component
public class MailruOAuth2RegistrationMapper extends OAuth2RegistrationMapper {

    @Override
    public ClientRegistration mapToClientRegistration(ClientRegistrationWrapper wrapper) {
        return getBuilder(wrapper.getRegistrationId(), CLIENT_SECRET_BASIC)
                .clientId(wrapper.getClientId())
                .clientSecret(wrapper.getClientSecret())
                .clientName(wrapper.getClientName())

                .scope("userinfo")

                .authorizationUri("https://oauth.mail.ru/login")
                .tokenUri("https://oauth.mail.ru/token")
                .userInfoUri("https://oauth.mail.ru/userinfo")

                .userInfoAuthenticationMethod(QUERY)
                .userNameAttributeName(ID)
                .build();
    }

    @Override
    public OAuth2ProviderType getProviderType() {
        return MAILRU;
    }
}
