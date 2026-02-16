package org.magneto.api;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.magneto.api.dtos.DNADto;
import org.magneto.api.dtos.ErrorResponse;
import org.magneto.services.DNAService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldReturnBadRequest() {
        var dto = new DNADto();
        dto.dna = new String[]{
                "ATGCGA",
                "CAGTG",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        given()
            .body(dto).contentType(ContentType.JSON)
                .when().post("/mutant")
                .then()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", equalTo("DNA rows must not have different lengths"));
    }

    @Nested
    class MutantControllerMockedTest {
        @InjectMock
        DNAService dnaService;
        @Test
        void shouldReturnInternalServerError() {
        var dto = new DNADto();
        var expected = new ErrorResponse("Error Test");
        when(dnaService.stats()).thenThrow(new RuntimeException(expected.message));
        given()
                .body(dto).contentType(ContentType.JSON)
                .when().post("/mutant")
                .then().assertThat()
                    .statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

}
