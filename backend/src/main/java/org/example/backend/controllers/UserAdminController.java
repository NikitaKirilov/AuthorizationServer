package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserDetailsDto;
import org.example.backend.models.entities.User;
import org.example.backend.services.UserAdminService;
import org.example.backend.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;
    private final UserService userService;

    @GetMapping
    public Page<UserDto> getAllUsers(User probe, Pageable pageable) {
        return userService.getAllUsersDto(probe, pageable);
    }

    @GetMapping("/{id}")
    public UserDetailsDto getUserDetailsById(@PathVariable String id) {
        return userService.getUserDetailsDtoById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return userAdminService.updateUser(id, userDto);
    }

    @PutMapping("/{userId}/roles/{roleId}")
    public void assignRole(@PathVariable String userId, @PathVariable String roleId) {
        userAdminService.assignRole(userId, roleId);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public void revokeRole(@PathVariable String userId, @PathVariable String roleId) {
        userAdminService.revokeRole(userId, roleId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userAdminService.deleteUser(id);
    }
}
