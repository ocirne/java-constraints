package de.enricopilz.constraints.solver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NonogramSolver2 {

    private static final char UNKNOWN = '.';
    private static final char BLACK = '#';
    private static final char WHITE = ' ';

    // ähnlich Variable, vielleicht zusammenlegen
    // Dann auch prüfen, ob man constraints-hopping einbauen kann
    static class BlackArea {

        private final int length;

        private List<Integer> possibilities;

        BlackArea(final int length, final List<Integer> possibilities) {
            this.length = length;
            this.possibilities = new ArrayList<>(possibilities);
        }

        public Optional<Integer> value() {
            return possibilities.size() == 1 ? Optional.of(possibilities.get(0)) : Optional.empty();
        }

        public List<Integer> getPossibilities() {
            return this.possibilities;
        }

        public void removePossibilities(final List<Integer> removals) {
            this.possibilities.removeAll(removals);
        }

        public int getLength() {
            return length;
        }

        @Override
        public String toString() {
            return "ba [length: " + length + ", possibilities: " + String.valueOf(possibilities) + "]";
        }

        public int maxValue() {
            return possibilities.stream().max(Integer::compareTo).orElseThrow();
        }

        public int minValue() {
            return possibilities.stream().min(Integer::compareTo).orElseThrow();
        }

        public void setMinValue(int newMinValue) {
            List<Integer> removals = new LinkedList<>();
            for (int p :possibilities) {
                if (p < newMinValue) {
                    removals.add(p);
                }
            }
            possibilities.removeAll(removals);
            if (possibilities.isEmpty()) {
                throw new IllegalStateException("Oops removed all values");
            }
        }

        public void setMaxValue(int newMaxValue) {
            List<Integer> removals = new LinkedList<>();
            for (int p :possibilities) {
                if (p > newMaxValue) {
                    removals.add(p);
                }
            }
            possibilities.removeAll(removals);
            if (possibilities.isEmpty()) {
                throw new IllegalStateException("Oops removed all values");
            }
        }

        public void makeSure(int makeSureValue) {
            setMaxValue(makeSureValue);
            setMinValue(makeSureValue - length + 1);
        }
    }

    class Constraint {
        BlackArea a;
        BlackArea b;
    }

    abstract class Line {
        // Zahlen sind in einer doppelt verketteten Liste hinterlegt (pro Linie), Alternative: LinkedList
        private BlackArea[] bas;

        private final int size;

        public Line(int[] numbers, int size) {
            this.size = size;
            bas = new BlackArea[numbers.length];
            for (int n = 0; n < numbers.length; n++) {
                bas[n] = new BlackArea(numbers[n], IntStream.range(0, size).boxed().collect(Collectors.toList()));
            }
            useTechniqueSimpleBoxes();
        }

        // initialization - wäre schon gut, um die initialen Maximalgrößen festzulegen
        // Andererseits kommt das auch rein, wenn die constraints angewendet werden
        public void useTechniqueSimpleBoxes() {
            if (bas.length == 0) {
                return;
            }
//            bas[0].setMinimalOuterLeft(0);
//            bas[bas.length - 1].setMaximalOuterRight(size);
        }

        public void solveLine() {
            for (BlackArea ba : bas) {
                adjustValues();
                removeNonMatching(ba);
                glueing();
            }
            setDefiniteBlack();
            setDefiniteWhite();
 //           System.err.println("Zwischenergebnis: ");
//            System.err.println(extractResultAsString());
        }

        // schiebt rechts und links, damit die Werte zusammenpassen
        private void adjustValues() {
            if (bas.length < 2) {
                return;
            }
            // from left
            System.err.println("from left");
            for (int b = 1; b < bas.length; b++) {
                System.err.println(bas[b]);
                BlackArea current = bas[b];
                BlackArea prev = bas[b-1];
                for (BlackArea ba : bas) {
                    System.err.println("DEBUG " + ba);
                }
                System.err.print("DEBUG ");
                for (int i = 0; i < size; i++) {
                    System.err.print(getResult(i));
                }
                System.err.println();
                int newMinValue = prev.minValue() + prev.getLength() + 1;
                current.setMinValue(newMinValue);
                System.err.println(newMinValue + " -> " + current);
            }
            // from right
            System.err.println("from right");
            for (int b = bas.length - 2; b >= 0; b--) {
                System.err.println(bas[b]);
                BlackArea current = bas[b];
                BlackArea next = bas[b+1];
                int newMaxValue = next.maxValue() - current.getLength() - 1;
                current.setMaxValue(newMaxValue);
                System.err.println(newMaxValue + " -> " + current);
            }
        }

        // matcht vorhandene schwarze felder mit Black Areas
        //
        private void glueing() {
            List<Set<BlackArea>> putter = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                putter.add(new HashSet<>());
            }
            for (BlackArea ba : bas) {
                for (int p : ba.possibilities) {
                    for (int i = p; i < p + ba.getLength(); i++) {
                        if (i < size) {
                            System.err.println("put in " + i + " ba " + ba);
                            putter.get(i).add(ba);
                        }
                    }
                }
            }
            for (int a = 0; a < size; a++) {
                System.err.println(a + " -> " + putter.get(a).size());
            }
            int i = 0;
            while (i < size) {
                if (putter.get(i).size() == 1 && getResult(i) == BLACK) {
                    BlackArea ba = putter.get(i).iterator().next();
                    while (i < size && getResult(i) == BLACK) {
                        System.err.println("make sure: " + i);
//                        System.err.println(extractResultAsString());
                        ba.makeSure(i);
                        System.err.println("--> " + ba);
                        i++;
                    }
                } else {
                    i++;
                }
            }
        }

        // guckt im Prinzip nur, welche Werte ausgeschlossen werden können,
        // weil in den Zielfeldern schon weiße Flächen sind
        private void removeNonMatching(final BlackArea blackArea) {
            // If already solved, then only check
            if (blackArea.value().isPresent()) {
                if (!matchPossible(blackArea, blackArea.value().get())) {
                    throw new IllegalStateException();
                }
                return;
            }
            // remove everything which doesn't match
            List<Integer> removals = new LinkedList<>();
            for (Integer value : blackArea.getPossibilities()) {
                System.err.println("check constraint value: " + value);
                if (!matchPossible(blackArea, value)) {
                    removals.add(value);
                }
            }
            // No more possibilities? Then a guess was wrong.
            if (blackArea.getPossibilities().size() == removals.size()) {
                return;
            }
            blackArea.removePossibilities(removals);
        }

        // Prüft, ob schwarze Area ba start eingebaut werden kann
        private boolean matchPossible(final BlackArea ba, final int start) {
            // kann später weggelassen werden, da das mit initial constraints erledigt wird
            if (start + ba.getLength() > size) {
                System.err.println("zu lang: " + ", start: " + start + ", length: " + ba.getLength() + ", size: " + size);
                return false;
            }
            for (int i = 0; i < ba.getLength(); i++) {
                if (getResult(start + i) == WHITE) {
                    return false;
                }
            }
            return true;
        }

        // naja
        private void setDefiniteWhite() {
            // Ermittle in tmp alles, was nicht durch Schwarz erreicht werden kann
            char[] tmp = new char[size];
            Arrays.fill(tmp, UNKNOWN);
            for (BlackArea ba : bas) {
                System.err.println(ba);
                for (int start : ba.getPossibilities()) {
                    for (int i = 0; i < ba.getLength(); i++) {
//                        System.err.println("could be black " + (start + i));
                        tmp[start + i] = BLACK;
                    }
                }
            }
            // Alles, was nicht erreicht wird, muss weiß sein
            for (int i = 0; i < size; i++) {
                if (tmp[i] == UNKNOWN) {
                    System.err.println("white: " + i);
                    setResult(i, WHITE);
                }
            }
        }

        // eigentlich Repräsentation
        private void setDefiniteBlack() {
            for (BlackArea ba : bas) {
                System.err.println(ba);
//                System.err.println("set black from: " + (ba.maxValue()));
//                System.err.println("set black to: " + (ba.minValue() + ba.getLength()));
                for (int i = ba.maxValue(); i < ba.minValue() + ba.getLength(); i++) {
                    System.err.println("definitiv black: " + i);
                    setResult(i, BLACK);
                }
            }
        }

        public abstract char getResult(int i);

        public abstract void setResult(int i, char value);
    }

    class Row extends Line {
        private final int y;

        Row(int[] numbers, int size, int y) {
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

        Col(int[] numbers, int size, int x) {
            super(numbers, size);
            this.x = x;
        }

        @Override
        public char getResult(int y) {
            return result[x][y];
        }

        @Override
        public void setResult(int y, char value) {
            result[x][y] = value;
        }
    }

    public class GenericLine extends Line {

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
        for (int i = 0; i < 2; i++) {
            genericLine.solveLine();
        }
        return String.valueOf(testResult);
    }

    private void initializeGenericVariables(final String input, final int[] numbers) {
        this.genericLine = new GenericLine(numbers, input.length());
        this.testResult = input.toCharArray();
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
        // loop:
            // Alle Constraints anwenden
            // gleichzeitig Sichere schwarze setzen und Weiße
        for (int i = 0; i < 10; i++) {
            for (Line row : rows) {
                row.solveLine();
            }
            for (Line col : cols) {
                col.solveLine();
            }
        }
    }
}
