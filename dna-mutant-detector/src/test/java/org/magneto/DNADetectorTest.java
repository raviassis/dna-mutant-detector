package org.magneto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

public class DNADetectorTest {
    @ParameterizedTest
    @MethodSource("mutantSamples")
    void shouldReturnTrueForMutants(String[] dna) {
        assertTrue(DNADetector.isMutant(dna));
    }

    @ParameterizedTest
    @MethodSource("humanSamples")
    void shouldReturnFalseForHumans(String[] dna) {
        assertFalse(DNADetector.isMutant(dna));
    }

    @ParameterizedTest
    @MethodSource("invalidCharacterSamples")
    void shouldThrowForInvalidCharacter(String[] dna) {
        assertThrows(IllegalArgumentException.class, () -> DNADetector.isMutant(dna));
    }

    @Test
    void shouldThrowWhenDnaIsNull() {
        assertThrows(IllegalArgumentException.class, () -> DNADetector.isMutant(null));
    }

    @Test
    void shouldThrowWhenAnyRowIsNull() {
        String[] dna = {
                "ATGCGA",
                null,
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        assertThrows(IllegalArgumentException.class, () -> DNADetector.isMutant(dna));
    }

    @Test
    void shouldCountOverlapping() {
        String[] dna = {
                "AAAAAG",
                "ATGCGA",
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        assertTrue(DNADetector.isMutant(dna));
    }

    private static Stream<Arguments> mutantSamples() {
        return Stream.of(
                // 2 horizontal sequences
                arguments((Object) new String[]{
                        "AAAAGA",
                        "AAAAGC",
                        "TTATGT",
                        "AGAAGG",
                        "CCCCTA",
                        "TCACTG"
                }),
                // 2 vertical sequences
                arguments((Object) new String[]{
                        "AAGCGA",
                        "AAGTGC",
                        "AAATGT",
                        "AAAAGG",
                        "CCCCTA",
                        "TCACTG"
                }),
                // 2 diagonal TL-BR sequences
                arguments((Object) new String[]{
                        "ATGCGA",
                        "CATTGC",
                        "TTATGT",
                        "AGAATG",
                        "CCCCTA",
                        "TCACTG"
                }),
                // 2 diagonal TR-BL sequences
                arguments((Object) new String[]{
                        "ATGCTA",
                        "CAGTAC",
                        "TATAGT",
                        "ATAAGG",
                        "CCCCTA",
                        "TCACTG"
                }),
                arguments((Object) new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGAAGG",
                        "CCCCTA",
                        "TCACTG"
                }),
                arguments((Object) new String[]{
                        "ATGCAA",
                        "ATGTGC",
                        "ATATGT",
                        "ATACGG",
                        "GCGTCA",
                        "TCACTG"
                })
        );
    }

    private static Stream<Arguments> humanSamples() {
        return Stream.of(
                arguments((Object) new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATTT",
                        "AGACGG",
                        "GCGTCA",
                        "TCACTG"
                }),
                arguments((Object) new String[]{
                        "ATGCGA",
                        "CAGTAC",
                        "TTATGT",
                        "AGACGG",
                        "GCGTCA",
                        "TCACTG"
                })
        );
    }

    private static Stream<Arguments> invalidCharacterSamples() {
        return Stream.of(
                arguments((Object) new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATXT",
                        "AGACGG",
                        "GCGTCA",
                        "TCACTG"
                }),
                arguments((Object) new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGACGG",
                        "GCG1CA",
                        "TCACTG"
                })
        );
    }
}
