package org.magneto.infra.redis;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.magneto.entities.StatsEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DNAStatsServiceTest {

    @Test
    void shouldPersistSerializedStatsIntoRedis() throws Exception {
        ReactiveRedisDataSource ds = mock(ReactiveRedisDataSource.class);
        ReactiveValueCommands<String, String> commands = mock(ReactiveValueCommands.class);
        when(ds.value(String.class)).thenReturn(commands);
        var json = "{\"countMutantDNA\":1,\"countHumanDNA\":2,\"ratio\":0.5}";
        when(commands.set(eq("stats"), eq(json)))
                .thenReturn(Uni.createFrom().item((Void) null));

        DNAStatsService service = new DNAStatsService();
        service.ds = ds;
        StatsEntity stats = new StatsEntity();
        stats.countMutantDNA = 1;
        stats.countHumanDNA = 2;
        stats.ratio = 0.5;

        service.updateStats(stats).await().indefinitely();

        verify(commands).set("stats", json);
    }

    @Test
    void shouldWrapRedisFailuresWhenUpdatingStats() {
        ReactiveRedisDataSource ds = mock(ReactiveRedisDataSource.class);
        ReactiveValueCommands<String, String> commands = mock(ReactiveValueCommands.class);
        when(ds.value(String.class)).thenReturn(commands);
        when(commands.set(eq("stats"), eq("{\"countMutantDNA\":1,\"countHumanDNA\":2,\"ratio\":0.5}")))
                .thenThrow(new RuntimeException("redis down"));

        DNAStatsService service = new DNAStatsService();
        service.ds = ds;
        StatsEntity stats = new StatsEntity();
        stats.countMutantDNA = 1;
        stats.countHumanDNA = 2;
        stats.ratio = 0.5;

        assertThrows(RuntimeException.class, () -> service.updateStats(stats));
    }

    @Test
    void shouldReturnDefaultStatsWhenRedisHasNoValue() {
        ReactiveRedisDataSource ds = mock(ReactiveRedisDataSource.class);
        ReactiveValueCommands<String, String> commands = mock(ReactiveValueCommands.class);
        when(ds.value(String.class)).thenReturn(commands);
        when(commands.get("stats")).thenReturn(Uni.createFrom().nullItem());

        DNAStatsService service = new DNAStatsService();
        service.ds = ds;

        StatsEntity result = service.getStats().await().indefinitely();

        assertEquals(0, result.countMutantDNA);
        assertEquals(0, result.countHumanDNA);
        assertEquals(0.0, result.ratio);
    }

    @Test
    void shouldReturnDeserializedStatsFromRedis() {
        ReactiveRedisDataSource ds = mock(ReactiveRedisDataSource.class);
        ReactiveValueCommands<String, String> commands = mock(ReactiveValueCommands.class);
        when(ds.value(String.class)).thenReturn(commands);
        when(commands.get("stats"))
                .thenReturn(Uni.createFrom().item("{\"countMutantDNA\":9,\"countHumanDNA\":4,\"ratio\":2.25}"));

        DNAStatsService service = new DNAStatsService();
        service.ds = ds;

        StatsEntity result = service.getStats().await().indefinitely();

        assertEquals(9, result.countMutantDNA);
        assertEquals(4, result.countHumanDNA);
        assertEquals(2.25, result.ratio);
    }

    @Test
    void shouldFailWhenRedisReturnsInvalidJson() {
        ReactiveRedisDataSource ds = mock(ReactiveRedisDataSource.class);
        ReactiveValueCommands<String, String> commands = mock(ReactiveValueCommands.class);
        when(ds.value(String.class)).thenReturn(commands);
        when(commands.get("stats")).thenReturn(Uni.createFrom().item("not-json"));

        DNAStatsService service = new DNAStatsService();
        service.ds = ds;

        assertThrows(RuntimeException.class, () -> service.getStats().await().indefinitely());
    }
}
