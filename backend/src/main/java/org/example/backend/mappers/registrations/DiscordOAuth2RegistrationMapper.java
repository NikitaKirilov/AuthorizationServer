package org.example.backend.mappers.registrations;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import static org.example.backend.models.TokenClaimNames.ID;
import static org.example.backend.models.enums.OAuth2ProviderType.DISCORD;
import static org.springframework.security.oauth2.core.AuthenticationMethod.HEADER;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.EMAIL;

@Component
public class DiscordOAuth2RegistrationMapper extends OAuth2RegistrationMapper {

    @Override
    public ClientRegistration mapToClientRegistration(ClientRegistrationWrapper wrapper) {
        return getBuilder(wrapper.getRegistrationId(), CLIENT_SECRET_BASIC)
                .clientId(wrapper.getClientId())
                .clientSecret(wrapper.getClientSecret())
                .clientName(wrapper.getClientName())

                .scope("identify", EMAIL)

                .authorizationUri("https://discord.com/oauth2/authorize")
                .tokenUri("https://discord.com/api/oauth2/token")
                .userInfoUri("https://discordapp.com/api/users/@me")

                .userInfoAuthenticationMethod(HEADER)
                .userNameAttributeName(ID)
                .build();
    }

    @Override
    public OAuth2ProviderType getProviderType() {
        return DISCORD;
    }
}
