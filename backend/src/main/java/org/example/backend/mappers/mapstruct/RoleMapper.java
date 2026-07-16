package org.example.backend.mappers.mapstruct;

import org.example.backend.dtos.RoleDto;
import org.example.backend.dtos.UserRoleDto;
import org.example.backend.models.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleDto mapToDto(Role role);

    UserRoleDto mapToUserRoleDto(Role role);
}
