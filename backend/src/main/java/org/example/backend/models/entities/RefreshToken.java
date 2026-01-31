package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Setter
@Getter
public class RefreshToken {

    @Id
    private String id;

    @ManyToOne
    private User user;

    private String tokenValue;

    private Timestamp createdAt;
    private Timestamp expiresAt;
}
