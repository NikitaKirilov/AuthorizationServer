package org.example.backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserWithRolesDto;
import org.example.backend.models.entities.User;
import org.example.backend.services.UserAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping
    public Page<UserDto> getAllUsers(User probe, Pageable pageable) {
        return userAdminService.getAllUsers(probe, pageable);
    }

    @GetMapping("/{id}")
    public UserWithRolesDto getUserWithRolesDtoById(@PathVariable String id) {
        return userAdminService.getUserWithRolesDtoById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable String id, @Valid @RequestBody UserDto userDto) {
        return userAdminService.updateUser(id, userDto);
    }

    @PutMapping("/{userId}/roles")
    public void assignRole(@PathVariable String userId, @RequestBody Set<String> roleIds) {
        userAdminService.assignRoles(userId, roleIds);
    }

    @DeleteMapping("/{userId}/roles")
    public void revokeRole(@PathVariable String userId, @RequestBody Set<String> roleIds) {
        userAdminService.revokeRoles(userId, roleIds);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userAdminService.deleteUser(id);
    }
}
