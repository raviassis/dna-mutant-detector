package org.magneto.api;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.magneto.api.dtos.DNADto;
import org.magneto.services.DNAService;

import java.security.NoSuchAlgorithmException;

@Path("mutant")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MutantController {

    @Inject
    DNAService dnaService;

    // async persists
    @POST
    @Blocking
    public Response mutant(DNADto dto) throws NoSuchAlgorithmException {
        var dna = dnaService.analyse(dto.dna);
        if (dna.mutant) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
