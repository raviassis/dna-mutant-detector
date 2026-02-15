package org.magneto.core;

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
    @MethodSource("smallSizeSamples")
    void shouldAcceptAnySizeMatrix(String[] dna) {
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
                }),
                // 7x7 with two horizontal sequences
                arguments((Object) new String[]{
                        "AAAATCG",
                        "CAGTGCA",
                        "TTATGTA",
                        "AGACGGT",
                        "CCCCGTA",
                        "TCACTGA",
                        "GACTACT"
                }),
                // 8x8 with two horizontal sequences
                arguments((Object) new String[]{
                        "ATCGATCG",
                        "CAGTGCAT",
                        "TTATGTAA",
                        "AAAACTGT",
                        "GCGTCATC",
                        "TCACTGAC",
                        "CCCCAGTT",
                        "GACTACTG"
                }),
                // 4x6 with two horizontal sequences
                arguments((Object) new String[]{
                        "AAAATA",
                        "CAGTGC",
                        "TTTTGT",
                        "AGACGG"
                }),
                // 6x4 with repeated vertical sequences
                arguments((Object) new String[]{
                        "ATGC",
                        "ATGC",
                        "ATGC",
                        "ATGC",
                        "CAGT",
                        "TGCA"
                }),
                // 7x8 with two horizontal sequences
                arguments((Object) new String[]{
                        "AAAATGCA",
                        "CAGTGCAT",
                        "TTATGTAA",
                        "AGACGGTT",
                        "CCCCATGA",
                        "TCACTGAC",
                        "GACTACTG"
                }),
                // 9x7 with two horizontal sequences
                arguments((Object) new String[]{
                        "ATGCGTA",
                        "CAGTGCA",
                        "TTATGTA",
                        "AAAACGT",
                        "GCGTCAT",
                        "TCACTGA",
                        "CCCCAGT",
                        "GACTACT",
                        "ATCGATC"
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
                }),
                // 4x6 without mutant sequences
                arguments((Object) new String[]{
                        "ATGCGA",
                        "CAGTAC",
                        "TTATGT",
                        "AGACGG"
                }),
                // 6x4 without mutant sequences
                arguments((Object) new String[]{
                        "ATGC",
                        "CAGT",
                        "TTAT",
                        "AGAC",
                        "GCGT",
                        "TCAA"
                }),
                // 7x7 without mutant sequences
                arguments((Object) new String[]{
                        "ATCGATC",
                        "TAGCTAG",
                        "CGTACGT",
                        "CAATGGA",
                        "ATCAATC",
                        "TAGCTAG",
                        "CGTACGT"
                }),
                // 8x8 without mutant sequences
                arguments((Object) new String[]{
                        "ATAGATCG",
                        "TAGTTAGC",
                        "CGTACGTA",
                        "ACATTCAT",
                        "TCCATACG",
                        "AGCATGCA",
                        "CTAGCTAG",
                        "GTCAGTCA"
                }),
                // 8x7 without mutant sequences
                arguments((Object) new String[]{
                        "ATCGATC",
                        "TAGCTAG",
                        "CGTACGT",
                        "CCATGAA",
                        "TACATAC",
                        "TGCATGC",
                        "CTAGCTA",
                        "GTCAGTC"
                }),
                // 7x9 without mutant sequences
                arguments((Object) new String[]{
                        "ATAGATCGA",
                        "TAGTTAGCT",
                        "CGTACGTAC",
                        "CCATACATG",
                        "TACGTACGT",
                        "AGCATGCAT",
                        "CTAGCTAGC"
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


    private static Stream<Arguments> smallSizeSamples() {
        return Stream.of(
                arguments((Object) new String[]{
                        "A"
                }),
                arguments((Object) new String[]{
                        "ATC",
                        "GTA"
                }),
                arguments((Object) new String[]{
                        "AT",
                        "CG",
                        "TA"
                }),
                arguments((Object) new String[]{
                        "ATC",
                        "GTA",
                        "CGA"
                })
        );
    }
}
