package org.example.backend.dtos;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;

@Data
public class AuthorizationDto {

    private String id;
    private String oauth2ClientId;

    private Set<String> authorizedScopes;

    private String clientName;

    private Instant createdAt;
    private Instant updatedAt;

    public AuthorizationDto(String id, String oauth2ClientId, String authorizedScopes, String clientName, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.oauth2ClientId = oauth2ClientId;
        this.authorizedScopes = StringUtils.commaDelimitedListToSet(authorizedScopes);
        this.clientName = clientName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
