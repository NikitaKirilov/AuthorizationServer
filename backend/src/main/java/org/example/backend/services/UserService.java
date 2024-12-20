package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getOptionalByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User saveUserOnIdpLogin(User user) {
        String email = user.getEmail();
        ClientRegistrationWrapper clientRegistrationWrapper = user.getClientRegistrationWrapper();
        Timestamp lastLogin = user.getLastLogin();

        user = getOptionalByEmail(email)
                .map(existingUser -> {
                    existingUser.setClientRegistrationWrapper(clientRegistrationWrapper);
                    existingUser.setLastLogin(lastLogin);
                    //TODO: try to merge user attribute (like name, familyname, etc) if existingUser attribute is null
                    return existingUser;
                }).orElse(user);

        return save(user);
    }
}
