package org.magneto.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.magneto.services.DNAService;
import org.magneto.services.DNAServiceAsync;

@Path("stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsController {

    @Inject
    DNAService dnaService;

    @GET
    public Response stats() {
        return Response.ok(dnaService.stats()).build();
    }
}
