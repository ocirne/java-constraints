package de.enricopilz.constraints.api;

import de.enricopilz.constraints.api.representation.Sudoku;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

import static de.enricopilz.constraints.api.SolverFactory.SolverEnum.DFS;
import static org.assertj.core.api.Assertions.assertThat;

public class SudokuIT {

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
            StringBuilder fieldBuilder = new StringBuilder();
            for (int y = 0; y < 9; y++) {
                fieldBuilder.append(b.readLine());
            }
            // solve
            final String solution = solveUniqueSudoku(fieldBuilder.toString());
            final int result = Integer.parseInt(solution.substring(0, 3));
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
            String sol = solveUniqueSudoku(readLine);
            System.out.println(sol);
        }
    }

    @Test
    public void canSolve2x2Sudoku() {
        final String givenProblem = "3000020110200003";
        final String expected     = "3142423113242413";
        Sudoku sudoku = new Sudoku(2, 2, givenProblem);
        List<String> actualSolutions = sudoku.solve(DFS);
        assertThat(actualSolutions).containsExactly(expected);
    }

    @Test
    public void canSolve2x3Sudoku() {
        final String givenProblem = "602100000000060045540020000000006403";
        final String expected     = "652134314562261345543621435216126453";
        Sudoku sudoku = new Sudoku(2, 3, givenProblem);
        List<String> actualSolutions = sudoku.solve(DFS);
        assertThat(actualSolutions).containsExactly(expected);
    }

    private String solveUniqueSudoku(final String sudokuProblem) {
        Sudoku sudoku = new Sudoku(sudokuProblem);
        List<String> sudokuSolutions = sudoku.solve(DFS);
        assertThat(sudokuSolutions).hasSize(1);
        return sudokuSolutions.get(0);
    }
}
