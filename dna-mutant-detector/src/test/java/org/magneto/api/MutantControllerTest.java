package org.magneto.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.magneto.api.dtos.DNADto;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MutantControllerTest {

    @Test
    void shouldReturnOkWhenIsMutant() {
        var dto = new DNADto();
        dto.dna = new String[]{
                "AAAAGA",
                "AAAAGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        given()
            .body(dto).contentType(ContentType.JSON)
            .when().post("/mutant")
            .then().statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    void shouldReturnForbiddenForHumans() {
        var dto = new DNADto();
        dto.dna = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        given()
                .body(dto).contentType(ContentType.JSON)
                .when().post("/mutant")
                .then().statusCode(Response.Status.FORBIDDEN.getStatusCode());

    }
}
