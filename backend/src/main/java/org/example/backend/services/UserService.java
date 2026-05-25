package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.dtos.UserUpdateDto;
import org.example.backend.exceptions.EmailIsAlreadyTakenException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.exceptions.UserPasswordUpdateException;
import org.example.backend.mappers.mapstruct.UserMapper;
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
import java.util.Objects;
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
        return getUserById(SecurityUtils.getCurrentUserId());
    }

    public UserDto getUserDtoById(String id) {
        User user = this.getUserById(id);
        return userMapper.mapEntityToDto(user);
    }

    public List<UserDto> getAllUsers(User user, Pageable pageable) {
        Page<User> users = userRepository.findAll(Example.of(user, EXAMPLE_MATCHER), pageable);
        return users.stream().map(userMapper::mapEntityToDto).toList();
    }

    public User createUser(RegistrationDto registrationDto) {
        User user = userRepository.findByEmail(registrationDto.getEmail())
                .orElseGet(User::new);

        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyTakenException();
        }

        if (user.isNew()) {
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

    public User saveFederatedIdpUser(User federatedIdpUser, String registrationId) {
        User user = userRepository.findByEmail(federatedIdpUser.getEmail()).orElseGet(User::new);

        if (user.isNew()) {
            user.setId(UUID.randomUUID().toString());
            user.getRoles().add(roleRepository.getDefaultASUserRole());
        }

        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            userMapper.mergeUsers(federatedIdpUser, user);
        }

        if (!Objects.equals(user.getClientRegistrationId(), registrationId)) {
            user.setClientRegistrationId(registrationId);
        }

        return userRepository.save(user);
    }

    public User updateUser(UserUpdateDto userUpdateDto) {
        User user = getCurrentUser();
        user.setNickname(userUpdateDto.getNickname());
        user.setGivenName(userUpdateDto.getGivenName());
        user.setFamilyName(userUpdateDto.getFamilyName());
        user.setBirthday(userUpdateDto.getBirthday());

        return userRepository.save(user);
    }

    public User updatePassword(UserPasswordUpdateDto userPasswordUpdateDto) {
        User user = getCurrentUser();
        String oldPassword = userPasswordUpdateDto.getOldPassword();
        String newPassword = userPasswordUpdateDto.getNewPassword();

        if (Objects.equals(oldPassword, newPassword)) {
            throw new UserPasswordUpdateException("Passwords must be different");
        }

        if (user.getPassword() != null && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserPasswordUpdateException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        return userRepository.save(user);
    }

    public User verifyEmail(User user) {
        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyTakenException();
        }

        user.setEmailVerified(true);

        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
