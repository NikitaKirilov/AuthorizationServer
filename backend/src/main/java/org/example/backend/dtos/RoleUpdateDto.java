package org.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleUpdateDto {

    private String id;

    private String resource;
    private String name;
    private String description;

    private Set<String> authorityIds;
}
