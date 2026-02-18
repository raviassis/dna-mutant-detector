package org.magneto.infra.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.magneto.entities.StatsEntity;

@ApplicationScoped
public class DNAStatsService {
    private static final String STATS_KEY = "stats";
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    RedisDataSource ds;

    public void updateStats(StatsEntity stats) throws JsonProcessingException {
        var commands = ds.value(String.class);
        try {
            var json = mapper.writeValueAsString(StatsDto.fromEntity(stats));
            commands.set(STATS_KEY, json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StatsEntity getStats() {
        var commands = ds.value(String.class);
        String json = commands.get(STATS_KEY);
        if (json == null) {
            return new StatsEntity();
        }
        try {
            var dto = mapper.readValue(json, StatsDto.class);
            return dto.toEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
