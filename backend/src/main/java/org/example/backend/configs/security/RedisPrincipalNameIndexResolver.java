package org.example.backend.configs.security;

import org.example.backend.models.UserPrincipal;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.Session;
import org.springframework.session.SingleIndexResolver;
import org.springframework.stereotype.Component;

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

        SecurityContext context = session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (context != null) {
            return getPrincipalNameFromAuthentication(context);
        }

        return null;
    }

    private String getPrincipalNameFromAuthentication(SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getName();
        } //TODO: remove

        return EXPRESSION.getValue(authentication, String.class);
    }
}
