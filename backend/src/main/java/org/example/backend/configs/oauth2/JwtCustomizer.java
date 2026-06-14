package org.example.backend.configs.oauth2;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.AuthException;
import org.example.backend.models.security.ResourceBasedGrantedAuthority;
import org.example.backend.models.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL_VERIFIED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.FAMILY_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NICKNAME;
import static org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimNames.SUB;

@Component
@RequiredArgsConstructor
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final String AUTHENTICATION_PRINCIPAL_KEY = "org.springframework.security.core.Authentication.PRINCIPAL";
    private static final String AUTHORITIES = "authorities";

    @Override
    public void customize(JwtEncodingContext context) {
        Authentication authentication = context.get(AUTHENTICATION_PRINCIPAL_KEY);
        if (authentication == null) {
            throw new AuthException("Authentication object is null");
        }

        UserPrincipal userPrincipal = this.getUserPrincipal(authentication);
        JwtClaimsSet.Builder claimsBuilder = context.getClaims();
        if (context.getAuthorizedScopes().contains(PROFILE)) {
            claimsBuilder.claims(consumer -> {
                consumer.computeIfAbsent(NICKNAME, val -> userPrincipal.getNickname());
                consumer.computeIfAbsent(GIVEN_NAME, val -> userPrincipal.getGivenName());
                consumer.computeIfAbsent(FAMILY_NAME, val -> userPrincipal.getFamilyName());
            });
        }

        if (context.getAuthorizedScopes().contains(EMAIL)) {
            claimsBuilder.claim(EMAIL, userPrincipal.getEmail());
            claimsBuilder.claim(EMAIL_VERIFIED, userPrincipal.isEmailVerified());
        }

        Map<String, Set<String>> allAuthorities = userPrincipal.getAuthorities().stream()
                .collect(Collectors.groupingBy(
                        ResourceBasedGrantedAuthority::getResource,
                        Collectors.mapping(ResourceBasedGrantedAuthority::getName, Collectors.toSet())
                ));

        claimsBuilder.claim(AUTHORITIES, allAuthorities);
        claimsBuilder.claim(SUB, userPrincipal.getId());
    }

    private UserPrincipal getUserPrincipal(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal;
        }

        throw new AuthException("Unsupported authentication principal: " + authentication);
    }
}
