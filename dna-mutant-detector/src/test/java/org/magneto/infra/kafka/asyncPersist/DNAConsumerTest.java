package org.magneto.infra.kafka.asyncPersist;

import org.junit.jupiter.api.Test;
import org.magneto.infra.redis.DNAStatsService;
import org.mockito.ArgumentCaptor;
import org.magneto.entities.DNAEntity;
import org.magneto.repositories.DNARepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DNAConsumerTest {

    @Test
    void shouldPersistEntityWhenHashDoesNotExist() {
        DNARepository repository = mock(DNARepository.class);
        var statsService = mock(DNAStatsService.class);
        when(repository.findByHash("hash-new")).thenReturn(null);
        DNAConsumer consumer = new DNAConsumer(repository, statsService);
        String payload = "{\"dnaHash\":\"hash-new\",\"isMutant\":true}";

        consumer.process(payload);

        ArgumentCaptor<DNAEntity> entityCaptor = ArgumentCaptor.forClass(DNAEntity.class);
        verify(repository).persist(entityCaptor.capture());
        DNAEntity persisted = entityCaptor.getValue();
        assertEquals("hash-new", persisted.dnaHash);
        assertTrue(persisted.mutant);
    }

    @Test
    void shouldIgnoreDuplicatedHash() {
        DNARepository repository = mock(DNARepository.class);
        var statsService = mock(DNAStatsService.class);
        DNAEntity existing = new DNAEntity();
        existing.dnaHash = "hash-existing";
        when(repository.findByHash("hash-existing")).thenReturn(existing);
        DNAConsumer consumer = new DNAConsumer(repository, statsService);
        String payload = "{\"dnaHash\":\"hash-existing\",\"isMutant\":false}";

        consumer.process(payload);

        verify(repository, never()).persist(org.mockito.ArgumentMatchers.any(DNAEntity.class));
    }

    @Test
    void shouldNotTouchRepository() {
        DNARepository repository = mock(DNARepository.class);
        var statsService = mock(DNAStatsService.class);
        DNAConsumer consumer = new DNAConsumer(repository, statsService);

        assertThrows(RuntimeException.class, () -> consumer.process("not-a-json"));
        verifyNoInteractions(repository);
    }
}
