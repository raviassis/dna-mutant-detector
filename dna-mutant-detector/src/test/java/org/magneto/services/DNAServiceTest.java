package org.magneto.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.magneto.api.dtos.StatsDto;
import org.magneto.core.DNAHasher;
import org.magneto.entities.DNAEntity;
import org.magneto.repositories.DNARepository;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DNAServiceTest {
    @Test
    void shouldReturnExistingEntityWhenHashAlreadyExists() throws NoSuchAlgorithmException {
        String[] dna = {
                "AAAAGA",
                "AAAAGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        String hash = DNAHasher.hash(dna);
        DNAEntity existing = new DNAEntity();
        existing.dnaHash = hash;
        existing.mutant = true;

        DNARepository repository = mock(DNARepository.class);
        when(repository.findByHash(hash)).thenReturn(existing);
        DNAService service = new DNAService(repository);

        DNAEntity result = service.analyse(dna);

        assertSame(existing, result);
        verify(repository, never()).persist(any(DNAEntity.class));
    }

    @Test
    void shouldCreateAndPersistEntityWhenHashDoesNotExist() throws NoSuchAlgorithmException {
        String[] dna = {
                "AAAAGA",
                "AAAAGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        String hash = DNAHasher.hash(dna);

        DNARepository repository = mock(DNARepository.class);
        when(repository.findByHash(hash)).thenReturn(null);
        DNAService service = new DNAService(repository);

        DNAEntity result = service.analyse(dna);

        ArgumentCaptor<DNAEntity> entityCaptor = ArgumentCaptor.forClass(DNAEntity.class);
        verify(repository).persist(entityCaptor.capture());
        DNAEntity persisted = entityCaptor.getValue();
        assertSame(persisted, result);
        assertEquals(hash, persisted.dnaHash);
        assertTrue(persisted.mutant);
    }

    @Test
    void shouldCreateAndPersistHumanEntityWhenHashDoesNotExist() throws NoSuchAlgorithmException {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        String hash = DNAHasher.hash(dna);

        DNARepository repository = mock(DNARepository.class);
        when(repository.findByHash(hash)).thenReturn(null);
        DNAService service = new DNAService(repository);

        DNAEntity result = service.analyse(dna);

        ArgumentCaptor<DNAEntity> entityCaptor = ArgumentCaptor.forClass(DNAEntity.class);
        verify(repository).persist(entityCaptor.capture());
        DNAEntity persisted = entityCaptor.getValue();
        assertSame(persisted, result);
        assertEquals(hash, persisted.dnaHash);
        assertFalse(persisted.mutant);
    }

    @Test
    void shouldReturnStatsWithRatioWhenHumanCountIsNotZero() {
        DNARepository repository = mock(DNARepository.class);
        when(repository.countDNAs()).thenReturn(new DNARepository.DNACounts(40L, 10L));
        DNAService service = new DNAService(repository);

        StatsDto stats = service.stats();

        assertEquals(40L, stats.CountMutantDNA);
        assertEquals(10L, stats.CountHumanDNA);
        assertEquals(4.0, stats.Ratio);
    }

    @Test
    void shouldReturnStatsWithoutRatioWhenHumanCountIsZero() {
        DNARepository repository = mock(DNARepository.class);
        when(repository.countDNAs()).thenReturn(new DNARepository.DNACounts(5L, 0L));
        DNAService service = new DNAService(repository);

        StatsDto stats = service.stats();

        assertEquals(5L, stats.CountMutantDNA);
        assertEquals(0L, stats.CountHumanDNA);
        assertEquals(0.0, stats.Ratio);
    }
}
