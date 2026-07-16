package org.example.backend.mappers.mapstruct;

import org.example.backend.dtos.AuthorityDto;
import org.example.backend.dtos.UserAuthorityDto;
import org.example.backend.models.entities.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorityMapper {

    UserAuthorityDto mapToUserAuthorityDto(Authority authority);

    AuthorityDto mapToAuthorityDto(Authority authority);
}
