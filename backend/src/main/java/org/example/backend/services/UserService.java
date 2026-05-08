package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.dtos.UpdateUserDto;
import org.example.backend.dtos.UpdateUserPasswordDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.EmailIsAlreadyTakenException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.exceptions.UserUpdateException;
import org.example.backend.mappers.UserMapper;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.RoleRepository;
import org.example.backend.repositories.UserRepository;
import org.example.backend.utils.SecurityUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final ExampleMatcher EXAMPLE_MATCHER = ExampleMatcher.matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnorePaths("emailVerificationCodes", "authorities", "emailVerified", "lastLogin", "createdAt", "updatedAt");

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
    }

    public User getCurrentUser() {
        return this.getUserById(SecurityUtils.getCurrentUserId());
    }

    public UserDto getUserDtoById(String id) {
        User user = this.getUserById(id);
        return userMapper.mapToUserDto(user);
    }

    public List<UserDto> getAllUsers(User user, Pageable pageable) {
        Page<User> users = userRepository.findAll(Example.of(user, EXAMPLE_MATCHER), pageable);
        return users.stream().map(userMapper::mapToUserDto).toList();
    }

    public User createUser(RegistrationDto registrationDto) {
        User user = userRepository.findByEmail(registrationDto.getEmail()).orElseGet(User::new);

        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyTakenException();
        }

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setEmail(registrationDto.getEmail());
        user.setPendingEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        user.setNickname(registrationDto.getNickname());
        user.setGivenName(registrationDto.getGivenName());
        user.setFamilyName(registrationDto.getFamilyName());

        user.getRoles().add(roleRepository.getDefaultASUserRole());

        return userRepository.save(user);
    }

    public User createOAuth2User(User newUser, String registrationId) {
        User user = userRepository.findByEmail(newUser.getEmail())
                .map(existing -> {
                            if (!existing.isEmailVerified()) {
                                return userMapper.mergeUsers(newUser, existing);
                            }
                            return existing;
                        }
                )
                .orElse(newUser);

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setEmailVerified(true);
        user.setClientRegistrationId(registrationId);

        user.getRoles().add(roleRepository.getDefaultASUserRole());

        return userRepository.save(user);
    }

    public User updateUser(String id, UpdateUserDto updateUserDto) {
        User user = this.getUserById(id);

        user.setNickname(updateUserDto.getNickname());
        user.setGivenName(updateUserDto.getGivenName());
        user.setFamilyName(updateUserDto.getFamilyName());

        return userRepository.save(user);
    }

    public User updatePassword(String userId, UpdateUserPasswordDto updateUserPasswordDto) {
        User user = this.getUserById(userId);

        String oldPassword = updateUserPasswordDto.getOldPassword();
        String newPassword = updateUserPasswordDto.getNewPassword();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserUpdateException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        return userRepository.save(user);
    }

    public User updateEmail(User user) {
        String newEmail = user.getPendingEmail();

        userRepository.findByEmail(newEmail).ifPresent(existingUser -> {
            if (existingUser.isEmailVerified()) {
                throw new EmailIsAlreadyTakenException();
            } else if (user != existingUser) {
                userRepository.delete(existingUser);
                userRepository.flush();
            }
        });

        user.setEmail(newEmail);
        user.setEmailVerified(true);
        user.setPendingEmail(null);

        return userRepository.save(user);
    }
}
