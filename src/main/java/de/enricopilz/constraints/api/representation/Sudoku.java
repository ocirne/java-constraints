package de.enricopilz.constraints.api.representation;

import de.enricopilz.constraints.api.Problem;
import de.enricopilz.constraints.api.Solution;
import de.enricopilz.constraints.api.Solver;
import de.enricopilz.constraints.api.SolverFactory;
import de.enricopilz.constraints.api.SolverFactory.SolverEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sudoku {

    // Ein Standard-Sudoku hat eine Größe von 3x3, die Gruppen enthalten dann 9 Zahlen (size)
    // Ein 2x3 sieht so aus
    /* ... ...
       ... ...

       ... ...
       ... ...

       ... ...
       ... ...
     */
    private final int width;

    private final int height;

    // convenience outer size
    private final int size;

    private final int[] field;

    /** Standard 9x9 Sudoku */
    public Sudoku(String field) {
        this(3, 3, field);
    }

    public Sudoku(int width, int height, String field) {
        if (width * width * height * height != field.length()) {
            throw new IllegalArgumentException("Größe des Problems entspricht nicht angegebener Breite x Höhe.");
        }
        this.width = width;
        this.height = height;
        this.size = width * height;
        char[] tmp = field.toCharArray();
        this.field = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            this.field[i] = Character.getNumericValue(tmp[i]);
        }
    }

    private List<Integer> range() {
        return IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
    }

    /** row, column or tile */
    private static class Group {

        private List<Integer> group;

        Group() {
            this.group = new ArrayList<>();
        }

        void add(Integer value) {
            group.add(value);
        }

        @Override
        public String toString() {
            return group.stream().map(Object::toString).collect(Collectors.joining(", "));
        }

        List<Integer> asList() {
            return group;
        }
    }

    // TODO das funktioniert (vermutlich) nur bis 9x9
    private Integer fieldSymbol(int y, int x) {
        return 10 * y + x;
    }

    private List<Group> createRows() {
        List<Group> rows = new ArrayList<>();
        for (int y = 1; y <= size; y++) {
            Group row = new Group();
            for (int x = 1; x <= size; x++) {
                row.add(fieldSymbol(y, x));
            }
            rows.add(row);
        }
        return rows;
    }

    private List<Group> createCols() {
        List<Group> cols = new ArrayList<>();
        for (int x = 1; x <= size; x++) {
            Group col = new Group();
            for (int y = 1; y <= size; y++) {
                col.add(fieldSymbol(y, x));
            }
            cols.add(col);
        }
        return cols;
    }

    private List<Group> createTiles() {
        List<Group> tiles = new ArrayList<>();
        for (int ty = 0; ty < width; ty++) {
            for (int tx = 0; tx < height; tx++) {
                Group tile = new Group();
                for (int my = 1; my <= height; my++) {
                    for (int mx = 1; mx <= width; mx++) {
                        tile.add(fieldSymbol(ty * width + my,tx * height + mx));
                    }
                }
                tiles.add(tile);
            }
        }
        return tiles;
    }

    /**
     * There are 9x9 cells, thus 81 variables.
     * At least 17 are given.
     * The tiles get symbols from 11 to 99 with possibilities 1 to 9.
     */
    public List<String> solve(SolverEnum solverEnum) {
        Problem.Builder<Integer> sudokuProblem = new Problem.Builder<>();

        List<Group> rows = createRows();
        List<Group> cols = createCols();
        List<Group> tiles = createTiles();

        // variables
        for (int y = 1; y <= size; y++) {
            for (int x = 1; x <= size; x++) {
                sudokuProblem.addVariable(fieldSymbol(y, x), range());
            }
        }
        // general sudoku constraints
        rows.forEach(row -> sudokuProblem.addAllDifferentConstraint(row.asList()));
        cols.forEach(col -> sudokuProblem.addAllDifferentConstraint(col.asList()));
        tiles.forEach(tile -> sudokuProblem.addAllDifferentConstraint(tile.asList()));

        // given values
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int i = y * size + x;
                if (field[i] != 0) {
                    final int cellValue = field[i];
                    sudokuProblem.addConstraint(fieldSymbol(y+1, x+1), (a) -> a.equals(cellValue));
                }
            }
        }

        // solve
        Solver<Integer> solver = SolverFactory.constructSolver(solverEnum, sudokuProblem.build());

        List<Solution<Integer>> solutions = solver.solve();
        return solutions.stream().map(this::extractSolution).collect(Collectors.toList());
    }

    private String extractSolution(final Solution<Integer> solution) {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                final int cellValue = solution.getValue(fieldSymbol(y +1, x + 1));
                builder.append(cellValue);
            }
        }
        return builder.toString();
    }
}
