package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Entity
@Getter
@Setter
public class OAuth2Client {

    @Id
    private String id;

    private String clientId;
    private String clientSecret;

    private String clientName;
    private String clientDescription;

    private String scopes;

    private String redirectUris;
    private String postLogoutRedirectUris;

    private String jwkSetUrl;

    private boolean requireProofKey;
    private boolean requireAuthorizationConsent;
    private boolean reuseRefreshTokens;

    private Duration accessTokenTimeToLive;
    private Duration refreshTokenTimeToLive;
}
