package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.dtos.UserUpdateDto;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.User;
import org.example.backend.utils.SecurityUtils;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    private final UserMapper userMapper;
    private final UserService userService;
    private final SessionService sessionService;
    private final AuthorizationService authorizationService;

    public UserDto getCurrentUserDto() {
        return userMapper.mapEntityToDto(userService.getCurrentUser());
    }

    @Transactional
    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(userUpdateDto);
        sessionService.updateUserSessions(user);
        authorizationService.updateUserAuthorizations(user);
        return userMapper.mapEntityToDto(user);
    }

    @Transactional
    public UserDto updatePassword(UserPasswordUpdateDto userPasswordUpdateDto) {
        User user = userService.updatePassword(userPasswordUpdateDto);
        return userMapper.mapEntityToDto(user);
    }

    @Transactional
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getCurrentUser();
        sessionService.closeUserSessions(user);
        authorizationService.deleteAllByUserId(user.getId());
        userService.deleteUser(user);
        logoutHandler.logout(request, response, SecurityUtils.getAuthenticatedUserToken());
    }
}
