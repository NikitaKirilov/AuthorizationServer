package org.example.backend.configs.security;

import org.example.backend.models.CustomUserDetails;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.session.Session;
import org.springframework.session.SingleIndexResolver;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

@Component
public class RedisPrincipalNameIndexResolver<S extends Session> extends SingleIndexResolver<S> {
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private static final Expression EXPRESSION = new SpelExpressionParser().parseExpression("authentication?.name");

    public RedisPrincipalNameIndexResolver() {
        super(PRINCIPAL_NAME_INDEX_NAME);
    }

    public String resolveIndexValueFor(S session) {
        String principalName = session.getAttribute(getIndexName());
        if (principalName != null) {
            return principalName;
        }

        Object authentication = session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (authentication != null) {
            return getPrincipalNameFromAuthentication(authentication);
        }

        return null;
    }

    private String getPrincipalNameFromAuthentication(Object authentication) {
        if (Objects.requireNonNull(authentication) instanceof UsernamePasswordAuthenticationToken token) {
            CustomUserDetails userDetails = (CustomUserDetails) token.getPrincipal();
            return userDetails.getName();
        }
        return EXPRESSION.getValue(authentication, String.class);
    }
}
