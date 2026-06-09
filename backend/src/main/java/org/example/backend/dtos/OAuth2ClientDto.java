package org.example.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Set;

@Data
public class OAuth2ClientDto {

    @NotBlank(message = "Client Id не должен быть пустым")
    private String clientId;

    @NotBlank(message = "Client Name не должен быть пустым")
    private String clientName;
    private String clientDescription;

    @NotNull(message = "Scopes не должен быть пустым")
    private Set<String> scopes;

    private Set<String> redirectUris;
    private Set<String> postLogoutRedirectUris;

    private String jwkSetUrl;

    private boolean requireProofKey;
    private boolean requireAuthorizationConsent;
    private boolean reuseRefreshTokens;

    @NotNull(message = "Access Token Time To Live не должен быть пустым")
    private Duration accessTokenTimeToLive;
    @NotNull(message = "Refresh Token Time To Live не должен быть пустым")
    private Duration refreshTokenTimeToLive;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
