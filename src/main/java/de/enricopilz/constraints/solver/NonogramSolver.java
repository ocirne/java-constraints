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
            setMaximalOuterRight(innerLeft + length);
        }

        public int getInnerRight() {
            return outerLeft + length;
        }

        public void setInnerRight(int innerRight) {
            setMinimalOuterLeft(innerRight - length);
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

    abstract class Line {
        // Zahlen sind in einer doppelt verketteten Liste hinterlegt (pro Linie), Alternative: LinkedList
        private BlackArea[] bas;

        private final int size;

        public Line(int[] numbers, int size) {
            this.size = size;
            bas = new BlackArea[numbers.length];
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
            useTechniqueSimpleBoxes();
        }

        // initialization
        public void useTechniqueSimpleBoxes() {
            if (bas.length == 0) {
                return;
            }
            bas[0].setMinimalOuterLeft(0);
            bas[bas.length - 1].setMaximalOuterRight(size);
        }

        public void solveLine() {
            useTechniqueForcing();
            useTechniqueGlueing();

            setDefiniteBlack();
            setDefiniteWhite();
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
                if (getResult(i) == WHITE) {
                    return false;
                }
            }
            return true;
        }

        private boolean isPossibleEnd(BlackArea ba, int end) {
            for (int i = end; i > end - ba.getLength(); i--) {
                if (getResult(i - 1) == WHITE) {
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
                if (getResult(i) == BLACK) {
                    ba.setInnerLeft(i);
                    return;
                }
            }
        }

        private void glueingFromRight(BlackArea ba) {
            // von oben - TODO ist vermutlich noch fehlerhaft
            for (int i = ba.getOuterRight(); i > ba.getInnerRight(); i--) {
                if (getResult(i - 1) == BLACK) {
                    ba.setInnerRight(i);
                    return;
                }
            }
        }


        private void setDefiniteWhite() {
            // Ermittle in tmp alles, was nicht durch Schwarz erreicht werden kann
            char[] tmp = new char[size];
            Arrays.fill(tmp, UNKNOWN);
            for (BlackArea ba : bas) {
//                System.err.println(ba.getOuterLeft() + "-" + ba.getOuterRight());
                for (int i = ba.getOuterLeft(); i < ba.getOuterRight(); i++) {
                    tmp[i] = BLACK;
                }
            }
//            System.err.println(Arrays.toString(tmp));
            // Alles, was nicht erreicht wird, muss weiß sein
            for (int i = 0; i < size; i++) {
                if (tmp[i] == UNKNOWN) {
                    setResult(i, WHITE);
                }
            }
        }

        private void setDefiniteBlack() {
            for (BlackArea ba : bas) {
                for (int i = ba.getInnerLeft(); i < ba.getInnerRight(); i++) {
                    setResult(i, BLACK);
                }
            }
        }

        public abstract char getResult(int i);

        public abstract void setResult(int i, char value);
    }

    class Row extends Line {
        private final int y;

        public Row(int[] numbers, int size, int y) {
            super(numbers, size);
            this.y = y;
        }

        @Override
        public char getResult(int x) {
            return result[x][y];
        }

        @Override
        public void setResult(int x, char value) {
            result[x][y] = value;
        }
    }

    class Col extends Line {
        private final int x;

        public Col(int[] numbers, int size, int x) {
            super(numbers, size);
            this.x = x;
        }

        @Override
        public char getResult(int y) {
            return result[x][y];
        }

        @Override
        public void setResult(int y, char value) {
            //          System.err.println(y + ": " + value);
            result[x][y] = value;
        }
    }

    public class GenericLine extends NonogramSolver.Line {

        public GenericLine(int[] numbers, int size) {
            super(numbers, size);
        }

        @Override
        public char getResult(int i) {
            return testResult[i];
        }

        @Override
        public void setResult(int i, char value) {
            testResult[i] = value;
        }
    }

    private Line[] rows;
    private Line[] cols;

    private char[][] result;

    private Line genericLine;
    private char[] testResult;

    public String solveGenericLine(String input, int[] numbers) {
        initializeGenericVariables(input, numbers);
        genericSolve();
        return String.valueOf(testResult);
    }

    private void initializeGenericVariables(final String input, final int[] numbers) {
        this.genericLine = new GenericLine(numbers, input.length());
        this.testResult = input.toCharArray();
        Arrays.fill(testResult, UNKNOWN);
    }

    public String solve(final int[][] rowNumbers, final int[][] colNumbers) {
        initializeVariables(rowNumbers, colNumbers);
        solve();
        return extractResultAsString();
    }

    private void initializeVariables(final int[][] rowNumbers, final int[][] colNumbers) {
        this.rows = new Line[rowNumbers.length];
        for (int y = 0; y < rowNumbers.length; y++) {
            this.rows[y] = new Row(rowNumbers[y], colNumbers.length, y);
        }
        this.cols = new Line[colNumbers.length];
        for (int x = 0; x < colNumbers.length; x++) {
            this.cols[x] = new Col(colNumbers[x], rowNumbers.length, x);
        }
        this.result = new char[colNumbers.length][rowNumbers.length];
        for (int x = 0; x < colNumbers.length; x++) {
            for (int y = 0; y < rowNumbers.length; y++) {
                result[x][y] = UNKNOWN;
            }
        }
    }

    private String extractResultAsString() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < cols.length; x++) {
                builder.append(result[x][y]);
            }
            // TODO System.getProperty("line.separator")
            builder.append('\n');
        }
        return builder.toString();
    }

    private void solve() {
        for (int i = 0; i < 1; i++) {
            for (Line row : rows) {
                row.solveLine();
            }
            for (Line col : cols) {
                col.solveLine();
            }
        }
    }

    private void genericSolve() {
        genericLine.solveLine();
    }
}
