package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.AuthException;
import org.example.backend.models.GithubEmailResponse;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.example.backend.models.OAuth2UserAttributes.LOGIN;
import static org.example.backend.models.enums.OAuth2ProviderType.GITHUB;
import static org.example.backend.utils.RequestUtils.createOAuth2EmailRequest;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;

@Service
@RequiredArgsConstructor
public class GithubOAuth2UserHandler implements OAuth2UserHandler {

    private static final ParameterizedTypeReference<List<GithubEmailResponse>> GITHUB_EMAIL_LIST_TYPE = new ParameterizedTypeReference<>() {
    };

    private static final String GITHUB_EMAIL_URI = "https://api.github.com/user/emails";

    private final RestOperations restOperations;

    @Override
    public User getUser(OAuth2UserRequest request, OAuth2User oAuth2User, IdpRegistration idpRegistration) {
        GithubEmailResponse githubEmailResponse = fetchEmail(request);
        String email = githubEmailResponse.getEmail();
        boolean emailVerified = githubEmailResponse.isVerified();
        Timestamp timestampNow = getCurrentTimestamp();

        return User.builder()
                .id(UUID.randomUUID().toString())
                .idpRegistration(idpRegistration)
                .email(email)
                .emailVerified(emailVerified)
                .name(oAuth2User.getAttribute(LOGIN))
                .givenName(oAuth2User.getAttribute(GIVEN_NAME))
                .lastLogin(timestampNow)
                .createdAt(timestampNow)
                .updatedAt(timestampNow)
                .build();
    }

    @Override
    public OAuth2ProviderType getHandlerType() {
        return GITHUB;
    }

    private GithubEmailResponse fetchEmail(OAuth2UserRequest userRequest) {
        RequestEntity<?> request = createOAuth2EmailRequest(userRequest, GITHUB_EMAIL_URI);
        ResponseEntity<List<GithubEmailResponse>> response = restOperations.exchange(request, GITHUB_EMAIL_LIST_TYPE);
        List<GithubEmailResponse> emails = response.getBody();

        if (emails == null) {
            throw new AuthException("No email found");
        }

        return emails.stream()
                .filter(GithubEmailResponse::isPrimary)
                .findFirst()
                .orElseThrow(() -> new AuthException("Primary email not found"));
    }
}
