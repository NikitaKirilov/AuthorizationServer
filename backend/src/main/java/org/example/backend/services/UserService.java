package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserDetailsDto;
import org.example.backend.exceptions.EmailIsAlreadyTakenException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.Authority;
import org.example.backend.models.entities.Role;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.example.backend.utils.SecurityUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final ExampleMatcher EXAMPLE_MATCHER = ExampleMatcher.matchingAny()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnorePaths("roles", "emailVerificationCodes", "userDevices");

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

    public UserDetailsDto getUserDetailsDtoById(String id) {
        User user = userRepository.findWithRolesAndAuthoritiesById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));

        UserDto userDto = userMapper.mapEntityToDto(user);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getFullName)
                .collect(Collectors.toSet());

        Set<String> authorities = user.getRoles().stream()
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .map(Authority::getFullName)
                .collect(Collectors.toSet());

        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setUserDto(userDto);
        userDetails.setRoles(roles);
        userDetails.setAuthorities(authorities);

        return userDetails;
    }

    public User getCurrentUser() {
        return getUserById(SecurityUtils.getCurrentUserId());
    }

    public UserDetailsDto getCurrentUserDetailsDto() {
        return getUserDetailsDtoById(SecurityUtils.getCurrentUserId());
    }

    public Page<UserDto> getAllUsersDto(User probe, Pageable pageable) {
        return userRepository.findAll(Example.of(probe, EXAMPLE_MATCHER), pageable)
                .map(userMapper::mapEntityToDto);
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
        user.getRoles().add(roleService.getASUserRole());

        return save(user);
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
