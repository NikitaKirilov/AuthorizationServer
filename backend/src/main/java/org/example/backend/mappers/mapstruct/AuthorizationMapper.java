package org.example.backend.mappers.mapstruct;

import org.example.backend.models.entities.Authorization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthorizationMapper {

    void mergeAuthorizations(Authorization source, @MappingTarget Authorization target);
}
