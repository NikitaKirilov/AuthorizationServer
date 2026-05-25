package org.example.backend.mappers.mapstruct;

import org.example.backend.dtos.UserDto;
import org.example.backend.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    void mergeUsers(User src, @MappingTarget User trg);

    UserDto mapEntityToDto(User user);
}
