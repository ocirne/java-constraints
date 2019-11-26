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
        NonogramSolver nonogramSolver = new NonogramSolver();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
    }

    @Test
    public void testWikipediaP() {
        // size 8 cols x 11 rows
        int[][] rowNumbers = new int[][]{{}, {4}, {6}, {2, 2}, {2, 2}, {6}, {4}, {2}, {2}, {2}, {}};
        int[][] colNumbers = new int[][]{{}, {9}, {9}, {2, 2}, {2, 2}, {4}, {4}, {}};
        String expectedSolution =
                "        \n" +
                " ####   \n" +
                " ###### \n" +
                " ##  ## \n" +
                " ##  ## \n" +
                " ###### \n" +
                " ####   \n" +
                " ##     \n" +
                " ##     \n" +
                " ##     \n" +
                "        \n";
        NonogramSolver nonogramSolver = new NonogramSolver();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
        assertThat(result).isEqualTo(expectedSolution);
    }

    @Test
    public void testRandomPattern() {
        // size 8 cols x 11 rows
        int[][] rowNumbers = new int[][]{
                {3,4},
                {2,5},
                {2,1,1,1,3,1},
                {6,5,1},
                {8,6},
                {9},
                {6},
                {3,1},
                {2},
                {1,2},
                {2,2},
                {4,1},
                {1,1,7},
                {1,9},
                {12},
        };
        int[][] colNumbers = new int[][]{
                {6,3},
                {6,1},
                {1,3,5},
                {4,3,2},
                {4,4},
                {6,4},
                {4,3},
                {7,3},
                {1,1,2,3},
                {5,1,5},
                {5,3,3},
                {5,2,1},
                {1,1},
                {1,1},
                {2,1},
        };
        String expectedSolution =
                "###      ####  \n" +
                "##     #####   \n" +
                "## # # # ### # \n" +
                "###### #####  #\n" +
                "######## ######\n" +
                "#########      \n" +
                "    ######     \n" +
                "     ###      #\n" +
                "          ##   \n" +
                "   #      ##   \n" +
                "  ##     ##    \n" +
                "  ####   #     \n" +
                "# # #######    \n" +
                "# #########    \n" +
                "############   \n";
        NonogramSolver nonogramSolver = new NonogramSolver();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
        assertThat(result).isEqualTo(expectedSolution);
    }

}
