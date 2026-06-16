package org.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class UserDetailsDto {

    @JsonProperty(value = "user")
    private UserDto userDto;
    private Set<String> roles;
    private Set<String> authorities;
}
