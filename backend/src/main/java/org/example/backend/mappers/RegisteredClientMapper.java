package org.example.backend.mappers;

import org.example.backend.models.entities.OAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.function.Consumer;

@Component
public class RegisteredClientMapper {

    private static final Set<AuthorizationGrantType> SUPPORTED_GRANT_TYPES = Set.of(
            AuthorizationGrantType.AUTHORIZATION_CODE,
            AuthorizationGrantType.REFRESH_TOKEN,
            AuthorizationGrantType.TOKEN_EXCHANGE
    );

    private static final Set<ClientAuthenticationMethod> SUPPORTED_CLIENT_AUTH_METHODS = Set.of(
            ClientAuthenticationMethod.CLIENT_SECRET_POST,
            ClientAuthenticationMethod.CLIENT_SECRET_JWT,
            ClientAuthenticationMethod.CLIENT_SECRET_BASIC
    );

    public OAuth2Client mapToEntity(RegisteredClient registeredClient) {
        OAuth2Client client = new OAuth2Client();
        client.setId(registeredClient.getId());
        client.setClientId(registeredClient.getClientId());
        client.setClientSecret(registeredClient.getClientSecret());
        client.setClientName(registeredClient.getClientName());

        TokenSettings tokenSettings = registeredClient.getTokenSettings();
        if (tokenSettings != null) {
            client.setAccessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive());
            client.setRefreshTokenTimeToLive(tokenSettings.getRefreshTokenTimeToLive());
            client.setReuseRefreshTokens(tokenSettings.isReuseRefreshTokens());
        }

        ClientSettings clientSettings = registeredClient.getClientSettings();
        if (clientSettings != null) {
            client.setRequireAuthorizationConsent(clientSettings.isRequireAuthorizationConsent());
            client.setRequireProofKey(clientSettings.isRequireProofKey());
            client.setJwkSetUrl(clientSettings.getJwkSetUrl());
        }

        client.setScopes(
                StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes())
        );
        client.setRedirectUris(
                StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris())
        );
        client.setPostLogoutRedirectUris(
                StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris())
        );

        return client;
    }

    public RegisteredClient mapToObject(OAuth2Client oAuth2Client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(oAuth2Client.getId());
        builder.clientId(oAuth2Client.getClientId());
        builder.clientSecret(oAuth2Client.getClientSecret());
        builder.clientName(oAuth2Client.getClientName());

        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();
        tokenSettingsBuilder.accessTokenTimeToLive(oAuth2Client.getAccessTokenTimeToLive());
        tokenSettingsBuilder.refreshTokenTimeToLive(oAuth2Client.getRefreshTokenTimeToLive());
        tokenSettingsBuilder.reuseRefreshTokens(oAuth2Client.isReuseRefreshTokens());
        builder.tokenSettings(tokenSettingsBuilder.build());

        ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder();
        clientSettingsBuilder.requireAuthorizationConsent(oAuth2Client.isRequireAuthorizationConsent());
        clientSettingsBuilder.requireProofKey(oAuth2Client.isRequireProofKey());
        setIfNotNull(clientSettingsBuilder::jwkSetUrl, oAuth2Client.getJwkSetUrl());
        builder.clientSettings(clientSettingsBuilder.build());

        builder.clientAuthenticationMethods(clientAuthenticationMethods ->
                clientAuthenticationMethods.addAll(SUPPORTED_CLIENT_AUTH_METHODS)
        );
        builder.authorizationGrantTypes((authorizationGrantTypes ->
                authorizationGrantTypes.addAll(SUPPORTED_GRANT_TYPES))
        );
        builder.scopes(scopes ->
                scopes.addAll(StringUtils.commaDelimitedListToSet(oAuth2Client.getScopes()))
        );
        builder.redirectUris(redirectUris ->
                redirectUris.addAll(StringUtils.commaDelimitedListToSet(oAuth2Client.getRedirectUris()))
        );
        builder.postLogoutRedirectUris(postLogoutRedirectUris ->
                postLogoutRedirectUris.addAll(
                        StringUtils.commaDelimitedListToSet(oAuth2Client.getPostLogoutRedirectUris())
                )
        );

        return builder.build();
    }

    private void setIfNotNull(Consumer<String> consumer, String value) {
        if (value != null) {
            consumer.accept(value);
        }
    }
}
