package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Authority {

    @Id
    private String id;
    private String resource;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles = new HashSet<>();

    private String name;
    private String description;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public String getFullName() {
        return resource + ":"  + name;
    }
}
