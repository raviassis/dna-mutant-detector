package org.magneto.infra.kafka.asyncPersist;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.magneto.entities.DNAEntity;

@ApplicationScoped
public class DNAProducer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    @Channel("async-persist-dna-out")
    Emitter<String> emitter;

    public void send(DNAEntity entity) {
        try {
            var dto = DNAToPersistDto.fromEntity(entity);
            String json = mapper.writeValueAsString(dto);
            emitter.send(json);
            System.out.println("Sended message: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
