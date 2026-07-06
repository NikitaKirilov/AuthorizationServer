package org.example.backend.models.entities;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class CommonEntity {

    @Id
    private String id;

    public boolean isNew() {
        return this.id == null;
    }
}
