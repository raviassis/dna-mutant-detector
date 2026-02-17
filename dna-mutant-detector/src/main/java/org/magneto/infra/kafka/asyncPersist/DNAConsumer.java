package org.magneto.infra.kafka.asyncPersist;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.magneto.repositories.DNARepository;

@ApplicationScoped
public class DNAConsumer {
    private final ObjectMapper mapper = new ObjectMapper();

    DNARepository dnaRepository;

    @Inject
    public DNAConsumer(DNARepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    @Incoming("async-persist-dna")
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
            System.out.println("Saved dna: " + dto.dnaHash);

        } catch (Throwable e) {
            // ignoring errors to avoid bad messages blocking the queue
            // TODO: implement dead letter
            System.out.println("Error payload: " + payload);
            e.printStackTrace();
        }
    }
}
