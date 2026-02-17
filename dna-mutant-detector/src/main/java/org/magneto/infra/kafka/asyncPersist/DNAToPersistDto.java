package org.magneto.infra.kafka.asyncPersist;

import org.magneto.entities.DNAEntity;

public class DNAToPersistDto {
    public String dnaHash;
    public boolean isMutant;

    public DNAEntity toEntity() {
        var dnaEntity = new DNAEntity();
        dnaEntity.dnaHash = dnaHash;
        dnaEntity.mutant = isMutant;
        return dnaEntity;
    }

    @Override
    public String toString() {
        return "DNAToPersistDto{" +
                "dnaHash='" + dnaHash + '\'' +
                ", isMutant=" + isMutant +
                '}';
    }
}
