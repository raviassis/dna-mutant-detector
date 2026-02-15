package org.magneto.api;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.magneto.api.dtos.StatsDto;
import org.magneto.services.DNAService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
class StatsControllerTest {

    @InjectMock
    DNAService dnaService;

    @Test
    void shouldReturnOkWithStats() {
        StatsDto expected = new StatsDto();
        expected.CountMutantDNA = 40L;
        expected.CountHumanDNA = 10L;
        expected.Ratio = 4.0;
        when(dnaService.stats()).thenReturn(expected);

        given()
                .when().get("/stats")
                .then()
                .statusCode(200)
                .body("count_mutant_dna", equalTo(40))
                .body("count_human_dna", equalTo(10))
                .body("ratio", equalTo(4.0f));
    }
}
