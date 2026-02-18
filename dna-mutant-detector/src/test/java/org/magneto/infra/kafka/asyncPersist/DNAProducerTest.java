package org.magneto.infra.kafka.asyncPersist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.magneto.entities.DNAEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class DNAProducerTest {

    @Test
    void shouldSendSerializedMessageToEmitter() throws Exception {
        Emitter<String> emitter = mock(Emitter.class);
        DNAProducer producer = new DNAProducer();
        producer.emitter = emitter;

        DNAEntity entity = new DNAEntity();
        entity.dnaHash = "hash-123";
        entity.mutant = true;

        producer.send(entity);

        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(emitter).send(payloadCaptor.capture());
        DNAToPersistDto dto = new ObjectMapper().readValue(payloadCaptor.getValue(), DNAToPersistDto.class);
        assertEquals("hash-123", dto.dnaHash);
        assertTrue(dto.isMutant);
    }

    @Test
    void shouldIgnoreSerializationErrors() {
        Emitter<String> emitter = mock(Emitter.class);
        DNAProducer producer = new DNAProducer();
        producer.emitter = emitter;

        assertDoesNotThrow(() -> producer.send(null));
        verifyNoInteractions(emitter);
    }

    @Test
    void shouldIgnoreEmitterFailures() {
        Emitter<String> emitter = mock(Emitter.class);
        doThrow(new RuntimeException("kafka unavailable")).when(emitter).send(org.mockito.ArgumentMatchers.anyString());
        DNAProducer producer = new DNAProducer();
        producer.emitter = emitter;

        DNAEntity entity = new DNAEntity();
        entity.dnaHash = "hash-456";
        entity.mutant = false;

        assertDoesNotThrow(() -> producer.send(entity));
        verify(emitter).send(org.mockito.ArgumentMatchers.anyString());
    }
}
