package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.EmailIsAlreadyVerifiedException;
import org.example.backend.exceptions.RegistrationException;
import org.example.backend.exceptions.TokenCooldownException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.mappers.UserMapper;
import org.example.backend.mappers.idp.OAuth2UserMapper;
import org.example.backend.mappers.idp.OAuth2UserMappers;
import org.example.backend.models.entities.User;
import org.example.backend.models.properties.UserProperties;
import org.example.backend.repositories.UserRepository;
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
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserProperties userProperties;

    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
    }

    public User registerUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseGet(User::new);
        if (user.isEmailVerified()) {
            throw new RegistrationException("Registration failed");
        }

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
            user.setNextVerificationTokenAt(Instant.now());
        }

        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setNickname(userDto.getNickname());
        user.setGivenName(userDto.getGivenName());
        user.setFamilyName(userDto.getFamilyName());

        return userRepository.save(user);
    }

    public User saveOrUpdateOAuth2User(OAuth2User oAuth2User, String registrationId) {
        OAuth2UserMapper mapper = oAuth2UserMappers.getMapper(registrationId);
        User mappedUser = mapper.mapOAuth2UserToEntity(oAuth2User);
        Instant now = Instant.now();

        User user = userRepository.findByEmail(mappedUser.getEmail()).map(existing -> {
            if (!existing.isEmailVerified()) {
                userMapper.mergeUsers(mappedUser, existing);
                existing.getAuthorities().add(authorityService.getDefaultAuthority());
            }
            existing.setClientRegistrationId(registrationId);
            existing.setLastLogin(now);
            return existing;
        }).orElseGet(() -> {
            mappedUser.setId(UUID.randomUUID().toString());
            mappedUser.setClientRegistrationId(registrationId);
            mappedUser.getAuthorities().add(authorityService.getDefaultAuthority());
            mappedUser.setLastLogin(now);
            return mappedUser;
        });

        return userRepository.save(user);
    }

    public void activateUser(User user) {
        if (user.isEmailVerified()) {
            throw new RegistrationException("Registration failed");
        }

        user.setEmailVerified(true);
        user.getAuthorities().add(authorityService.getDefaultAuthority());
        user.setLastLogin(Instant.now());
    }

    public void checkUserCanRequestToken(User user) {
        Instant now = Instant.now();
        Instant nextTokenAt = user.getNextVerificationTokenAt();

        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyVerifiedException();
        }

        if (now.isBefore(nextTokenAt)) {
            throw new TokenCooldownException(nextTokenAt);
        }

        user.setNextVerificationTokenAt(now.plusSeconds(userProperties.getNextVerificationTokenAtSeconds()));
    }
}
