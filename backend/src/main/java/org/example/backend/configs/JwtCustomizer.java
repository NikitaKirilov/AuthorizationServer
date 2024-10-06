package org.example.backend.configs;

import org.example.backend.models.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimNames.SUB;

@Component
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final String AUTHENTICATION_PRINCIPAL_KEY = "org.springframework.security.core.Authentication.PRINCIPAL";

    public static final String NAME = "name";
    public static final String FAMILY_NAME = "family_name";
    public static final String UPDATED_AT = "updated_at";

    public static final String EMAIL = "email";
    public static final String EMAIL_VERIFIED = "email_verified";

    public static final String ROLES = "roles";

    @Override
    public void customize(JwtEncodingContext context) {
        Authentication authentication = context.get(AUTHENTICATION_PRINCIPAL_KEY);
        if (authentication != null &&
                authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

            if (context.getAuthorizedScopes().contains(PROFILE)) {
                context.getClaims()
                        .claim(NAME, userDetails.getName())
                        .claim(FAMILY_NAME, userDetails.getFamilyName())
                        .claim(UPDATED_AT, userDetails.getUpdatedAt());
            }

            if (context.getAuthorizedScopes().contains(OidcScopes.EMAIL)) {
                context.getClaims()
                        .claim(EMAIL, userDetails.getEmail())
                        .claim(EMAIL_VERIFIED, userDetails.isEmailVerified());
            }

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(SimpleGrantedAuthority::getAuthority).toList();

            context.getClaims().claim(ROLES, roles);
            context.getClaims().claim(SUB, userDetails.getId());
        }
    }
}
