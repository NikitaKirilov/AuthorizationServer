package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.UserDetailsImpl;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)); //TODO create own exception

        return new UserDetailsImpl(user);
    }
}
