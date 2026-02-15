package org.magneto.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.magneto.entities.DNAEntity;

@ApplicationScoped
public class DNARepository implements PanacheRepository<DNAEntity> {
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
