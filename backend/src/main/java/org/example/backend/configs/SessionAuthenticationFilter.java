package org.example.backend.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends GenericFilterBean {

    private final SessionRegistry sessionRegistry;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        String sessionId = request.getSession().getId();
        SessionInformation currentSessionInformation = sessionRegistry.getSessionInformation(sessionId);

        if (currentSessionInformation == null) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            CustomUserDetails contextUserDetails = (CustomUserDetails) authentication.getPrincipal();
            CustomUserDetails sessionUserDetails = (CustomUserDetails) currentSessionInformation.getPrincipal();

            if (!contextUserDetails.equals(sessionUserDetails)) {
                String credentials = sessionUserDetails.getPassword();
                Collection<? extends GrantedAuthority> authorities = sessionUserDetails.getAuthorities();

                Authentication newAuthentication =
                        new UsernamePasswordAuthenticationToken(sessionUserDetails, credentials, authorities);

                context.setAuthentication(newAuthentication);
            }
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User contextUserDetails = (CustomOAuth2User) authentication.getPrincipal();
            CustomOAuth2User sessionUserDetails = (CustomOAuth2User) currentSessionInformation.getPrincipal();

            if (!contextUserDetails.equals(sessionUserDetails)) {
                Collection<? extends GrantedAuthority> authorities = sessionUserDetails.getAuthorities();
                String registrationId = sessionUserDetails.getIdpRegistrationId();

                Authentication newAuthentication =
                        new OAuth2AuthenticationToken(sessionUserDetails, authorities, registrationId);

                context.setAuthentication(newAuthentication);
            }
        }

        chain.doFilter(request, response);
    }
}
