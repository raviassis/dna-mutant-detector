package org.magneto.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.magneto.api.dtos.StatsDto;
import org.magneto.entities.DNAEntity;
import org.magneto.entities.StatsEntity;

@ApplicationScoped
public class DNARepository implements PanacheRepository<DNAEntity> {
    public StatsEntity getStats() {
        var count = this.countDNAs();
        var stats = new StatsEntity();
        stats.countMutantDNA = count.mutants;
        stats.countHumanDNA = count.humans;
        if (count.humans != 0)
            stats.ratio = count.mutants / (double) count.humans;
        return stats;
    }

    public record DNACounts(long mutants, long humans) {
    }

    public DNAEntity findByHash(String hash) {
        return find("dnaHash", hash).firstResult();
    }

    public DNACounts countDNAs() {
        Object[] row = (Object[]) getEntityManager()
                .createQuery("""
                        select
                            sum(case when d.mutant = true then 1 else 0 end),
                            sum(case when d.mutant = false then 1 else 0 end)
                        from DNAEntity d
                        """)
                .getSingleResult();

        long mutants = row[0] == null ? 0L : ((Number) row[0]).longValue();
        long humans = row[1] == null ? 0L : ((Number) row[1]).longValue();
        return new DNACounts(mutants, humans);
    }
}
