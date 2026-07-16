package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.exceptions.EmailIsAlreadyTakenException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.User;
import org.example.backend.models.properties.AdminProperties;
import org.example.backend.repositories.UserRepository;
import org.example.backend.utils.SecurityUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AdminProperties adminProperties;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
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

    public User getUserWithAuthoritiesById(String id) {
        return userRepository.findWithRolesAndAuthoritiesById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    public User getCurrentUser() {
        return getUserById(SecurityUtils.getCurrentUserId());
    }

    public User getCurrentUserWithAuthorities() {
        return getUserWithAuthoritiesById(SecurityUtils.getCurrentUserId());
    }

    public Page<User> getAllUsers(User probe, ExampleMatcher matcher, Pageable pageable) {
        return userRepository.findAll(Example.of(probe, matcher), pageable);
    }

    public User createUser(RegistrationDto registrationDto) {
        User user = userRepository.findByEmail(registrationDto.getEmail())
                .orElseGet(User::new);

        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyTakenException();
        }

        if (user.isNew()) {
            user.setId(UUID.randomUUID().toString());
            user.getRoles().add(roleService.getASUserRole());
        }

        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setNickname(registrationDto.getNickname());
        user.setGivenName(registrationDto.getGivenName());
        user.setFamilyName(registrationDto.getFamilyName());
        user.setBirthday(registrationDto.getBirthday());

        return save(user);
    }

    @Transactional
    public void initializeAdmin() {
        User user = userRepository.findBySuperuserTrue().orElseGet(User::new);

        if (user.isNew()) {
            user.setId(UUID.randomUUID().toString());
            user.setSuperuser(true);
            user.setEmailVerified(true);
            user.getRoles().add(roleService.getASAdminRole());
        }

        user.setEmail(adminProperties.getUsername());
        user.setPassword(passwordEncoder.encode(adminProperties.getPassword()));

        save(user);
    }

    public User saveFederatedIdpUser(User federatedIdpUser, String registrationId) {
        User user = userRepository.findByEmail(federatedIdpUser.getEmail()).orElseGet(User::new);

        if (user.isNew()) {
            user.setId(UUID.randomUUID().toString());
            user.getRoles().add(roleService.getASUserRole());
        }

        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            userMapper.mergeUsers(federatedIdpUser, user);
        }

        if (!Objects.equals(user.getClientRegistrationId(), registrationId)) {
            user.setClientRegistrationId(registrationId);
        }

        return save(user);
    }

    public User confirmEmail(User user) {
        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyTakenException();
        }

        user.setEmailVerified(true);

        return save(user);
    }

    public User save(User user) {
        //TODO: add logging
        return userRepository.save(user);
    }

    public void delete(User user) {
        //TODO: add logging
        userRepository.delete(user);
    }
}
