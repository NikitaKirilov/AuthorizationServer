package org.example.backend.configs.oauth2;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.AuthException;
import org.example.backend.models.DefaultUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL_VERIFIED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.FAMILY_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NAME;
import static org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimNames.SUB;

@Component
@RequiredArgsConstructor
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final String AUTHENTICATION_PRINCIPAL_KEY = "org.springframework.security.core.Authentication.PRINCIPAL";
    private static final String ROLES = "roles";

    @Override
    public void customize(JwtEncodingContext context) {
        Authentication authentication = context.get(AUTHENTICATION_PRINCIPAL_KEY);
        if (authentication == null) {
            throw new AuthException("Authentication object is null");
        }

        DefaultUserDetails userDetails = this.getUserDetails(authentication);
        JwtClaimsSet.Builder claimsBuilder = context.getClaims();
        if (context.getAuthorizedScopes().contains(PROFILE)) {
            claimsBuilder.claims(consumer -> {
                consumer.computeIfAbsent(NAME, val -> userDetails.getName());
                consumer.computeIfAbsent(GIVEN_NAME, val -> userDetails.getGivenName());
                consumer.computeIfAbsent(FAMILY_NAME, val -> userDetails.getFamilyName());
            });
        }

        if (context.getAuthorizedScopes().contains(EMAIL)) {
            claimsBuilder.claim(EMAIL, userDetails.getEmail());
            claimsBuilder.claim(EMAIL_VERIFIED, userDetails.isEmailVerified());
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claimsBuilder.claim(ROLES, roles);
        claimsBuilder.claim(SUB, userDetails.getId());
    }

    private DefaultUserDetails getUserDetails(Authentication authentication) {
        if (authentication.getPrincipal() instanceof DefaultUserDetails userDetails) {
            return userDetails;
        }

        throw new AuthException("Unsupported authentication principal: " + authentication);
    }
}
