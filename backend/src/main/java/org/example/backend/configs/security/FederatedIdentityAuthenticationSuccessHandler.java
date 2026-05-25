package org.example.backend.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.services.UserAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FederatedIdentityAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserAuthService userAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            authentication = userAuthService.loginUserWithFederatedIdentity(
                    request, response, token.getPrincipal(), token.getAuthorizedClientRegistrationId()
            );
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
