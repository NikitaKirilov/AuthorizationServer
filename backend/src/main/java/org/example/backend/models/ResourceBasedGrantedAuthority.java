package org.example.backend.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode
@Getter
public class ResourceBasedGrantedAuthority implements GrantedAuthority {

    private final String authority;
    private final String resource;

    public ResourceBasedGrantedAuthority(String resource, String authority) {
        this.authority = authority;
        this.resource = resource;
    }
}
