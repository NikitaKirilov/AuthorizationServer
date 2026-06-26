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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        checkAdminNotModifyingHimself(id);

        User user = userService.getUserById(id);
        user.setEmailVerified(userDto.isEmailVerified());
        user.setNickname(userDto.getNickname());
        user.setGivenName(userDto.getGivenName());
        user.setFamilyName(userDto.getFamilyName());
        user.setBirthday(userDto.getBirthday());
        user.setBlocked(userDto.isBlocked());

        authorizationService.updateUserAuthorizations(user);
        sessionService.updateUserSessions(user);
        userService.save(user);

        return userMapper.mapEntityToDto(user);
    }

    @Transactional
    public void assignRoles(String userId, Set<String> roleIds) {
        checkAdminNotModifyingHimself(userId);

        User user = userService.getUserById(userId);
        List<Role> roles = roleService.getRolesByIds(roleIds);

        user.getRoles().addAll(roles);

        authorizationService.updateUserAuthorizations(user);
        sessionService.updateUserSessions(user);
        userService.save(user);
    }

    @Transactional
    public void revokeRoles(String userId, Set<String> roleIds) {
        checkAdminNotModifyingHimself(userId);

        User user = userService.getUserById(userId);
        Role admin = roleService.getASAdminRole();
        List<Role> roles = roleService.getRolesByIds(roleIds);

        user.getRoles().removeAll(roles.stream()
                .filter(role -> !Objects.equals(role.getId(), admin.getId()))
                .collect(Collectors.toSet())
        );

        authorizationService.updateUserAuthorizations(user);
        sessionService.updateUserSessions(user);
        userService.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        checkAdminNotModifyingHimself(userId);
        User user = userService.getUserById(userId);
        sessionService.closeUserSessions(user);
        authorizationService.deleteAllByUserId(user.getId());
        userService.delete(user);
    }

    private void checkAdminNotModifyingHimself(String userId) {
        String currentId = SecurityUtils.getCurrentUserId();
        if (Objects.equals(currentId, userId)) {
            throw new ForbiddenOperationException("You can't modify yourself");
        }
    }
}
