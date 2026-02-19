package org.magneto.api;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.magneto.api.dtos.StatsDto;
import org.magneto.entities.StatsEntity;
import org.magneto.services.DNAService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatsControllerTest {

    @Test
    void shouldReturnOkWithStats() {
        DNAService dnaService = mock(DNAService.class);
        StatsEntity stats = new StatsEntity(40L, 10L);
        when(dnaService.stats()).thenReturn(Uni.createFrom().item(stats));
        StatsController controller = new StatsController();
        controller.dnaService = dnaService;

        var response = controller.stats().await().indefinitely();

        assertEquals(200, response.getStatus());
        StatsDto body = assertInstanceOf(StatsDto.class, response.getEntity());
        assertEquals(40L, body.countMutantDNA);
        assertEquals(10L, body.countHumanDNA);
        assertEquals(4.0, body.ratio);
    }

    @Test
    void shouldFailWhenStatsServiceFails() {
        DNAService dnaService = mock(DNAService.class);
        when(dnaService.stats()).thenReturn(Uni.createFrom().failure(new RuntimeException("redis unavailable")));
        StatsController controller = new StatsController();
        controller.dnaService = dnaService;

        var error = assertThrows(RuntimeException.class, () -> controller.stats().await().indefinitely());
        assertEquals("redis unavailable", error.getMessage());
    }
}
