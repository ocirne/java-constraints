package de.enricopilz.constraints.api;

import de.enricopilz.constraints.solver.NonogramSolver2;
import org.junit.Ignore;
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
        int[][] rowNumbers = new int[][]{{1, 1}, {2}, {2}, {1, 1}};
        int[][] colNumbers = new int[][]{{1, 1}, {2}, {2}, {1, 1}};
        String expectedSolution =
                "#  #\n" +
                " ## \n" +
                " ## \n" +
                "#  #\n";
        NonogramSolver2 nonogramSolver = new NonogramSolver2();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
        assertThat(result).isEqualTo(expectedSolution);
    }

    @Test
    public void testChessboard() {
        // size 4 cols x 2 rows
        int[][] rowNumbers = new int[][]{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}};
        int[][] colNumbers = new int[][]{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}};
        String expectedSolution1 =
                "# # # # \n" +
                " # # # #\n" +
                "# # # # \n" +
                " # # # #\n" +
                "# # # # \n" +
                " # # # #\n" +
                "# # # # \n" +
                " # # # #\n";
        String expectedSolution2 =
                " # # # #\n" +
                "# # # # \n" +
                " # # # #\n" +
                "# # # # \n" +
                " # # # #\n" +
                "# # # # \n" +
                " # # # #\n" +
                "# # # # \n";
        NonogramSolver2 nonogramSolver = new NonogramSolver2();
        // sollte zwei Lösungen ergeben
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
        assertThat(result).isEqualTo(expectedSolution1);
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
        NonogramSolver2 nonogramSolver = new NonogramSolver2();
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
        NonogramSolver2 nonogramSolver = new NonogramSolver2();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
        assertThat(result).isEqualTo(expectedSolution);
    }

    /*
    https://stackoverflow.com/questions/813366/solving-nonograms-picross
    Chad Birch:
    15 15
    15,4 5,2 4,1 3,2,2,2 4 3,2 6 2,2 1 6 2,2 1 1 4 2,1 1,1 3 2 1,2 2 1 2 1,3 3 2 1,9
    4 4,3 1 2 3,2 1 2 2,2 1 1,1 4 2,1 3,1 8,1 3 1 1,1 4 2 1,1 4,2 4 3,3 3 3,4 1,10 3,10
     */
    @Test
    public void testChadBirchPattern() {
        // size 8 cols x 11 rows
        int[][] rowNumbers = new int[][]{
                {15}, {4, 5}, {2, 4}, {1, 3}, {2}, {2}, {2, 4, 3}, {2, 6, 2}, {2, 1, 6, 2}, {2, 1, 1, 4, 2}, {1, 1}, {1, 3, 2, 1}, {2, 2, 1, 2, 1}, {3, 3, 2, 1}, {9},
        };
        int[][] colNumbers = new int[][]{
                {4, 4}, {3, 1, 2, 3}, {2, 1, 2, 2}, {2, 1, 1}, {1, 4, 2}, {1, 3}, {1, 8}, {1, 3, 1, 1}, {1, 4, 2, 1}, {1, 4}, {2, 4, 3}, {3, 3, 3}, {4, 1}, {10, 3}, {10},
        };
        String expectedSolution = ".";
        NonogramSolver2 nonogramSolver = new NonogramSolver2();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
        assertThat(result).isEqualTo(expectedSolution);
    }

    /*
        https://stackoverflow.com/questions/813366/solving-nonograms-picross
        Mikko Rantanen:
     */
    @Ignore
    @Test
    public void testMikkoRantanenPattern() {
        // size 8 cols x 11 rows
        int[][] rowNumbers = new int[][]{
                {8, 29, 4},
                {6, 4, 25, 4, 3},
                {5, 3, 2, 3, 9, 4, 2, 1, 3},
                {4, 2, 2, 2, 2, 1, 2, 2},
                {4, 1, 1, 9, 10, 2, 2, 1},
                {3, 2, 6, 5, 5, 1, 1},
                {3, 1, 5, 5, 1, 1},
                {3, 1, 4, 4, 1, 1},
                {3, 1, 4, 4, 1, 1},
                {3, 1, 3, 3, 1, 1},
                {3, 1, 3, 6, 2},
                {3, 1, 2, 3, 2, 4, 2},
                {4, 3, 1, 8, 7, 1, 2, 3},
                {4, 2, 1, 12, 11, 1, 2, 4},
                {5, 1, 2, 7, 2, 2, 6, 1, 1, 4},
                {4, 1, 1, 1, 6, 2, 2, 6, 1, 2, 1, 3},
                {4, 1, 1, 2, 4, 3, 4, 3, 1, 1, 1, 1, 3},
                {4, 1, 1, 2, 1, 4, 1, 2, 3, 2, 1, 2, 2},
                {3, 1, 1, 1, 2, 5, 6, 1, 1, 1, 3, 2},
                {3, 2, 1, 1, 2, 1, 5, 4, 4, 2, 1, 2, 1, 2},
                {3, 2, 2, 1, 1, 4, 2, 2, 3, 1, 1, 2, 1, 1, 2},
                {3, 1, 3, 2, 1, 1, 4, 1, 5, 3, 2, 1, 3, 1, 2},
                {3, 1, 2, 1, 2, 1, 3, 7, 4, 1, 4, 2, 2},
                {2, 1, 4, 1, 1, 1, 2, 6, 2, 2, 2, 3, 2, 1},
                {2, 2, 4, 1, 2, 1, 2, 5, 2, 1, 1, 3, 2, 1},
                {2, 2, 1, 4, 1, 1, 3, 3, 2, 1, 4, 4, 1},
                {2, 3, 3, 2, 1, 3, 3, 7, 4, 1},
                {2, 3, 2, 4, 5, 8, 1, 2, 1},
                {1, 1, 3, 11, 6, 7, 1, 3, 1},
                {1, 1, 2, 2, 13, 10, 2, 3, 2},
                {1, 2, 3, 1, 6, 1, 1, 7, 1, 5, 2},
                {1, 1, 3, 2, 6, 1, 1, 1, 1, 4, 1, 4, 2},
                {1, 1, 6, 7, 2, 4, 2, 5, 6, 1},
                {1, 1, 2, 3, 1, 4, 2, 2, 11, 2, 1},
                {1, 1, 1, 1, 2, 1, 5, 10, 1, 1, 1},
                {1, 1, 1, 1, 4, 7, 4, 10, 1, 1, 1},
                {1, 2, 1, 1, 28, 1, 1, 3},
                {1, 2, 1, 2, 27, 2, 1, 3},
                {1, 1, 1, 1, 26, 1, 1, 1, 1},
                {2, 3, 1, 28, 2, 1, 2, 1}
        };
        int[][] colNumbers = new int[][]{
                {40},
                {28, 1},
                {23, 8},
                {5, 6, 7, 4},
                {3, 6, 1, 9, 3, 1},
                {2, 3, 2, 5, 4, 2, 2},
                {1, 2, 4, 1, 2, 5, 2},
                {1, 1, 4, 9, 2, 3, 2},
                {2, 4, 2, 6, 1, 4, 3},
                {1, 4, 1, 3, 4, 1, 6},
                {1, 4, 3, 2, 3, 5, 5},
                {2, 4, 1, 2, 3, 4, 1, 3},
                {1, 2, 3, 4, 2, 2, 4, 4, 1},
                {1, 1, 2, 3, 2, 1, 4, 2, 4},
                {2, 3, 5, 3, 3, 5, 4},
                {3, 1, 6, 1, 2, 5, 5},
                {3, 2, 6, 2, 15},
                {3, 1, 8, 2, 13},
                {2, 2, 4, 5, 15},
                {2, 2, 2, 2, 22},
                {2, 1, 1, 1, 12, 6},
                {2, 1, 10, 4, 5},
                {3, 1, 3, 1, 2, 4},
                {3, 1, 1, 4, 3, 1, 4},
                {3, 2, 2, 3, 2, 2, 5},
                {3, 1, 1, 5, 1, 1, 5},
                {3, 1, 1, 5, 1, 1, 5},
                {3, 1, 1, 5, 1, 1, 5},
                {3, 2, 5, 2, 1, 1, 4},
                {3, 1, 1, 3, 2, 2, 4},
                {3, 1, 6, 4, 5},
                {2, 2, 12, 2, 6},
                {2, 2, 1, 1, 22},
                {2, 1, 2, 2, 5, 15},
                {3, 1, 4, 3, 2, 14},
                {3, 1, 7, 2, 1, 13},
                {3, 2, 6, 1, 1, 6, 8},
                {3, 2, 5, 2, 2, 4, 7},
                {2, 1, 2, 4, 1, 1, 1, 4, 1, 4, 2},
                {1, 1, 4, 4, 3, 1, 4, 5, 1},
                {1, 1, 5, 1, 1, 2, 1, 2, 2, 3, 2},
                {1, 5, 2, 2, 1, 5, 5, 3},
                {1, 6, 2, 1, 4, 2, 6, 1},
                {1, 6, 2, 6, 5, 2},
                {1, 5, 3, 1, 9, 2},
                {2, 2, 4, 2, 6, 3},
                {1, 2, 2, 2, 9, 2, 1},
                {3, 5, 5, 8, 4},
                {4, 13, 9},
                {27, 2}
        };
        String expectedSolution = ".";
        NonogramSolver2 nonogramSolver = new NonogramSolver2();
        String result = nonogramSolver.solve(rowNumbers, colNumbers);
        System.err.println("Result:");
        System.err.println(result);
        assertThat(result).isEqualTo(expectedSolution);
    }
}
