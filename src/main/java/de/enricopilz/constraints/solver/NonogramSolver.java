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
     * <p>
     * 3 in einer 5 Linie bedeutet:
     * - Anfang ist mindestens bei 0 (outerLeft), höchstens bei 2 (innerLeft)
     * - Ende ist mindestens vor 3 (innerRight), höchstens vor 5 (outerRight)
     */
    private int[] nu;
    private int[] outerLeft;
    private int[] innerLeft;
    private int[] innerRight;
    private int[] outerRight;
    private char[] output;

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
        char[] tmp;
        printState();
        useTechniqueSimpleBoxes();
        printState();
        do {
            tmp = output.clone();
            useTechniqueForcing();
            useTechniqueGlueing();
            useTechniqueMercury();

            setDefiniteBlack();
            setDefiniteWhite();
            printState();
        } while (!Arrays.equals(tmp, output));

        return output;
    }

    private void printState() {
        System.out.println("'" + String.valueOf(output) + "'");
        for (int n = 0; n < nu.length; n++) {
            System.out.println("index: " + n + ", zahl: " + nu[n] + "," +
                    " von " + outerLeft[n] + "/" + innerLeft[n] +
                    " bis " + innerRight[n] + "/" + outerRight[n]);
        }
        System.out.println();
    }

    public void useTechniqueSimpleBoxes() {
        if (nu.length == 0) {
            return;
        }
        // untergrenzen
        outerLeft[0] = 0;
        innerRight[0] = outerLeft[0] + nu[0];
        for (int n = 1; n < nu.length; n++) {
            outerLeft[n] = outerLeft[n - 1] + nu[n - 1] + 1;
            innerRight[n] = outerLeft[n] + nu[n];
        }
        // obergrenzen
        outerRight[nu.length - 1] = output.length;
        innerLeft[nu.length - 1] = outerRight[nu.length-1] - nu[0];
        for (int n = nu.length - 2; n >= 0; n--) {
            outerRight[n] = outerRight[n + 1] - nu[n + 1] - 1;
            innerLeft[n] = outerRight[n] - nu[n];
        }
    }

    public void useTechniqueForcing() {
        for (int n = 0; n < nu.length; n++) {
            increaseFirstPossibleStart(n);
            decreaseLastPossibleEnding(n);
        }
    }

    private void increaseFirstPossibleStart(int n) {
        for (int start = outerLeft[n]; start <= innerLeft[n]; start++) {
            if (isPossibleStart(n, start)) {
                outerLeft[n] = start;
                innerRight[n] = outerLeft[n] + nu[n];
                return;
            }
        }
    }

    private void decreaseLastPossibleEnding(int n) {
        for (int end = outerRight[n]; end >= innerRight[n]; end--) {
            if (isPossibleEnd(n, end)) {
                outerRight[n] = end;
                innerLeft[n] = outerRight[n] - nu[n];
                return;
            }
        }
    }

    private boolean isPossibleStart(int n, int start) {
        for (int i = start; i < start + nu[n]; i++) {
            if (output[i] == WHITE) {
                return false;
            }
        }
        return true;
    }

    private boolean isPossibleEnd(int n, int end) {
        for (int i = end; i > end - nu[n]; i--) {
            if (output[i-1] == WHITE) {
                return false;
            }
        }
        return true;
    }

    private void useTechniqueGlueing() {
        // von unten - TODO noch nicht präzise definiert
        for (int n = 0; n < nu.length; n++) {
            for (int i = outerLeft[n]; i < innerLeft[n]; i++) {
                if (output[i] == BLACK) {
                    innerLeft[n] = i;
                    outerRight[n] = i + nu[n];
                }
            }
        }
        // von oben - TODO noch nicht präzise definiert
        for (int n = nu.length - 1; n >= 0; n--) {
            for (int i = outerRight[n]; i > innerRight[n]; i--) {
                if (output[i - 1] == BLACK) {
                    innerRight[n] = i;
                    outerLeft[n] = i - nu[n];
                }
            }
        }
    }

    private void useTechniqueMercury() {
        for (int i = 0; i < output.length; i++) {
            if (output[i] == BLACK) {

            }
        }
    }

    private void setDefiniteWhite() {
        // shortcut for convenience
        if (nu.length == 0) {
            Arrays.fill(output, WHITE);
            return;
        }
        // Ermittle alles, was nicht durch Schwarz erreicht werden kann
        char[] tmp = new char[output.length];
        Arrays.fill(tmp, UNKNOWN);
        for (int n = 0; n < nu.length; n++) {
            for (int j = outerLeft[n]; j < outerRight[n]; j++) {
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

    private void setDefiniteBlack() {
        // Sicher schwarze setzen
        for (int i = 0; i < nu.length; i++) {
            if (innerRight[i] - innerLeft[i] > nu[i]) {
                throw new IllegalArgumentException("nicht möglich");
            }
            for (int j = innerLeft[i]; j < innerRight[i]; j++) {
                output[j] = BLACK;
            }
        }
    }
}
