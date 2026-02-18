package org.magneto.api;

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
    public Response stats() {
        return Response.ok(
                StatsDto.fromEntity(dnaService.stats())
        ).build();
    }
}
