package org.example.backend.mappers.mapstruct;

import org.example.backend.dtos.OAuth2ClientDto;
import org.example.backend.models.entities.OAuth2Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.StringUtils;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OAuth2ClientMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "scopes", target = "scopes", qualifiedByName = "stringSetToString")
    @Mapping(source = "redirectUris", target = "redirectUris", qualifiedByName = "stringSetToString")
    @Mapping(source = "postLogoutRedirectUris", target = "postLogoutRedirectUris", qualifiedByName = "stringSetToString")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void mergeDto(OAuth2ClientDto dto, @MappingTarget OAuth2Client client);

    @Mapping(source = "scopes", target = "scopes", qualifiedByName = "stringToStringSet")
    @Mapping(source = "redirectUris", target = "redirectUris", qualifiedByName = "stringToStringSet")
    @Mapping(source = "postLogoutRedirectUris", target = "postLogoutRedirectUris", qualifiedByName = "stringToStringSet")
    OAuth2ClientDto mapEntityToDto(OAuth2Client oAuth2Client);

    @Mapping(source = "scopes", target = "scopes", qualifiedByName = "stringSetToString")
    @Mapping(source = "redirectUris", target = "redirectUris", qualifiedByName = "stringSetToString")
    @Mapping(source = "postLogoutRedirectUris", target = "postLogoutRedirectUris", qualifiedByName = "stringSetToString")
    OAuth2Client mapDtoToEntity(OAuth2ClientDto dto);

    @Named("stringSetToString")
    static String setToString(Set<String> set) {
        return StringUtils.collectionToCommaDelimitedString(set);
    }

    @Named("stringToStringSet")
    static Set<String> stringToStringSet(String str) {
        return StringUtils.commaDelimitedListToSet(str);
    }
}
