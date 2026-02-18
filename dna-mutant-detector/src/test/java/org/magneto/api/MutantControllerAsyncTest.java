package org.magneto.api;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.magneto.api.dtos.DNADto;
import org.magneto.entities.DNAEntity;
import org.magneto.exceptions.ValidationException;
import org.magneto.services.DNAServiceAsync;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
class MutantControllerAsyncTest {

    @InjectMock
    DNAServiceAsync dnaServiceAsync;

    @Test
    void shouldReturnOkWhenIsMutant() throws Exception {
        DNADto dto = new DNADto();
        dto.dna = new String[]{"ATGCGA"};
        DNAEntity analysed = new DNAEntity();
        analysed.mutant = true;
        when(dnaServiceAsync.analyse(dto.dna)).thenReturn(analysed);

        given()
                .body(dto).contentType(ContentType.JSON)
                .when().post("/mutant-async")
                .then().statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void shouldReturnForbiddenForHumans() throws Exception {
        DNADto dto = new DNADto();
        dto.dna = new String[]{"ATGCGA"};
        DNAEntity analysed = new DNAEntity();
        analysed.mutant = false;
        when(dnaServiceAsync.analyse(dto.dna)).thenReturn(analysed);

        given()
                .body(dto).contentType(ContentType.JSON)
                .when().post("/mutant-async")
                .then().statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        DNADto dto = new DNADto();
        dto.dna = new String[]{"ATGCGA"};
        when(dnaServiceAsync.analyse(dto.dna))
                .thenThrow(new ValidationException("DNA rows must not have different lengths"));

        given()
                .body(dto).contentType(ContentType.JSON)
                .when().post("/mutant-async")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("message", equalTo("DNA rows must not have different lengths"));
    }

    @Test
    void shouldReturnInternalServerErrorForUnexpectedFailures() throws Exception {
        DNADto dto = new DNADto();
        dto.dna = new String[]{"ATGCGA"};
        when(dnaServiceAsync.analyse(dto.dna)).thenThrow(new RuntimeException("Unexpected error"));

        given()
                .body(dto).contentType(ContentType.JSON)
                .when().post("/mutant-async")
                .then().statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }
}
