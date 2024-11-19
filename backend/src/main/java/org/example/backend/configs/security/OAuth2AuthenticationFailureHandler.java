package org.example.backend.configs.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.exceptions.IdpRegistrationNotFoundException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Throwable cause = exception.getCause();
        if (cause instanceof IdpRegistrationNotFoundException idpRegistrationNotFoundException) {
            response.sendError(SC_NOT_FOUND, idpRegistrationNotFoundException.getMessage());
            return;
        }

        response.sendError(SC_INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    public static ObjectPostProcessor<OAuth2AuthorizationRequestRedirectFilter> getPostProcessor() {
        return new ObjectPostProcessor<>() {
            @Override
            public <O extends OAuth2AuthorizationRequestRedirectFilter> O postProcess(O object) {
                OAuth2AuthenticationFailureHandler failureHandler = new OAuth2AuthenticationFailureHandler();
                object.setAuthenticationFailureHandler(failureHandler);

                return object;
            }
        };
    }
}
