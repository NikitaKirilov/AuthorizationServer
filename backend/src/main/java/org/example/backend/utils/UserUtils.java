package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.example.backend.models.entities.Authority;
import org.example.backend.models.entities.Role;
import org.example.backend.models.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UserUtils {

    private static final String AS_PREFIX = "AS";

    public static Collection<GrantedAuthority> getGrantedAuthorities(User user) {
        return user.getRoles().stream()
                .filter(role -> Objects.equals(role.getResource(), AS_PREFIX))
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .map(Authority::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public static Set<Authority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
