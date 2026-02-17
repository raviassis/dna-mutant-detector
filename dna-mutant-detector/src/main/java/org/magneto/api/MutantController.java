package org.magneto.api;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
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

    @POST
    public Response mutant(DNADto dto) throws NoSuchAlgorithmException {
        var dna = dnaService.analyse2(dto.dna);
        if (dna.mutant) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
