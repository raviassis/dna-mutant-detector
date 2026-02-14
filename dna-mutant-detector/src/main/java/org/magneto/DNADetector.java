package org.magneto;

public class DNADetector {
    private static int SEQUENCE_LENGTH = 4;
    public static boolean isMutant(String[] dna) {
        if (dna == null) {
            throw new IllegalArgumentException("DNA must not be null");
        }
        var found = 0;
        for (int i = 0; i < dna.length; i++) {
            var row = dna[i];
            if (row == null) {
                throw new IllegalArgumentException("DNA row must not be null");
            }

            for (int j = 0; j < row.length(); j++) {
                char nucleotide = row.charAt(j);
                // valid every nucleotide
                validNucleotide(nucleotide);

                // skip mutant test
                if (found > 1) continue;

                // check horizontally
                if (checkHorizontally(dna, i, j)) found++;

                // check vertically
                if (checkVertically(dna, i, j)) found++;

                // check diagonal tl-br
                if (checkDiagonallyTL_BR(dna, i, j)) found++;

                // check diagonal tr-bl
                if (checkDiagonallyTR_BL(dna, i, j)) found++;

            }
        }

        return found > 1;
    }

    private static boolean checkDiagonallyTR_BL(String[] dna, int i, int j) {
        if (i > dna.length - SEQUENCE_LENGTH || j > dna[i].length() - SEQUENCE_LENGTH) return false;
        char a = getN(dna, i+3, j);
        char b = getN(dna, i+2, j+1);
        char c = getN(dna, i+1, j+2);
        char d = getN(dna, i, j+3);
        return a == b && a == c && a == d;
    }

    private static boolean checkDiagonallyTL_BR(String[] dna, int i, int j) {
        if (i > dna.length - SEQUENCE_LENGTH || j > dna[i].length() - SEQUENCE_LENGTH) return false;
        char a = getN(dna, i, j);
        char b = getN(dna, i+1, j+1);
        char c = getN(dna, i+2, j+2);
        char d = getN(dna, i+3, j+3);
        return a == b && a == c && a == d;
    }

    private static boolean checkVertically(String[] dna, int i, int j) {
        if (i > dna.length - SEQUENCE_LENGTH) return false;
        char a = getN(dna, i, j);
        char b = getN(dna, i+1, j);
        char c = getN(dna, i+2, j);
        char d = getN(dna, i+3, j);
        return a == b && a == c && a == d;
    }

    private static boolean checkHorizontally(String[] dna, int i, int j) {
        if (j > dna[i].length() - SEQUENCE_LENGTH) return false;
        char a = getN(dna, i, j);
        char b = getN(dna, i, j+1);
        char c = getN(dna, i, j+2);
        char d = getN(dna, i, j+3);
        return a == b && a == c && a == d;
    }


    static char getN(String[] dna, int i, int j) {
        if (dna == null) {
            throw new IllegalArgumentException("DNA must not be null");
        }
        var row = dna[i];
        if (row == null) {
            throw new IllegalArgumentException("DNA row must not be null");
        }
        return row.charAt(j);
    }

    static void validNucleotide(char nucleotide) {
        if (
                nucleotide != 'A' &&
                nucleotide != 'T' &&
                nucleotide != 'C' &&
                nucleotide != 'G'
        ) {
            throw new IllegalArgumentException(
                    "Invalid DNA character: " + nucleotide + ". Allowed characters are A, T, C, G."
            );
        }
    }
}
