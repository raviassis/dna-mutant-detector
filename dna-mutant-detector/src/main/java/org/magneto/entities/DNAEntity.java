package org.magneto.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DNAEntity extends PanacheEntityBase {
    @Id
    @Column(nullable = false, length = 64)
    public String dnaHash;
    public boolean mutant;
}
