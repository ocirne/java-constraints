package de.enricopilz.constraints.api;

import de.enricopilz.constraints.solver.DeepFirstSearchSolver;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SudokuIT {

    private static final List<Integer> RANGE_1_9 = IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toList());

    /** row, column or tile */
    static class Group {

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

    private Integer fieldSymbol(int y, int x) {
        return 10 * y + x;
    }

    private List<Group> createRows() {
        List<Group> rows = new ArrayList<>();
        for (int y = 1; y <= 9; y++) {
            Group row = new Group();
            for (int x = 1; x <= 9; x++) {
                row.add(fieldSymbol(y, x));
            }
            rows.add(row);
        }
        return rows;
    }

    private List<Group> createCols() {
        List<Group> cols = new ArrayList<>();
        for (int x = 1; x <= 9; x++) {
            Group col = new Group();
            for (int y = 1; y <= 9; y++) {
                col.add(fieldSymbol(y, x));
            }
            cols.add(col);
        }
        return cols;
    }

    private List<Group> createTiles() {
        List<Group> tiles = new ArrayList<>();
        for (int ty = 0; ty < 3; ty++) {
            for (int tx = 0; tx < 3; tx++) {
                Group tile = new Group();
                for (int my = 1; my <= 3; my++) {
                    for (int mx = 1; mx <= 3; mx++) {
                        tile.add(fieldSymbol(ty * 3 + my,tx * 3 + mx));
                    }
                }
                tiles.add(tile);
            }
        }
        return tiles;
    }

    /** problem definition from https://projecteuler.net/project/resources/p096_sudoku.txt */
    @Test
    public void canSolveEulerSudokus() throws Exception {
        URL resource = SudokuIT.class.getClassLoader().getResource("p096_sudoku.txt");
        File file = new File(Objects.requireNonNull(resource).getFile());
        BufferedReader b = new BufferedReader(new FileReader(file));

        int total = 0;
        while (true) {
            // ignore "Grid xx"
            String header = b.readLine();
            if (header == null) {
                break;
            }
            // read sudokus
            int[][] field = new int[9][9];
            for (int y = 0; y < 9; y++) {
                String readLine = b.readLine();
                for (int x = 0; x < 9; x++) {
                    field[y][x] = Integer.parseInt(readLine.substring(x, x+1));
                }
            }
            // solve
            int result = canSolveSudoku(field);
            total += result;
        }
        assertThat(total).isEqualTo(24702);
    }

    /** Problem definition from http://staffhome.ecm.uwa.edu.au/~00013890/sudokumin.php */
    @Test
    @Ignore("too inefficient right now")
    public void canSolveMinimalSudokus() throws Exception {
        URL resource = SudokuIT.class.getClassLoader().getResource("sudoku17");
        File file = new File(Objects.requireNonNull(resource).getFile());
        BufferedReader b = new BufferedReader(new FileReader(file));

        String readLine;
        while ((readLine = b.readLine()) != null) {
            // read
            int[][] field = new int[9][9];
            int i = 0;
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    field[y][x] = Integer.parseInt(readLine.substring(i, i+1));
                    i++;
                }
            }
            // solve
            int result = canSolveSudoku(field);
            System.out.println("sol: " + result);
        }
    }

    /**
     * There are 9x9 cells, thus 81 variables.
     * At least 17 are given.
     * The tiles get symbols from 11 to 99 with possibilities 1 to 9.
     */
    private int canSolveSudoku(int[][] field) {
        Problem.Builder<Integer> sudokuProblem = new Problem.Builder<>();

        List<Group> rows = createRows();
        List<Group> cols = createCols();
        List<Group> tiles = createTiles();

        // variables
        for (int y = 1; y <= 9; y++) {
            for (int x = 1; x <= 9; x++) {
                sudokuProblem.addVariable(fieldSymbol(y, x), RANGE_1_9);
            }
        }
        // general sudoku constraints
        rows.forEach(row -> sudokuProblem.addAllDifferentConstraint(row.asList()));
        cols.forEach(col -> sudokuProblem.addAllDifferentConstraint(col.asList()));
        tiles.forEach(tile -> sudokuProblem.addAllDifferentConstraint(tile.asList()));

        // given values
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (field[y][x] != 0) {
                    final int cellValue = field[y][x];
                    sudokuProblem.addConstraint(fieldSymbol(y+1, x+1), (a) -> a.equals(cellValue));
                }
            }
        }

        Solver<Integer> solver = new DeepFirstSearchSolver<>(sudokuProblem.build());
        List<Solution<Integer>> sudokuSolutions = solver.solve();
        assertThat(sudokuSolutions).hasSize(1);
        Solution<Integer> sudokuSolution = sudokuSolutions.get(0);

        int f00 = sudokuSolution.getValue(fieldSymbol(1, 1));
        int f01 = sudokuSolution.getValue(fieldSymbol(1, 2));
        int f02 = sudokuSolution.getValue(fieldSymbol(1, 3));

        return 100 * f00 + 10 * f01 + f02;
    }
}
