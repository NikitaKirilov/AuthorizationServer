package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.dtos.UserUpdateDto;
import org.example.backend.exceptions.UserPasswordUpdateException;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.User;
import org.example.backend.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final UserMapper userMapper;
    private final UserService userService;

    @Transactional
    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User user = userService.getCurrentUser();
        user.setNickname(userUpdateDto.getNickname());
        user.setGivenName(userUpdateDto.getGivenName());
        user.setFamilyName(userUpdateDto.getFamilyName());
        user.setBirthday(userUpdateDto.getBirthday());

        sessionService.updateUserSessions(user);
        authorizationService.updateUserAuthorizations(user);

        return userMapper.mapEntityToDto(userService.save(user));
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
        sessionService.closeUserSessions(user);
        authorizationService.deleteAllByUserId(user.getId());
        userService.delete(user);
        logoutHandler.logout(request, response, SecurityUtils.getAuthenticatedUserToken());
    }
}
