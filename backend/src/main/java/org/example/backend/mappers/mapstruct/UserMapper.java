package org.example.backend.mappers.mapstruct;

import org.example.backend.dtos.UserDto;
import org.example.backend.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    void mergeUsers(User src, @MappingTarget User trg);

    UserDto mapEntityToDto(User user);
}
