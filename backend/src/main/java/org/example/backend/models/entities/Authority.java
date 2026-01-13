package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Authority {

    @Id
    private String id;
    private String registeredClientId;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users = new ArrayList<>();

    private String name;
    private String description;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
