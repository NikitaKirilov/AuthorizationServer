package org.example.backend.dtos;

import lombok.Data;

@Data
public class UserAuthorityDto {

    private String id;
    private String resource;
    private String name;
}
