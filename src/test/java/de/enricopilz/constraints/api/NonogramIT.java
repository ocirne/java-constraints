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
//        assertThat(result).isEqualTo(expectedSolution);
    }

    // https://stackoverflow.com/questions/813366/solving-nonograms-picross

    /*
    Chad Birch:
    15 15
15,4 5,2 4,1 3,2,2,2 4 3,2 6 2,2 1 6 2,2 1 1 4 2,1 1,1 3 2 1,2 2 1 2 1,3 3 2 1,9
4 4,3 1 2 3,2 1 2 2,2 1 1,1 4 2,1 3,1 8,1 3 1 1,1 4 2 1,1 4,2 4 3,3 3 3,4 1,10 3,10


    Mikko Rantanen:
    List<List<int>> rows =
            new List<List<int>>()
            {
                    new List<int> { 8,29,4 },
                    new List<int> { 6,4,25,4,3 },
                    new List<int> { 5,3,2,3,9,4,2,1,3 },
                    new List<int> { 4,2,2,2,2,1,2,2 },
                    new List<int> { 4,1,1,9,10,2,2,1 },
                    new List<int> { 3,2,6,5,5,1,1 },
                    new List<int> { 3,1,5,5,1,1 },
                    new List<int> { 3,1,4,4,1,1 },
                    new List<int> { 3,1,4,4,1,1 },
                    new List<int> { 3,1,3,3,1,1 },
                    new List<int> { 3,1,3,6,2 },
                    new List<int> { 3,1,2,3,2,4,2 },
                    new List<int> { 4,3,1,8,7,1,2,3 },
                    new List<int> { 4,2,1,12,11,1,2,4 },
                    new List<int> { 5,1,2,7,2,2,6,1,1,4 },
                    new List<int> { 4,1,1,1,6,2,2,6,1,2,1,3 },
                    new List<int> { 4,1,1,2,4,3,4,3,1,1,1,1,3 },
                    new List<int> { 4,1,1,2,1,4,1,2,3,2,1,2,2 },
                    new List<int> { 3,1,1,1,2,5,6,1,1,1,3,2 },
                    new List<int> { 3,2,1,1,2,1,5,4,4,2,1,2,1,2 },
                    new List<int> { 3,2,2,1,1,4,2,2,3,1,1,2,1,1,2 },
                    new List<int> { 3,1,3,2,1,1,4,1,5,3,2,1,3,1,2 },
                    new List<int> { 3,1,2,1,2,1,3,7,4,1,4,2,2 },
                    new List<int> { 2,1,4,1,1,1,2,6,2,2,2,3,2,1 },
                    new List<int> { 2,2,4,1,2,1,2,5,2,1,1,3,2,1 },
                    new List<int> { 2,2,1,4,1,1,3,3,2,1,4,4,1 },
                    new List<int> { 2,3,3,2,1,3,3,7,4,1 },
                    new List<int> { 2,3,2,4,5,8,1,2,1 },
                    new List<int> { 1,1,3,11,6,7,1,3,1 },
                    new List<int> { 1,1,2,2,13,10,2,3,2 },
                    new List<int> { 1,2,3,1,6,1,1,7,1,5,2 },
                    new List<int> { 1,1,3,2,6,1,1,1,1,4,1,4,2 },
                    new List<int> { 1,1,6,7,2,4,2,5,6,1 },
                    new List<int> { 1,1,2,3,1,4,2,2,11,2,1 },
                    new List<int> { 1,1,1,1,2,1,5,10,1,1,1 },
                    new List<int> { 1,1,1,1,4,7,4,10,1,1,1 },
                    new List<int> { 1,2,1,1,28,1,1,3 },
                    new List<int> { 1,2,1,2,27,2,1,3 },
                    new List<int> { 1,1,1,1,26,1,1,1,1 },
                    new List<int> { 2,3,1,28,2,1,2,1 }
            };
    List<List<int>> cols =
            new List<List<int>>()
            {
                    new List<int> { 40 },
                    new List<int> { 28,1 },
                    new List<int> { 23,8 },
                    new List<int> { 5,6,7,4 },
                    new List<int> { 3,6,1,9,3,1 },
                    new List<int> { 2,3,2,5,4,2,2 },
                    new List<int> { 1,2,4,1,2,5,2 },
                    new List<int> { 1,1,4,9,2,3,2 },
                    new List<int> { 2,4,2,6,1,4,3 },
                    new List<int> { 1,4,1,3,4,1,6 },
                    new List<int> { 1,4,3,2,3,5,5 },
                    new List<int> { 2,4,1,2,3,4,1,3 },
                    new List<int> { 1,2,3,4,2,2,4,4,1 },
                    new List<int> { 1,1,2,3,2,1,4,2,4 },
                    new List<int> { 2,3,5,3,3,5,4 },
                    new List<int> { 3,1,6,1,2,5,5 },
                    new List<int> { 3,2,6,2,15 },
                    new List<int> { 3,1,8,2,13 },
                    new List<int> { 2,2,4,5,15 },
                    new List<int> { 2,2,2,2,22 },
                    new List<int> { 2,1,1,1,12,6 },
                    new List<int> { 2,1,10,4,5 },
                    new List<int> { 3,1,3,1,2,4 },
                    new List<int> { 3,1,1,4,3,1,4 },
                    new List<int> { 3,2,2,3,2,2,5 },
                    new List<int> { 3,1,1,5,1,1,5 },
                    new List<int> { 3,1,1,5,1,1,5 },
                    new List<int> { 3,1,1,5,1,1,5 },
                    new List<int> { 3,2,5,2,1,1,4 },
                    new List<int> { 3,1,1,3,2,2,4 },
                    new List<int> { 3,1,6,4,5 },
                    new List<int> { 2,2,12,2,6 },
                    new List<int> { 2,2,1,1,22 },
                    new List<int> { 2,1,2,2,5,15 },
                    new List<int> { 3,1,4,3,2,14 },
                    new List<int> { 3,1,7,2,1,13 },
                    new List<int> { 3,2,6,1,1,6,8 },
                    new List<int> { 3,2,5,2,2,4,7 },
                    new List<int> { 2,1,2,4,1,1,1,4,1,4,2 },
                    new List<int> { 1,1,4,4,3,1,4,5,1 },
                    new List<int> { 1,1,5,1,1,2,1,2,2,3,2 },
                    new List<int> { 1,5,2,2,1,5,5,3 },
                    new List<int> { 1,6,2,1,4,2,6,1 },
                    new List<int> { 1,6,2,6,5,2 },
                    new List<int> { 1,5,3,1,9,2 },
                    new List<int> { 2,2,4,2,6,3 },
                    new List<int> { 1,2,2,2,9,2,1 },
                    new List<int> { 3,5,5,8,4 },
                    new List<int> { 4,13,9 },
                    new List<int> { 27,2 }
            };
    */
}
