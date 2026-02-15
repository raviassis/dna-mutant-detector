package org.magneto.core;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DNAHasherTest {

    @Test
    void shouldReturnExpectedSha256ForKnownDna() throws NoSuchAlgorithmException {
        String[] dna = {
                "ATGC",
                "CAGT"
        };

        String hash = DNAHasher.hash(dna);

        assertEquals("b0940eacad5f3bcc5df87b6b439ad0c950ee50ebf31ebdf359b0ec2acb947421", hash);
    }

    @Test
    void shouldReturnSameHashForSameDna() throws NoSuchAlgorithmException {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT"
        };

        String hash1 = DNAHasher.hash(dna);
        String hash2 = DNAHasher.hash(dna);

        assertEquals(hash1, hash2);
    }

    @Test
    void shouldReturnDifferentHashWhenDnaChanges() throws NoSuchAlgorithmException {
        String[] dna1 = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT"
        };
        String[] dna2 = {
                "ATGCGA",
                "CAGTGC",
                "TTATGA"
        };

        String hash1 = DNAHasher.hash(dna1);
        String hash2 = DNAHasher.hash(dna2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void shouldReturnDifferentHashWhenRowOrderChanges() throws NoSuchAlgorithmException {
        String[] dna1 = {
                "ATGC",
                "CAGT"
        };
        String[] dna2 = {
                "CAGT",
                "ATGC"
        };

        String hash1 = DNAHasher.hash(dna1);
        String hash2 = DNAHasher.hash(dna2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void shouldReturnExpectedHashForEmptyDna() throws NoSuchAlgorithmException {
        String[] dna = {};

        String hash = DNAHasher.hash(dna);

        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);
    }

    @Test
    void shouldThrowWhenDnaIsNull() {
        assertThrows(NullPointerException.class, () -> DNAHasher.hash(null));
    }
}
