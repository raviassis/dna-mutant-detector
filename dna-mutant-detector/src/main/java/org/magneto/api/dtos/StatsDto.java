package org.magneto.api.dtos;

import jakarta.json.bind.annotation.JsonbProperty;
import org.magneto.entities.StatsEntity;

public class StatsDto {
    @JsonbProperty("count_mutant_dna")
    public long countMutantDNA = 0;
    @JsonbProperty("count_human_dna")
    public long countHumanDNA = 0;
    @JsonbProperty("ratio")
    public double ratio = 0.0;

    public static StatsDto fromEntity(StatsEntity stats) {
        var dto = new StatsDto();
        dto.countHumanDNA = stats.countHumanDNA;
        dto.countMutantDNA = stats.countMutantDNA;
        dto.ratio = stats.getRatio();
        return dto;
    }
}
