package org.magneto.api;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.magneto.api.dtos.StatsDto;
import org.magneto.services.DNAService;
import org.magneto.services.DNAServiceAsync;

@Path("stats-async")
@Produces(MediaType.APPLICATION_JSON)
public class StatsControllerAsync {

    @Inject
    DNAServiceAsync dnaService;

    @GET
    public Uni<Response> stats() {
        return dnaService.stats()
                .onItem()
                .transform(
                stats -> Response.ok(
                        StatsDto.fromEntity(stats)
                    ).build()
                );
    }
}
