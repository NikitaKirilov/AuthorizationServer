package org.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.Set;

import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.FACEBOOK;
import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.GITHUB;
import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.GOOGLE;
import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.OKTA;

@Data
public class ClientRegistrationDto {
    @JsonProperty("registration_id")
    private String registrationId;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("client_authentication_method")
    private String clientAuthenticationMethod;

    @JsonProperty("authorization_grant_type")
    private String authorizationGrantType;

    @JsonProperty("redirect_uri")
    private String redirectUri;

    private Set<String> scopes;

    @JsonProperty("authorization_uri")
    private String authorizationUri;

    @JsonProperty("token_uri")
    private String tokenUri;

    @JsonProperty("user_info_uri")
    private String userInfoUri;

    @JsonProperty("jwk_set_uri")
    private String jwkSetUri;

    @JsonProperty("issuer_uri")
    private String issuerUri;

    @JsonIgnore
    public ClientRegistration mapToClientRegistration() {
        switch (registrationId) {
            case "google" -> {
                return GOOGLE.getBuilder(registrationId).
                        clientId(clientId).
                        clientSecret(clientSecret).
                        build();
            }
            case "github" -> {
                return GITHUB.getBuilder(registrationId).
                        clientId(clientId).
                        clientSecret(clientSecret).
                        build();
            }
            case "facebook" -> {
                return FACEBOOK.getBuilder(registrationId).
                        clientId(clientId).
                        clientSecret(clientSecret).
                        build();
            }
            case "okta" -> {
                return OKTA.getBuilder(registrationId).
                        clientId(clientId).
                        clientSecret(clientSecret).
                        build();
            }
            default -> {
                return ClientRegistration.withRegistrationId(registrationId)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .clientAuthenticationMethod(new ClientAuthenticationMethod(clientAuthenticationMethod)) //TODO
                        .scope(scopes)
                        .redirectUri(redirectUri)
                        .authorizationUri(issuerUri)
                        .tokenUri(tokenUri)
                        .userInfoUri(userInfoUri)
                        .jwkSetUri(jwkSetUri)
                        .issuerUri(issuerUri)
                        .build();
            }
        }


    }
}
