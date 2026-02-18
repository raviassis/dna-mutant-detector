package org.magneto.infra.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.magneto.entities.StatsEntity;

@ApplicationScoped
public class DNAStatsService {
    private static final String STATS_KEY = "stats";
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    ReactiveRedisDataSource ds;

    public Uni<Void> updateStats(StatsEntity stats) throws JsonProcessingException {
        var commands = ds.value(String.class);
        try {
            var json = mapper.writeValueAsString(StatsDto.fromEntity(stats));
            return commands.set(STATS_KEY, json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Uni<StatsEntity> getStats() {
        var commands = ds.value(String.class);
        return commands.get(STATS_KEY)
                .onItem().transform(json -> {
                    if (json == null) {
                        return new StatsEntity();
                    }
                    try {
                        return mapper.readValue(json, StatsDto.class).toEntity();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
