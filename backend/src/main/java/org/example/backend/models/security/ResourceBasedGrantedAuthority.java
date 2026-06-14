package org.example.backend.models.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode
@Getter
public class ResourceBasedGrantedAuthority implements GrantedAuthority {

    private static final String DELIMITER = ":";

    private final String name;
    private final String resource;

    public ResourceBasedGrantedAuthority(String resource, String name) {
        this.name = name;
        this.resource = resource;
    }

    @Override
    public @NonNull String getAuthority() {
        return String.join(DELIMITER, resource, name);
    }
}
