package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.Authority;
import org.example.backend.repositories.AuthorityRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public Authority getDefaultAuthority() {
        return authorityRepository.getDefaultAuthority();
    }
}
