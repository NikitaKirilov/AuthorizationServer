package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.models.security.AuthenticatedUserToken;
import org.example.backend.models.security.UserDeviceInfo;
import org.example.backend.models.security.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContextService {

    private final SecurityContextRepository securityContextRepository;

    public Authentication createAuthenticatedUserContext(
            HttpServletRequest request,
            HttpServletResponse response,
            User user,
            UserDevice userDevice
    ) {
        UserPrincipal principal = new UserPrincipal(user);
        UserDeviceInfo userDeviceInfo = new UserDeviceInfo(userDevice);
        Object details = new WebAuthenticationDetails(request);

        AbstractAuthenticationToken authentication = new AuthenticatedUserToken(principal, userDeviceInfo);
        authentication.setDetails(details);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        securityContextRepository.saveContext(context, request, response);

        return authentication;
    }

    public void createRegisteredUserContext(
            HttpServletRequest request,
            HttpServletResponse response,
            User user
    ) {
        UserPrincipal principal = new UserPrincipal(user);
        Object details = new WebAuthenticationDetails(request);

        AbstractAuthenticationToken authentication = new AuthenticatedUserToken(principal, null);
        authentication.setDetails(details);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        securityContextRepository.saveContext(context, request, response);
    }
}
