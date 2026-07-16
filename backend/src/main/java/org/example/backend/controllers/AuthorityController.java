package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.AuthorityDto;
import org.example.backend.models.entities.Authority;
import org.example.backend.services.AuthorityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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
}
