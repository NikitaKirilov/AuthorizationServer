package org.example.backend.configs.oauth2;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.AuthException;
import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.CustomUserDetails;
import org.example.backend.models.entities.User;
import org.example.backend.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import static org.example.backend.models.TokenClaimNames.RESOURCE_ACCESS;
import static org.example.backend.utils.JwtUtils.getResourceAccessClaim;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL_VERIFIED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.FAMILY_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.UPDATED_AT;
import static org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimNames.SUB;

@Component
@RequiredArgsConstructor
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final UserService userService;

    private static final String AUTHENTICATION_PRINCIPAL_KEY = "org.springframework.security.core.Authentication.PRINCIPAL";

    @Override
    public void customize(JwtEncodingContext context) {
        Authentication authentication = context.get(AUTHENTICATION_PRINCIPAL_KEY);
        User user = getUserFromAuthentication(authentication);

        JwtClaimsSet.Builder claimsBuilder = context.getClaims();

        if (context.getAuthorizedScopes().contains(PROFILE)) {
            claimsBuilder.claims(consumer -> {
                consumer.computeIfAbsent(NAME, val -> user.getName());
                consumer.computeIfAbsent(GIVEN_NAME, val -> user.getGivenName());
                consumer.computeIfAbsent(FAMILY_NAME, val -> user.getFamilyName());
                consumer.put(UPDATED_AT, user.getUpdatedAt());
            });
        }

        if (context.getAuthorizedScopes().contains(EMAIL)) {
            claimsBuilder.claim(EMAIL, user.getEmail());
            claimsBuilder.claim(EMAIL_VERIFIED, user.isEmailVerified());
        }

        claimsBuilder.claim(SUB, user.getId());
        claimsBuilder.claim(RESOURCE_ACCESS, getResourceAccessClaim(user.getScopes()));
    }

    private User getUserFromAuthentication(Authentication authentication) {
        if (authentication != null &&
                authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            return userService.getById(userDetails.getId()); //TODO remove query to db
        } else if (authentication != null &&
                authentication.getPrincipal() instanceof CustomOAuth2User oAuth2User) {

            String userId = oAuth2User.getId();
            return userService.getById(userId); //TODO: remove query to db
        }

        throw new AuthException("Unsupported authentication principal: " + authentication);
    }
}
