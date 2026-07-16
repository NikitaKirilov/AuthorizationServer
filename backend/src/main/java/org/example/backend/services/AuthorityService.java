package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.AuthorityDto;
import org.example.backend.exceptions.AuthorityAlreadyExistsException;
import org.example.backend.exceptions.AuthorityNotFoundException;
import org.example.backend.mappers.mapstruct.AuthorityMapper;
import org.example.backend.models.entities.Authority;
import org.example.backend.repositories.AuthorityRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private static final ExampleMatcher AUTHORITY_MATCHER = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withIgnorePaths("roles", "createdAt", "updatedAt");

    private final AuthorityMapper authorityMapper;
    private final AuthorityRepository authorityRepository;

    public Authority getAuthorityById(String id) {
        return authorityRepository.findById(id).orElseThrow(() -> new AuthorityNotFoundException(id));
    }

    public AuthorityDto getAuthorityDtoById(String id) {
        return authorityMapper.mapToAuthorityDto(getAuthorityById(id));
    }

    public List<Authority> getAllAuthoritiesByIds(Set<String> ids) {
        return authorityRepository.findAllById(ids);
    }

    public Page<AuthorityDto> getAllAuthorities(Authority authority, Pageable pageable) {
        return authorityRepository.findAll(Example.of(authority, AUTHORITY_MATCHER), pageable)
                .map(authorityMapper::mapToAuthorityDto);
    }

    @Transactional
    public AuthorityDto createAuthority(AuthorityDto authorityDto) {
        checkAuthorityExists(authorityDto.getResource(), authorityDto.getName(), null);

        Authority authority = new Authority();
        authority.setId(UUID.randomUUID().toString());
        authorityMapper.mergeDto(authorityDto, authority);

        authorityRepository.save(authority);

        return authorityMapper.mapToAuthorityDto(authority);
    }

    @Transactional
    public AuthorityDto updateAuthority(String id, AuthorityDto authorityDto) {
        checkAuthorityExists(authorityDto.getResource(), authorityDto.getName(), id);

        Authority existingAuthority = getAuthorityById(id);
        authorityMapper.mergeDto(authorityDto, existingAuthority);

        authorityRepository.save(existingAuthority);

        return authorityMapper.mapToAuthorityDto(existingAuthority);
    }

    public void deleteAuthorityById(String id) {
        authorityRepository.deleteById(id);
    }

    private void checkAuthorityExists(String resource, String name, String id) {
        if (authorityRepository.existsByResourceAndNameAndIdNot(resource, name, id)) {
            throw new AuthorityAlreadyExistsException(resource, name, id);
        }
    }
}
