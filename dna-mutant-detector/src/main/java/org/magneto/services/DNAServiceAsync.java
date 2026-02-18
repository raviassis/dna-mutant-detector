package org.magneto.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.magneto.api.dtos.StatsDto;
import org.magneto.core.DNADetector;
import org.magneto.core.DNAHasher;
import org.magneto.entities.DNAEntity;
import org.magneto.entities.StatsEntity;
import org.magneto.infra.kafka.asyncPersist.DNAProducer;
import org.magneto.infra.redis.DNAStatsService;
import org.magneto.repositories.DNARepository;

import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class DNAServiceAsync {
    private final DNAStatsService dnaStatsService;
    private final DNARepository dnaRepository;
    private final DNAProducer dnaProducer;

    @Inject
    public DNAServiceAsync(
            DNARepository dnaRepository,
            DNAProducer dnaProducer,
            DNAStatsService dnaStatsService
    ) {
        this.dnaRepository = dnaRepository;
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
