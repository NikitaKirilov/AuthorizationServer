package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserAuthorityDto;
import org.example.backend.dtos.UserDetailsDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.dtos.UserRoleDto;
import org.example.backend.exceptions.UserPasswordUpdateException;
import org.example.backend.mappers.mapstruct.AuthorityMapper;
import org.example.backend.mappers.mapstruct.RoleMapper;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.Role;
import org.example.backend.models.entities.User;
import org.example.backend.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private final AuthorityMapper authorityMapper;
    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;
    private final SessionService sessionService;
    private final UserMapper userMapper;
    private final UserService userService;

    public UserDetailsDto getCurrentUserDetailsDto() {
        User user = userService.getCurrentUserWithAuthorities();

        UserDto userDto = userMapper.mapEntityToDto(user);
        Set<UserRoleDto> roleDtos = user.getRoles().stream()
                .map(roleMapper::mapToUserRoleDto)
                .collect(Collectors.toSet());
        Set<UserAuthorityDto> authorityDtos = user.getRoles().stream()
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .map(authorityMapper::mapToUserAuthorityDto)
                .collect(Collectors.toSet());

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setUser(userDto);
        userDetailsDto.setRoles(roleDtos);
        userDetailsDto.setAuthorities(authorityDtos);

        return userDetailsDto;
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User user = userService.getCurrentUser();
        user.setNickname(userDto.getNickname());
        user.setGivenName(userDto.getGivenName());
        user.setFamilyName(userDto.getFamilyName());
        user.setBirthday(userDto.getBirthday());

        userService.save(user);

        return userMapper.mapEntityToDto(user);
    }

    @Transactional
    public void updatePassword(UserPasswordUpdateDto userPasswordUpdateDto) {
        User user = userService.getCurrentUser();
        String oldPassword = userPasswordUpdateDto.getOldPassword();
        String newPassword = userPasswordUpdateDto.getNewPassword();

        if (Objects.equals(oldPassword, newPassword)) {
            throw new UserPasswordUpdateException("Passwords must be different");
        }

        if (user.getPassword() != null && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserPasswordUpdateException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
    }

    @Transactional
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getCurrentUser();
        sessionService.deleteUserSessions(user);
        authorizationService.deleteAllByUserId(user.getId());
        userService.delete(user);
        logoutHandler.logout(request, response, SecurityUtils.getCurrentAuthenticatedUserToken());
    }
}
