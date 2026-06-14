package org.example.backend.mappers.mapstruct;

import org.example.backend.dtos.OAuth2ClientDto;
import org.example.backend.models.entities.OAuth2Client;
import org.example.backend.utils.MapStructUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapStructUtils.class)
public interface OAuth2ClientMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "scopes", target = "scopes", qualifiedByName = "collectionToString")
    @Mapping(source = "redirectUris", target = "redirectUris", qualifiedByName = "collectionToString")
    @Mapping(source = "postLogoutRedirectUris", target = "postLogoutRedirectUris", qualifiedByName = "collectionToString")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void mergeDto(OAuth2ClientDto dto, @MappingTarget OAuth2Client client);

    @Mapping(source = "scopes", target = "scopes", qualifiedByName = "stringToStringSet")
    @Mapping(source = "redirectUris", target = "redirectUris", qualifiedByName = "stringToStringSet")
    @Mapping(source = "postLogoutRedirectUris", target = "postLogoutRedirectUris", qualifiedByName = "stringToStringSet")
    OAuth2ClientDto mapEntityToDto(OAuth2Client oAuth2Client);

    @Mapping(source = "scopes", target = "scopes", qualifiedByName = "collectionToString")
    @Mapping(source = "redirectUris", target = "redirectUris", qualifiedByName = "collectionToString")
    @Mapping(source = "postLogoutRedirectUris", target = "postLogoutRedirectUris", qualifiedByName = "collectionToString")
    OAuth2Client mapDtoToEntity(OAuth2ClientDto dto);

}
