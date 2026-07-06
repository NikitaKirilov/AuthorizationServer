package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "app_user")
@Getter
@Setter
@Builder(builderClassName = "Builder")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditingEntity {

    private String clientRegistrationId;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<EmailVerificationCode> emailVerificationCodes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "app_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserDevice> userDevices = new HashSet<>();

    private String email;
    private String pendingEmail;

    private boolean emailVerified; //TODO: user wrapper instead of primitive type

    private String password;

    private String nickname;
    private String givenName;
    private String familyName;

    private LocalDate birthday;

    private boolean blocked; //TODO: user wrapper instead of primitive type

    private Boolean superuser;
}
