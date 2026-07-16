package org.example.backend.mappers.mapstruct;

import org.example.backend.dtos.AuthorityDto;
import org.example.backend.dtos.UserAuthorityDto;
import org.example.backend.models.entities.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorityMapper {

    UserAuthorityDto mapToUserAuthorityDto(Authority authority);

    AuthorityDto mapToAuthorityDto(Authority authority);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "createdAt", target = "createdAt", ignore = true)
    @Mapping(source = "updatedAt", target = "updatedAt", ignore = true)
    void mergeDto(AuthorityDto dto, @MappingTarget Authority authority);
}
