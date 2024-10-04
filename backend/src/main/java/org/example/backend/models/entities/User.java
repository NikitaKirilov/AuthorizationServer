package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static java.util.Collections.emptyList;

@Entity(name = "app_user")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private long id;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "app_user_scope",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id")
    )
    private List<Scope> scopes = emptyList();

    private String email;
    private boolean emailVerified;

    private String password;

    private String name;
    private String familyName;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    //TODO set user role
    public User(String email, boolean emailVerified, String password, String name, String familyName) {
        this.email = email;
        this.emailVerified = emailVerified;

        this.password = password;
        this.name = name;
        this.familyName = familyName;

        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public void setScopes(List<Scope> scopes) {
        this.scopes.addAll(scopes);
    }
}
