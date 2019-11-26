package de.enricopilz.constraints.api;

import de.enricopilz.constraints.solver.NonogramSolver;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NonogramIT {

    /*
          1 1 1 1
      1 1 . . . .
        2 . . . .
     */
    @Test
    public void testGuessNonogram() {
        // size 4 cols x 2 rows
        int[][] rowNumbers = new int[][]{{1, 1}, {2}};
        int[][] colNumbers = new int[][]{{1}, {1}, {1}, {1}};
        String expectedSolution =
                "#  #" +
                " ## ";
    }

    @Test
    public void testWikipediaP() {
        // size 8 cols x 11 rows
        int[][] rowNumbers = new int[][]{{}, {4}, {6}, {2, 2}, {2, 2}, {6}, {4}, {2}, {2}, {2}, {}};
        int[][] colNumbers = new int[][]{{}, {9}, {9}, {2, 2}, {2, 2}, {4}, {4}, {}};
        String expectedSolution =
                "        " +
                " ####   " +
                " ###### " +
                " ##  ## " +
                " ##  ## " +
                " ###### " +
                " ####   " +
                " ##     " +
                " ##     " +
                " ##     " +
                " ##     " +
                "        ";
        NonogramSolver nonogramSolver = new NonogramSolver();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
//        assertThat(result).isEqualTo(expectedSolution);
    }
}
