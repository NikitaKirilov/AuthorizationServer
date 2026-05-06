package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UpdateUserDto;
import org.example.backend.dtos.UpdateUserPasswordDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.EmailVerificationCodeValidationException;
import org.example.backend.exceptions.UserUpdateException;
import org.example.backend.mappers.UserMapper;
import org.example.backend.models.CooldownAction;
import org.example.backend.models.entities.EmailVerificationCode;
import org.example.backend.models.entities.User;
import org.example.backend.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final CooldownService cooldownService;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final SessionService sessionService;

    public UserDto getCurrentUserDto() {
        return userMapper.mapToUserDto(userService.getCurrentUser());
    }

    @Transactional
    public UserDto updateUser(UpdateUserDto updateUserDto) {
        User user = userService.updateUser(SecurityUtils.getCurrentUserId(), updateUserDto);
        sessionService.refreshUserSessions(user);
        return userMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto updatePassword(UpdateUserPasswordDto updateUserPasswordDto) {
        User user = userService.updatePassword(SecurityUtils.getCurrentUserId(), updateUserPasswordDto);
        return userMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto updateEmail(String newEmail) {
        User user = userService.getCurrentUser();

        if (user.getEmail().equals(newEmail)) {
            throw new UserUpdateException("Emails must not be the same");
        }

        if (newEmail.equals(user.getPendingEmail())) {
            throw new UserUpdateException("This email is already waiting for verification");
        }

        user.setPendingEmail(newEmail);
        emailVerificationCodeService.sendCode(user);

        return userMapper.mapToUserDto(user);
    }

    @Transactional
    public void refreshCode() {
        User user = userService.getCurrentUser();
        cooldownService.acquire(CooldownAction.NEW_CODE_REQUEST, user.getId());
        emailVerificationCodeService.sendCode(user);
    }

    @Transactional(noRollbackFor = EmailVerificationCodeValidationException.class)
    public UserDto verifyNewEmail(String sourceCode) {
        User user = userService.getCurrentUser();
        EmailVerificationCode code = emailVerificationCodeService.getActiveByUser(user);

        emailVerificationCodeService.validateCode(sourceCode, code);
        userService.updateEmail(user);
        sessionService.refreshUserSessions(user);

        return userMapper.mapToUserDto(user);
    }
}
