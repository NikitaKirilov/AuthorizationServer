package org.example.backend.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.backend.models.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    private static final String REDIRECT_URL_HEADER = "redirect";
    private static final String DEFAULT_TARGET_URL = "/app/user/profile";
    private static final String VERIFICATION_PAGE_URL = "/app/registrations/verify";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null) {
            response.setHeader(REDIRECT_URL_HEADER, DEFAULT_TARGET_URL);
        } else if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal
                && !userPrincipal.isEmailVerified()) {
            response.setHeader(REDIRECT_URL_HEADER, VERIFICATION_PAGE_URL);
        } else {
            response.setHeader(REDIRECT_URL_HEADER, savedRequest.getRedirectUrl());
            requestCache.removeRequest(request, response);
        }

        this.clearAuthenticationAttributes(request);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
