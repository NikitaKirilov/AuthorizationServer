package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.dtos.UserUpdateDto;
import org.example.backend.mappers.UserMapper;
import org.example.backend.models.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserMapper userMapper;
    private final UserService userService;
    private final SessionService sessionService;

    public UserDto getCurrentUserDto() {
        return userMapper.mapToUserDto(userService.getCurrentUser());
    }

    @Transactional
    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(userUpdateDto);
        sessionService.refreshUserSessions(user);
        return userMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto updatePassword(UserPasswordUpdateDto userPasswordUpdateDto) {
        User user = userService.updatePassword(userPasswordUpdateDto);
        return userMapper.mapToUserDto(user);
    }
}
