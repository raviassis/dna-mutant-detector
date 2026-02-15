package org.magneto.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.magneto.api.dtos.DNADto;
import org.magneto.core.DNADetector;

@Path("mutant")
public class MutantController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response mutant(DNADto dto) {
        if (DNADetector.isMutant(dto.dna)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
