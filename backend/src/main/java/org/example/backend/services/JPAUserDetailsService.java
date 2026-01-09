package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.AuthException;
import org.example.backend.models.CustomUserDetails;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JPAUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmailAndEmailVerifiedTrue(username)
                .orElseThrow(() -> new AuthException("User not found by email: " + username));

        return new CustomUserDetails(user);
    }
}
