package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.ForbiddenOperationException;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.Role;
import org.example.backend.models.entities.User;
import org.example.backend.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final AuthorizationService authorizationService;
    private final RoleService roleService;
    private final SessionService sessionService;
    private final UserMapper userMapper;
    private final UserService userService;

    @Transactional
    public UserDto updateUser(String id, UserDto userDto) {
        User user = userService.getUserById(id);
        user.setEmailVerified(userDto.isEmailVerified());
        user.setNickname(userDto.getNickname());
        user.setGivenName(userDto.getGivenName());
        user.setFamilyName(userDto.getFamilyName());
        user.setBirthday(userDto.getBirthday());
        user.setBlocked(userDto.isBlocked());

        return userMapper.mapEntityToDto(userService.save(user));
    }

    @Transactional
    public void assignRole(String userId, String roleId) {
        User user = userService.getUserById(userId);
        Role role = roleService.getRoleById(roleId);
        user.getRoles().add(role);
        userService.save(user);
    }

    @Transactional
    public void revokeRole(String userId, String roleId) {
        User user = userService.getUserById(userId);
        Role role = roleService.getRoleById(roleId);
        user.getRoles().remove(role);
        userService.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        String currentId = SecurityUtils.getCurrentUserId();
        if (currentId.equals(userId)) {
            throw new ForbiddenOperationException("You can't delete yourself through admin endpoint");
        }

        User user = userService.getUserById(userId);
        sessionService.closeUserSessions(user);
        authorizationService.deleteAllByUserId(user.getId());
        userService.delete(user);
    }
}
