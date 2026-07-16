package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.AuthException;
import org.example.backend.mappers.OAuth2AuthorizationMapper;
import org.example.backend.models.entities.Authority;
import org.example.backend.models.entities.Authorization;
import org.example.backend.models.entities.User;
import org.example.backend.utils.UserUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.BIRTHDATE;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL_VERIFIED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.FAMILY_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NICKNAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.UPDATED_AT;
import static org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimNames.SUB;

@Service
@RequiredArgsConstructor
public class JwtCustomizerService {

    private static final String AUTHORITIES = "authorities";

    private final OAuth2AuthorizationMapper oAuth2AuthorizationMapper;
    private final UserService userService;

    public void customize(JwtEncodingContext context) {
        Objects.requireNonNull(context.getAuthorization(), "OAuth2Authorization is null");

        Authorization authorization = oAuth2AuthorizationMapper.toEntity(context.getAuthorization());
        User user = userService.getUserWithAuthoritiesById(authorization.getUserId());

        checkUserStatus(user);

        JwtClaimsSet.Builder claimsBuilder = context.getClaims();
        if (context.getAuthorizedScopes().contains(PROFILE)) {
            claimsBuilder.claim(NICKNAME, user.getNickname());
            claimsBuilder.claim(GIVEN_NAME, user.getGivenName());
            claimsBuilder.claim(FAMILY_NAME, user.getFamilyName());
            claimsBuilder.claim(BIRTHDATE, user.getBirthday().toString());
            claimsBuilder.claim(UPDATED_AT, user.getUpdatedAt().getEpochSecond());
        }

        if (context.getAuthorizedScopes().contains(EMAIL)) {
            claimsBuilder.claim(EMAIL, user.getEmail());
            claimsBuilder.claim(EMAIL_VERIFIED, user.isEmailVerified());
        }

        Map<String, Set<String>> allAuthorities = UserUtils.getAuthorities(user).stream()
                .collect(Collectors.groupingBy(
                        Authority::getResource,
                        Collectors.mapping(Authority::getName, Collectors.toSet())
                ));

        claimsBuilder.claim(AUTHORITIES, allAuthorities);
        claimsBuilder.claim(SUB, user.getId());
    }

    private void checkUserStatus(User user) {
        if (user.isBlocked()) {
            throw new AuthException("User is blocked");
        }

        if (!user.isEmailVerified()) {
            throw new AuthException("User's email is not verified");
        }
    }
}
