package org.example.backend.configs;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;

public class OAuth2RedirectFilterPostProcessor implements ObjectPostProcessor<OAuth2AuthorizationRequestRedirectFilter> {
    @Override
    public <O extends OAuth2AuthorizationRequestRedirectFilter> O postProcess(O object) {
        OAuth2AuthenticationFailureHandler failureHandler = new OAuth2AuthenticationFailureHandler();
        object.setAuthenticationFailureHandler(failureHandler);

        return object;
    }
}
