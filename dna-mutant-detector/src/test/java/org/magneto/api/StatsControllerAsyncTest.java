package org.magneto.api;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.magneto.api.dtos.StatsDto;
import org.magneto.entities.StatsEntity;
import org.magneto.services.DNAServiceAsync;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatsControllerAsyncTest {

    @Test
    void shouldReturnOkWithStats() {
        DNAServiceAsync dnaServiceAsync = mock(DNAServiceAsync.class);
        StatsEntity stats = new StatsEntity();
        stats.countMutantDNA = 40L;
        stats.countHumanDNA = 10L;
        stats.ratio = 4.0;
        when(dnaServiceAsync.stats()).thenReturn(Uni.createFrom().item(stats));
        StatsControllerAsync controller = new StatsControllerAsync();
        controller.dnaService = dnaServiceAsync;

        var response = controller.stats().await().indefinitely();

        assertEquals(200, response.getStatus());
        StatsDto body = assertInstanceOf(StatsDto.class, response.getEntity());
        assertEquals(40L, body.countMutantDNA);
        assertEquals(10L, body.countHumanDNA);
        assertEquals(4.0, body.ratio);
    }

    @Test
    void shouldFailWhenStatsServiceFails() {
        DNAServiceAsync dnaServiceAsync = mock(DNAServiceAsync.class);
        when(dnaServiceAsync.stats()).thenReturn(Uni.createFrom().failure(new RuntimeException("redis unavailable")));
        StatsControllerAsync controller = new StatsControllerAsync();
        controller.dnaService = dnaServiceAsync;

        var error = assertThrows(RuntimeException.class, () -> controller.stats().await().indefinitely());
        assertEquals("redis unavailable", error.getMessage());
    }
}
