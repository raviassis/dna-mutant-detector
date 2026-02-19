package org.magneto.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.magneto.core.DNADetector;
import org.magneto.core.DNAHasher;
import org.magneto.entities.DNAEntity;
import org.magneto.entities.StatsEntity;
import org.magneto.infra.kafka.asyncPersist.DNAProducer;
import org.magneto.infra.redis.DNAStatsService;

import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class DNAService {
    private final DNAStatsService dnaStatsService;
    private final DNAProducer dnaProducer;

    @Inject
    public DNAService(
            DNAProducer dnaProducer,
            DNAStatsService dnaStatsService
    ) {
        this.dnaProducer = dnaProducer;
        this.dnaStatsService = dnaStatsService;
    }

    public DNAEntity analyse(String[] dna) throws NoSuchAlgorithmException {
        var dnaEntity = new DNAEntity();
        dnaEntity.dnaHash = DNAHasher.hash(dna);
        dnaEntity.mutant = DNADetector.isMutant(dna);
        this.dnaProducer.send(dnaEntity);
        return dnaEntity;
    }

    public Uni<StatsEntity> stats() {
        return this.dnaStatsService.getStats();
    }
}
