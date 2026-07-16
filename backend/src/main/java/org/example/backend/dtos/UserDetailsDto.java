package org.example.backend.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class UserDetailsDto {

    private UserDto user;
    private Set<UserRoleDto> roles;
    private Set<UserAuthorityDto> authorities;
}
