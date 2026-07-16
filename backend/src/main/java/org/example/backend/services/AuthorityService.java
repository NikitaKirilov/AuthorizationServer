package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.AuthorityDto;
import org.example.backend.mappers.mapstruct.AuthorityMapper;
import org.example.backend.models.entities.Authority;
import org.example.backend.repositories.AuthorityRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public List<Authority> getAllAuthoritiesByIds(Set<String> ids) {
        return authorityRepository.findAllById(ids);
    }

    public Page<AuthorityDto> getAllAuthorities(Authority authority, Pageable pageable) {
        return authorityRepository.findAll(Example.of(authority, AUTHORITY_MATCHER), pageable)
                .map(authorityMapper::mapToAuthorityDto);
    }
}
