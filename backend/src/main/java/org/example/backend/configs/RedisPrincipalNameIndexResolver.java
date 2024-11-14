package org.example.backend.configs;

import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.CustomUserDetails;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.session.IndexResolver;
import org.springframework.session.Session;
import org.springframework.session.SingleIndexResolver;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

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
        switch (authentication) {
            case UsernamePasswordAuthenticationToken token -> {
                CustomUserDetails userDetails = (CustomUserDetails) token.getDetails();
                return userDetails.getId();
            }

            case RememberMeAuthenticationToken token -> {
                CustomUserDetails userDetails = (CustomUserDetails) token.getDetails();
                return userDetails.getId();
            }

            case OAuth2AuthenticationToken token -> {
                CustomOAuth2User oAuth2User = (CustomOAuth2User) token.getPrincipal();
                return oAuth2User.getId();
            }

            default -> {
                return EXPRESSION.getValue(authentication, String.class);
            }
        }
    }

    public static ObjectPostProcessor<RedisIndexedSessionRepository> getPostProcessor() {
        return new ObjectPostProcessor<>() {
            @Override
            public <O extends RedisIndexedSessionRepository> O postProcess(O object) {
                IndexResolver<Session> resolver = new RedisPrincipalNameIndexResolver<>();
                object.setIndexResolver(resolver);
                return object;
            }
        };
    }
}
