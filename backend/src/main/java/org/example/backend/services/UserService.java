package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.RegistrationException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.mappers.oauth2user.OAuth2UserMapper;
import org.example.backend.mappers.oauth2user.OAuth2UserMappers;
import org.example.backend.models.entities.User;
import org.example.backend.models.properties.UserProperties;
import org.example.backend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final int MAX_UNVERIFIED_USER_PER_EMAIL = 5;

    private final AuthorityService authorityService;
    private final OAuth2UserMappers oAuth2UserMappers;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProperties userProperties;

    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    public User registerUser(UserDto userDto) {
        List<User> unverifiedUsers = userRepository.findAllByEmail(userDto.getEmail());
        if (unverifiedUsers.stream().anyMatch(User::isEmailVerified)) {
            throw new RegistrationException("User with such email is already registered");
        }

        if (unverifiedUsers.size() >= MAX_UNVERIFIED_USER_PER_EMAIL) {
            unverifiedUsers.stream()
                    .min(Comparator.comparing(User::getCreatedAt))
                    .ifPresent(userRepository::delete);
        }

        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setName(userDto.getName());
        user.setGivenName(userDto.getGivenName());
        user.setFamilyName(userDto.getFamilyName());

        user.setCreatedAt(Instant.now());

        user.setNextVerificationTokenAt(
                Instant.now().plus(userProperties.getNextVerificationTokenAtSeconds(), ChronoUnit.SECONDS)
        );

        return userRepository.save(user);
    }

    public void saveOrUpdateOAuth2User(OAuth2User oAuth2User, String registrationId) {
        OAuth2UserMapper mapper = oAuth2UserMappers.getMapper(registrationId);
        User mappedUser = mapper.mapOAuth2UserToEntity(oAuth2User);
        Instant now = Instant.now();

        User user = userRepository.findByEmailAndEmailVerifiedTrue(mappedUser.getEmail()).map(existing -> {
            existing.setClientRegistrationId(registrationId);
            existing.setLastLogin(now);
            existing.setUpdatedAt(now);
            return existing;
        }).orElseGet(() -> {
            mappedUser.setId(UUID.randomUUID().toString());
            mappedUser.setClientRegistrationId(registrationId);
            mappedUser.getAuthorities().add(authorityService.getDefaultAuthority());
            mappedUser.setCreatedAt(now);
            mappedUser.setLastLogin(now);
            return mappedUser;
        });

        userRepository.save(user);
    }

    public void activateUser(User user) {
        Instant now = Instant.now();

        user.setEmailVerified(true);
        user.getAuthorities().add(authorityService.getDefaultAuthority());
        user.setLastLogin(now);
        user.setUpdatedAt(now);

        userRepository.deleteAllByEmailAndEmailVerifiedFalse(user.getEmail());
    }

    public void checkAndUpdateNextVerificationTokenAt(User user) {
        Instant now = Instant.now();
        Instant nextTokenAt = user.getNextVerificationTokenAt();
        if (now.isBefore(nextTokenAt)) {
            throw new RegistrationException("Next verification token available in: "
                    + (nextTokenAt.getEpochSecond() - now.getEpochSecond()) + " seconds");
        }

        user.setNextVerificationTokenAt(
                now.plus(userProperties.getNextVerificationTokenAtSeconds(), ChronoUnit.SECONDS)
        );
    }
}
