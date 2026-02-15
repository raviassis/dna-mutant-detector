package org.magneto.api.dtos;

import jakarta.json.bind.annotation.JsonbProperty;

public class StatsDto {
    @JsonbProperty("count_mutant_dna")
    public long CountMutantDNA = 0;
    @JsonbProperty("count_human_dna")
    public long CountHumanDNA = 0;
    @JsonbProperty("ratio")
    public double Ratio = 0.0;
}
