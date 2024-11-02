package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.example.backend.models.entities.Scope;
import org.example.backend.models.entities.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

@UtilityClass
public class UserUtils {

    public static Collection<SimpleGrantedAuthority> getAuthorities(User user) {
        List<Scope> scopes = user.getScopes();

        if (scopes == null) {
            return emptySet();
        }

        return scopes.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toSet());
    }
}
