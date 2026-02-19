package org.magneto.services;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.magneto.core.DNAHasher;
import org.magneto.entities.DNAEntity;
import org.magneto.entities.StatsEntity;
import org.magneto.infra.kafka.asyncPersist.DNAProducer;
import org.magneto.infra.redis.DNAStatsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DNAServiceTest {

    @Test
    void shouldAnalyseMutantDnaAndSendForAsyncPersistence() throws Exception {
        DNAProducer dnaProducer = mock(DNAProducer.class);
        DNAStatsService dnaStatsService = mock(DNAStatsService.class);
        DNAService service = new DNAService(dnaProducer, dnaStatsService);
        String[] dna = {
                "AAAAGA",
                "AAAAGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        DNAEntity result = service.analyse(dna);

        assertTrue(result.mutant);
        assertEquals(DNAHasher.hash(dna), result.dnaHash);
        verify(dnaProducer, times(1)).send(result);
    }

    @Test
    void shouldAnalyseHumanDnaAndSendForAsyncPersistence() throws Exception {
        DNAProducer dnaProducer = mock(DNAProducer.class);
        DNAStatsService dnaStatsService = mock(DNAStatsService.class);
        DNAService service = new DNAService(dnaProducer, dnaStatsService);
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        DNAEntity result = service.analyse(dna);

        assertFalse(result.mutant);
        assertEquals(DNAHasher.hash(dna), result.dnaHash);
        verify(dnaProducer, times(1)).send(result);
    }

    @Test
    void shouldReturnStatsFromDNAStatsService() {
        DNAProducer dnaProducer = mock(DNAProducer.class);
        DNAStatsService dnaStatsService = mock(DNAStatsService.class);
        DNAService service = new DNAService(dnaProducer, dnaStatsService);
        StatsEntity expected = new StatsEntity(40, 10);
        when(dnaStatsService.getStats()).thenReturn(Uni.createFrom().item(expected));

        StatsEntity result = service.stats().await().indefinitely();

        assertEquals(40, result.countMutantDNA);
        assertEquals(10, result.countHumanDNA);
        assertEquals(4.0, result.getRatio());
        verify(dnaStatsService, times(1)).getStats();
    }

    @Test
    void shouldPropagateStatsFailures() {
        DNAProducer dnaProducer = mock(DNAProducer.class);
        DNAStatsService dnaStatsService = mock(DNAStatsService.class);
        DNAService service = new DNAService(dnaProducer, dnaStatsService);
        when(dnaStatsService.getStats()).thenReturn(Uni.createFrom().failure(new RuntimeException("redis down")));

        RuntimeException error = assertThrows(RuntimeException.class, () -> service.stats().await().indefinitely());

        assertEquals("redis down", error.getMessage());
        verify(dnaStatsService, times(1)).getStats();
    }
}
