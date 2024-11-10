package org.example.backend.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.exceptions.idpregistrations.IdpRegistrationNotFoundException;
import org.springframework.security.core.AuthenticationException;
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
}
