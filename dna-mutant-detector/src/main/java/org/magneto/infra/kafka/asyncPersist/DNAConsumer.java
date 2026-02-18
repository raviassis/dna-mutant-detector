package org.magneto.infra.kafka.asyncPersist;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.magneto.entities.StatsEntity;
import org.magneto.infra.redis.DNAStatsService;
import org.magneto.repositories.DNARepository;

@ApplicationScoped
public class DNAConsumer {
    private final ObjectMapper mapper = new ObjectMapper();
    DNAStatsService dnaStatsService;
    DNARepository dnaRepository;

    @Inject
    public DNAConsumer(
            DNARepository dnaRepository,
            DNAStatsService dnaStatsService
    ) {
        this.dnaRepository = dnaRepository;
        this.dnaStatsService = dnaStatsService;
    }

    @Incoming("async-persist-dna")
    @Transactional
    public void process(String payload) {
        try {
            DNAToPersistDto dto = mapper.readValue(payload, DNAToPersistDto.class);
            System.out.println("Processing message: " + dto);
            var dnaEntity = dnaRepository.findByHash(dto.dnaHash);
            if (dnaEntity != null) {
                System.out.println("Ignored duplicated dna: " + dto.dnaHash);
                return;
            }
            dnaEntity = dto.toEntity();
            dnaRepository.persist(dnaEntity);
            StatsEntity stats = dnaRepository.getStats();
            dnaStatsService.updateStats(stats);
            System.out.println("Saved dna: " + dto.dnaHash);

        } catch (Throwable e) {
            // ignoring errors to avoid bad messages blocking the queue
            // TODO: implement dead letter
            System.out.println("Error payload: " + payload);
            e.printStackTrace();
        }
    }
}
