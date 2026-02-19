package org.magneto.entities;


public class StatsEntity {
    public long countMutantDNA = 0;
    public long countHumanDNA = 0;

    public StatsEntity() {}

    public StatsEntity(long countMutantDNA, long countHumanDNA ) {
        this.countMutantDNA = countMutantDNA;
        this.countHumanDNA = countHumanDNA;
    }

    public double getRatio() {
        if (this.countHumanDNA != 0)
            return this.countMutantDNA / (double) this.countHumanDNA;
        return 0;
    }
}
