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
    static class BlackArea {

        private final int length;

        private int outerLeft;
        private int outerRight;

        private BlackArea left;
        private BlackArea right;

        BlackArea(final int length) {
            this.length = length;
            outerLeft = Integer.MIN_VALUE;
            outerRight = Integer.MAX_VALUE;
        }

        public void setLeft(final BlackArea left) {
            this.left = left;
        }

        public void setRight(final BlackArea right) {
            this.right = right;
        }

        public int getOuterLeft() {
            return outerLeft;
        }

        public int getOuterRight() {
            return outerRight;
        }

        public int getInnerLeft() {
            return outerRight - length;
        }

        public void setInnerLeft(int innerLeft) {
            outerRight = innerLeft + length;
        }

        public int getInnerRight() {
            return outerLeft + length;
        }

        public void setInnerRight(int innerRight) {
            outerLeft = innerRight - length;
        }

        public void setMinimalOuterLeft(int newOuterLeft) {
            if (newOuterLeft <= outerLeft) {
                return;
            }
            outerLeft = newOuterLeft;
            if (right != null) {
                right.setMinimalOuterLeft(outerLeft + length + 1);
            }
        }

        public void setMaximalOuterRight(int newOuterRight) {
            if (newOuterRight >= outerRight) {
                return;
            }
            outerRight = newOuterRight;
            if (left != null) {
                left.setMaximalOuterRight(outerRight - length - 1);
            }
        }

        public int getLength() {
            return length;
        }
    }

    // Zahlen sind in einer doppelt verketteten Liste hinterlegt (pro Linie), Alternative: LinkedList
    private BlackArea[] bas;

    private char[] output;

    public String solveLine(String input, int[] numbers) {
        this.bas = new BlackArea[numbers.length];
        this.output = input.toCharArray();
        for (int n = 0; n < numbers.length; n++) {
            bas[n] = new BlackArea(numbers[n]);
        }
        for (int n = 0; n < numbers.length; n++) {
            if (n > 0) {
                bas[n].setLeft(bas[n - 1]);
            }
            if (n < numbers.length - 1) {
                bas[n].setRight(bas[n + 1]);
            }
        }
        return String.valueOf(solveLine());
    }

    public char[] solveLine() {
        char[] tmp;
        System.out.println("Urzustand:");
        printState();
        System.out.println("Initialization:");
        useTechniqueSimpleBoxes();
        printState();
        do {
            tmp = output.clone();
            useTechniqueForcing();
            useTechniqueGlueing();

            setDefiniteBlack();
            setDefiniteWhite();
            System.out.println("Loop:");
            printState();
        } while (!Arrays.equals(tmp, output));

        return output;
    }

    private void printState() {
        System.out.println("'" + String.valueOf(output) + "'");
        for (BlackArea ba : bas) {
            System.out.println("zahl: " + ba.length + "," +
                    " von " + ba.getOuterLeft() + "/" + ba.getInnerLeft() +
                    " bis " + ba.getInnerRight() + "/" + ba.getOuterRight());
        }
        System.out.println();
    }

    // initialization
    public void useTechniqueSimpleBoxes() {
        if (bas.length == 0) {
            return;
        }
        bas[0].setMinimalOuterLeft(0);
        bas[bas.length-1].setMaximalOuterRight(output.length);
    }

    public void useTechniqueForcing() {
        for (BlackArea ba : bas) {
            increaseFirstPossibleStart(ba);
            decreaseLastPossibleEnding(ba);
        }
    }

    private void increaseFirstPossibleStart(BlackArea ba) {
        // TODO Optimierungsmöglichkeiten
        for (int start = ba.getOuterLeft(); start <= ba.getInnerLeft(); start++) {
            if (isPossibleStart(ba, start)) {
                ba.setMinimalOuterLeft(start);
                return;
            }
        }
    }

    private void decreaseLastPossibleEnding(BlackArea ba) {
        for (int end = ba.getOuterRight(); end >= ba.getInnerRight(); end--) {
            if (isPossibleEnd(ba, end)) {
                ba.setMaximalOuterRight(end);
                return;
            }
        }
    }

    private boolean isPossibleStart(BlackArea ba, int start) {
        for (int i = start; i < start + ba.getLength(); i++) {
            if (output[i] == WHITE) {
                return false;
            }
        }
        return true;
    }

    private boolean isPossibleEnd(BlackArea ba, int end) {
        for (int i = end; i > end - ba.getLength(); i--) {
            if (output[i - 1] == WHITE) {
                return false;
            }
        }
        return true;
    }

    private void useTechniqueGlueing() {
        for (BlackArea ba : bas) {
            glueingFromLeft(ba);
        }
        for (int n = bas.length - 1; n >= 0; n--) {
            glueingFromRight(bas[n]);
        }
    }

    private void glueingFromLeft(BlackArea ba) {
        // von unten - TODO ist vermutlich noch fehlerhaft
        for (int i = ba.getOuterLeft(); i < ba.getInnerLeft(); i++) {
            if (output[i] == BLACK) {
                ba.setInnerLeft(i);
                return;
            }
        }
    }

    private void glueingFromRight(BlackArea ba) {
        // von oben - TODO ist vermutlich noch fehlerhaft
        for (int i = ba.getOuterRight(); i > ba.getInnerRight(); i--) {
            if (output[i - 1] == BLACK) {
                ba.setInnerRight(i);
                return;
            }
        }
    }

    private void setDefiniteWhite() {
        // Ermittle in tmp alles, was nicht durch Schwarz erreicht werden kann
        char[] tmp = new char[output.length];
        Arrays.fill(tmp, UNKNOWN);
        for (BlackArea ba : bas) {
            for (int i = ba.outerLeft; i < ba.getOuterRight(); i++) {
                tmp[i] = BLACK;
            }
        }
        // Alles, was nicht erreicht wird, muss weiß sein
        for (int i = 0; i < output.length; i++) {
            if (tmp[i] == UNKNOWN) {
                output[i] = WHITE;
            }
        }
    }

    private void setDefiniteBlack() {
        for (BlackArea ba : bas) {
            for (int i = ba.getInnerLeft(); i < ba.getInnerRight(); i++) {
                output[i] = BLACK;
            }
        }
    }
}
