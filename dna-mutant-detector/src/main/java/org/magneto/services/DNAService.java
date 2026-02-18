package org.magneto.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.magneto.api.dtos.StatsDto;
import org.magneto.core.DNADetector;
import org.magneto.core.DNAHasher;
import org.magneto.entities.DNAEntity;
import org.magneto.infra.kafka.asyncPersist.DNAProducer;
import org.magneto.repositories.DNARepository;

import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class DNAService {
    DNARepository dnaRepository;
    DNAProducer dnaProducer;

    @Inject
    public DNAService(
            DNARepository dnaRepository,
            DNAProducer dnaProducer
    ) {
        this.dnaRepository = dnaRepository;
        this.dnaProducer = dnaProducer;
    }

    @Transactional
    public DNAEntity analyse(String[] dna) throws NoSuchAlgorithmException {
        var hash = DNAHasher.hash(dna);
        var dnaEntity = dnaRepository.findByHash(hash);

        if (dnaEntity == null) {
            dnaEntity = new DNAEntity();
            dnaEntity.dnaHash = hash;
            dnaEntity.mutant = DNADetector.isMutant(dna);
            dnaRepository.persist(dnaEntity);
        }
        return dnaEntity;
    }

    public StatsDto stats() {
        var counts = dnaRepository.countDNAs();
        var countMutants = counts.mutants();
        var countHumans = counts.humans();
        var stats = new StatsDto();
        stats.countMutantDNA = countMutants;
        stats.countHumanDNA = countHumans;
        if (countHumans != 0)
            stats.ratio = countMutants / (double) countHumans;
        return stats;
    }
}
