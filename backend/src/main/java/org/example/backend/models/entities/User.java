package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity(name = "app_user")
@Getter
@Setter
@Builder(builderClassName = "Builder")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String clientRegistrationId;

    @OneToMany(mappedBy = "user")
    private List<EmailVerificationToken> emailVerificationTokens = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities = new ArrayList<>();

    private String email;
    private boolean emailVerified;

    private String password;

    private String name;
    private String givenName;
    private String familyName;

    private Instant lastLogin;

    private Instant createdAt;
    private Instant updatedAt;

    private Instant nextVerificationTokenAt;

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        return this.authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .toList();
    }

    public Optional<EmailVerificationToken> getActiveEmailVerificationToken() {
        return this.emailVerificationTokens.stream()
                .filter(EmailVerificationToken::isActive)
                .findAny();
    }
}
