package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.mappers.idp.OAuth2UserMappers;
import org.example.backend.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final OAuth2UserMappers oAuth2UserMappers;
    private final SecurityContextService securityContextService;
    private final UserDeviceService userDeviceService;
    private final UserService userService;

    @Transactional
    public Authentication loginUser(HttpServletRequest request, HttpServletResponse response, UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        userDeviceService.verifyDevice(user, request);
        return securityContextService.createAuthorizedUserContext(request, response, user);
    }

    @Transactional
    public Authentication loginUserWithOAuth2(HttpServletRequest request, HttpServletResponse response, OAuth2User oAuth2User, String registrationId) {
        User mappedUser = oAuth2UserMappers.delegate(oAuth2User, registrationId);
        User user = userService.createOAuth2User(mappedUser, registrationId);
        userDeviceService.verifyDevice(user, request);
        return securityContextService.createAuthorizedUserContext(request, response, user);
    }
}
