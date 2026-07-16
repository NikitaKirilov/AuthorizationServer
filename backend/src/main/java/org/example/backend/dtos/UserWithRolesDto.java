package org.example.backend.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class UserWithRolesDto {

    private UserDto user;
    private Set<RoleDto> roles;
}
