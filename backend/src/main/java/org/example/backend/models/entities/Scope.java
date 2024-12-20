package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Scope {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private long id;

    private String resourceName;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "app_user_scope",
            joinColumns = @JoinColumn(name = "scope_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    public Scope(String name) {
        this.name = name;
    }
}
