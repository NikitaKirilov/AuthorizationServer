package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.AuthException;
import org.example.backend.mappers.UserMapper;
import org.example.backend.mappers.idp.OAuth2UserMappers;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final EmailVerificationCodeService emailVerificationCodeService;
    private final OAuth2UserMappers oAuth2UserMappers;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SecurityContextService securityContextService;
    private final AuthorityService authorityService;

    @Transactional
    public void loginUser(HttpServletRequest request, HttpServletResponse response, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AuthException("User not found"));
        //TODO: add device service

        user.setLastLogin(Instant.now());
        user.setClientRegistrationId(null);

        securityContextService.updateSecurityContext(request, response, user);
    }

    @Transactional
    public User loginUserWithOAuth2(OAuth2User oAuth2User, String registrationId) {
        User mappedUser = oAuth2UserMappers.delegate(oAuth2User, registrationId);
        User user = userRepository.findByEmail(mappedUser.getEmail())
                .map(existing -> userMapper.mergeUsers(mappedUser, existing))
                .orElse(mappedUser);

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setEmailVerified(true);
        user.setLastLogin(Instant.now());
        user.setClientRegistrationId(registrationId);

        authorityService.assignDefaultAuthority(user);
        emailVerificationCodeService.deactivateActiveCode(user);

        return userRepository.save(user);
    }
}
