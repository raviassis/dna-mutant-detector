//package org.magneto.infra.redis;
//
//import io.quarkus.redis.datasource.RedisDataSource;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//
////@ApplicationScoped
//public class DNACounterService {
//    private static final String MUTANT_KEY = "count_mutant_dna";
//    private static final String HUMAN_KEY = "count_human_dna";
//
//    //@Inject
//    RedisDataSource ds;
//
//    public void incrementMutants() {
//        ds.value(Long.class).incr(MUTANT_KEY);
//    }
//
//    public void incrementHumans() {
//        ds.value(Long.class).incr(HUMAN_KEY);
//    }
//
//    public long getCountMutants() {
//        return ds.value(Long.class).get(MUTANT_KEY);
//    }
//
//    public long getCountHumans() {
//        return ds.value(Long.class).get(HUMAN_KEY);
//    }
//}
