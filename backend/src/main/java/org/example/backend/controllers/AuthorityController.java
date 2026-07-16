package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.AuthorityDto;
import org.example.backend.models.entities.Authority;
import org.example.backend.services.AuthorityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/authorities")
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping
    public Page<AuthorityDto> getAllAuthorities(Authority authority, Pageable pageable) {
        return authorityService.getAllAuthorities(authority, pageable);
    }

    @GetMapping("/{id}")
    public AuthorityDto getAuthorityById(@PathVariable String id) {
        return authorityService.getAuthorityDtoById(id);
    }

    @PostMapping()
    public AuthorityDto createAuthority(@RequestBody AuthorityDto authorityDto) {
        return authorityService.createAuthority(authorityDto);
    }

    @PutMapping("/{id}")
    public AuthorityDto updateAuthority(@PathVariable String id, @RequestBody AuthorityDto authorityDto) {
        return authorityService.updateAuthority(id, authorityDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthority(@PathVariable String id) {
        authorityService.deleteAuthorityById(id);
    }
}
