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
import static de.enricopilz.constraints.api.representation.Sudoku.stripWhiteSpace;
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
            final String solution = solveStandardSudoku(fieldBuilder.toString());
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
            String sol = solveStandardSudoku(readLine);
            System.out.println(sol);
        }
    }

    @Test
    public void canSolve2x2Sudoku() {
        final String givenProblem =
                "30 00" +
                "02 01" +

                "10 20" +
                "00 03";
        final String expected =
                "31 42" +
                "42 31" +

                "13 24" +
                "24 13";
        Sudoku sudoku = new Sudoku(2, 2, givenProblem);
        List<String> actualSolutions = sudoku.solve(DFS);
        assertThat(actualSolutions).containsExactly(stripWhiteSpace(expected));
    }

    @Test
    public void canSolve2x3Sudoku() {
        final String givenProblem =
                "602 100" +
                "000 000" +

                "060 045" +
                "540 020" +

                "000 000" +
                "006 403";
        final String expected =
                "652 134" +
                "314 562" +

                "261 345" +
                "543 621" +

                "435 216" +
                "126 453";
        Sudoku sudoku = new Sudoku(2, 3, givenProblem);
        List<String> actualSolutions = sudoku.solve(DFS);
        assertThat(actualSolutions).containsExactly(stripWhiteSpace(expected));
    }

    @Test
    public void canSolve3x4Sudoku() {
        final String givenProblem =
                "0000 02a0 0000" +
                "00a0 0b04 021c" +
                "4620 c000 a00b" +

                "0080 0c00 6090" +
                "0100 00b0 0004" +
                "70b0 0806 00c0" +

                "0200 1090 0c03" +
                "6000 0500 00b0" +
                "0407 00c0 0900" +

                "8003 000a 0572" +
                "bc60 2070 0a00" +
                "0000 0130 0000";
        final String expected =
                "5b7c 32a1 9468" +
                "93a8 6b54 721c" +
                "4621 c789 a35b" +

                "3584 ac12 6b97" +
                "c196 73b5 28a4" +
                "7ab2 9846 31c5" +

                "a25b 1697 8c43" +
                "68c9 4523 17ba" +
                "1437 8acb 5926" +

                "8913 b46a c572" +
                "bc65 2978 4a31" +
                "274a 513c b689";
        Sudoku sudoku = new Sudoku(3, 4, givenProblem);
        List<String> actualSolutions = sudoku.solve(DFS);
        assertThat(actualSolutions).containsExactly(stripWhiteSpace(expected));
    }

    private String solveStandardSudoku(final String sudokuProblem) {
        Sudoku sudoku = new Sudoku(sudokuProblem);
        List<String> sudokuSolutions = sudoku.solve(DFS);
        assertThat(sudokuSolutions).hasSize(1);
        return sudokuSolutions.get(0);
    }
}
