package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.CustomUserDetails;
import org.example.backend.models.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JPAUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOptionalByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email: " + username));

        return new CustomUserDetails(user);
    }
}
