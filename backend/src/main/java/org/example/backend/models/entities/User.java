package org.example.backend.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Entity(name = "app_user")
@Getter
@Setter
@Builder(builderClassName = "Builder")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "app_user_scope",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id")
    )
    private List<Scope> scopes;

    @ManyToOne
    @JoinColumn(name = "idp_registration_id")
    @JsonBackReference
    private IdpRegistration idpRegistration;

    private String email;
    private boolean emailVerified;

    private String password;

    private String name;
    private String givenName;
    private String familyName;

    private Timestamp lastLogin;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
