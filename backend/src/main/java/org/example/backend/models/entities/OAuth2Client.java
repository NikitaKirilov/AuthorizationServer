package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.Instant;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
