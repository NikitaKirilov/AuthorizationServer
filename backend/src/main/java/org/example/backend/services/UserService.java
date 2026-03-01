package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.exceptions.RegistrationException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.mappers.UserMapper;
import org.example.backend.mappers.idp.OAuth2UserMappers;
import org.example.backend.models.UserPrincipal;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthorityService authorityService;
    private final OAuth2UserMappers oAuth2UserMappers;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextService securityContextService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
    }

    public User getUserFromContext() {
        UserPrincipal userPrincipal = securityContextService.getPrincipal();
        return this.getById(userPrincipal.getId());
    }

    public User registerUser(HttpServletRequest request, HttpServletResponse response, RegistrationDto registrationDto) {
        User user = userRepository.findByEmail(registrationDto.getEmail()).orElseGet(User::new);
        if (user.isEmailVerified()) {
            throw new RegistrationException("Registration failed");
        }

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        user.setNickname(registrationDto.getNickname());
        user.setGivenName(registrationDto.getGivenName());
        user.setFamilyName(registrationDto.getFamilyName());

        userRepository.save(user);
        securityContextService.updateSecurityContext(request, response, user);

        return user;
    }

    @Transactional
    public void loginUser(HttpServletRequest request, HttpServletResponse response, UserDetails userDetails) {
        User user = this.getByEmail(userDetails.getUsername());
        //TODO: add device service
        user.setLastLogin(Instant.now());
        securityContextService.updateSecurityContext(request, response, user);
    }

    public User saveOrUpdateOAuth2User(OAuth2User oAuth2User, String registrationId) {
        User mappedUser = oAuth2UserMappers.delegate(oAuth2User, registrationId);
        User user = userRepository.findByEmail(mappedUser.getEmail()).map(existing -> {
            if (!existing.isEmailVerified()) {
                userMapper.mergeUsers(mappedUser, existing);
                existing.getAuthorities().add(authorityService.getDefaultAuthority());
            }
            existing.setClientRegistrationId(registrationId);
            existing.setLastLogin(Instant.now());
            return existing;
        }).orElseGet(() -> {
            mappedUser.setId(UUID.randomUUID().toString());
            mappedUser.setClientRegistrationId(registrationId);
            mappedUser.getAuthorities().add(authorityService.getDefaultAuthority());
            mappedUser.setLastLogin(Instant.now());
            return mappedUser;
        });

        return userRepository.save(user);
    }

    public void activateUser(HttpServletRequest request, HttpServletResponse response, User user) {
        if (user.isEmailVerified()) {
            throw new RegistrationException("Registration failed");
        }

        user.setEmailVerified(true);
        user.getAuthorities().add(authorityService.getDefaultAuthority());
        user.setLastLogin(Instant.now());

        securityContextService.updateSecurityContext(request, response, user);
    }
}
