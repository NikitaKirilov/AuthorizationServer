package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RoleDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserWithRolesDto;
import org.example.backend.exceptions.ForbiddenOperationException;
import org.example.backend.mappers.mapstruct.RoleMapper;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.Role;
import org.example.backend.models.entities.User;
import org.example.backend.utils.SecurityUtils;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private static final ExampleMatcher EXAMPLE_MATCHER = ExampleMatcher.matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnorePaths("roles", "emailVerificationCodes", "userDevices", "emailVerified", "blocked");

    private final AuthorizationService authorizationService;
    private final RoleMapper roleMapper;
    private final RoleService roleService;
    private final SessionService sessionService;
    private final UserMapper userMapper;
    private final UserService userService;

    public Page<UserDto> getAllUsers(User probe, Pageable pageable) {
        return userService.getAllUsers(probe, EXAMPLE_MATCHER, pageable)
                .map(userMapper::mapEntityToDto);
    }

    public UserWithRolesDto getUserWithRolesDtoById(String id) {
        User user = userService.getUserById(id);

        UserDto userDto = userMapper.mapEntityToDto(user);
        Set<RoleDto> roleDtos = user.getRoles().stream()
                .map(roleMapper::mapToDto)
                .collect(Collectors.toSet());

        UserWithRolesDto userWithRolesDto = new UserWithRolesDto();
        userWithRolesDto.setUser(userDto);
        userWithRolesDto.setRoles(roleDtos);

        return userWithRolesDto;
    }

    @Transactional
    public UserDto updateUser(String id, UserDto userDto) {
        checkAdminNotModifyingHimself(id);

        User user = userService.getUserById(id);
        user.setNickname(userDto.getNickname());
        user.setGivenName(userDto.getGivenName());
        user.setFamilyName(userDto.getFamilyName());
        user.setBirthday(userDto.getBirthday());
        user.setBlocked(userDto.isBlocked());

        userService.save(user);

        if (user.isBlocked()) {
            sessionService.deleteUserSessions(user);
        }

        return userMapper.mapEntityToDto(user);
    }

    @Transactional
    public void assignRoles(String userId, Set<String> roleIds) {
        checkAdminNotModifyingHimself(userId);

        User user = userService.getUserById(userId);
        List<Role> roles = roleService.getRolesByIds(roleIds);

        user.getRoles().addAll(roles);

        userService.save(user);
        sessionService.updateUserAuthorities(user);
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

        userService.save(user);
        sessionService.updateUserAuthorities(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        checkAdminNotModifyingHimself(userId);
        User user = userService.getUserById(userId);
        sessionService.deleteUserSessions(user);
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
