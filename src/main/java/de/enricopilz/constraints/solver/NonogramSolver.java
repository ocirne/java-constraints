package de.enricopilz.constraints.solver;

import java.util.Arrays;

public class NonogramSolver {

    private static final char UNKNOWN = '.';
    private static final char BLACK = '#';
    private static final char WHITE = ' ';

    /**
     * 01234
     * ###..
     * .###.
     * ..###
     * ->
     * ..#..
     *
     * 3 in einer 5 Linie bedeutet:
     * - Anfang ist mindestens bei 0 (outerLeft), höchstens bei 2 (innerLeft)
     * - Ende ist mindestens vor 3 (innerRight), höchstens vor 5 (outerRight)
     */
    int[] nu;
    int[] outerLeft;
    int[] innerLeft;
    int[] innerRight;
    int[] outerRight;
    char[] output;

    public String solveLine(String input, int[] numbers) {
        this.nu = numbers;
        outerLeft = new int[numbers.length];
        innerLeft = new int[numbers.length];
        innerRight = new int[numbers.length];
        outerRight = new int[numbers.length];
        this.output = input.toCharArray();
        return String.valueOf(solveLine());
    }

    public char[] solveLine() {
        useTechniqueSimpleBoxes();
        useTechniqueSimpleSpaces();
        useTechniqueForcing();
        useTechniqueGlueing();
        useTechniqueMercury();

        return output;
    }

    public void useTechniqueSimpleBoxes() {
        if (nu.length == 0) {
            return;
        }
        // untergrenzen
        outerLeft[0] = 0;
        innerRight[0] = outerLeft[0] + nu[0];
        for (int i = 1; i < nu.length; i++) {
            outerLeft[i] = outerLeft[i - 1] + nu[i - 1] + 1;
            innerRight[i] = outerLeft[i] + nu[i];
        }
        // obergrenzen
        outerRight[nu.length - 1] = output.length;
        innerLeft[nu.length - 1] = outerRight[nu.length-1] - nu[0];
        for (int i = nu.length - 2; i >= 0; i--) {
            outerRight[i] = outerRight[i + 1] - nu[i + 1] - 1;
            innerLeft[i] = outerRight[i] - nu[i];
        }
        for (int i = 0; i < nu.length; i++) {
            System.out.println("index: " + i + ", zahl: " + nu[i] + "," +
                    " von " + outerLeft[i] + "/" + innerLeft[i] +
                    " bis " + innerRight[i] + "/" + outerRight[i]);
            if (innerRight[i] - innerLeft[i] > nu[i]) {
                throw new IllegalArgumentException("nicht möglich");
            }
            for (int j = innerLeft[i]; j < innerRight[i]; j++) {
                output[j] = BLACK;
            }
        }
    }

    public void useTechniqueForcing() {

    }

    private void useTechniqueGlueing() {

    }

    private void useTechniqueMercury() {

    }

    private void useTechniqueSimpleSpaces() {
        // shortcut for convenience
        if (nu.length == 0) {
            Arrays.fill(output, WHITE);
            return;
        }
        // Ermittle alles, was nicht durch Schwarz erreicht werden kann
        char[] tmp = new char[output.length];
        Arrays.fill(tmp, UNKNOWN);
        for (int i = 0; i < nu.length; i++) {
            for (int j = outerLeft[i]; j < outerRight[i]; j++) {
                tmp[j] = BLACK;
            }
        }
        // Alles, was nicht erreicht wird, muss weiß sein
        for (int j = 0; j < output.length; j++) {
            if (tmp[j] == UNKNOWN) {
                output[j] = WHITE;
            }
        }
    }
}
