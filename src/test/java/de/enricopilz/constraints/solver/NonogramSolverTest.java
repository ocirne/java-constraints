package de.enricopilz.constraints.solver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setAllowComparingPrivateFields;

@RunWith(Parameterized.class)
public class NonogramSolverTest {

    private NonogramSolver nonogramSolver = new NonogramSolver();

    @Parameters
    public static Iterable<Object[]> data() {
          return Arrays.asList(new Object[][] {
                  { ".", a(), " " },
                  { ".", a(1), "#" },
                  { ".....", a(2), "....." },
                  { ".....", a(3), "..#.." },
                  { ".....", a(4), ".###." },
                  { ".....", a(2, 2), "## ##" },
                  { ".....", a(1, 1, 1), "# # #" },
                  { " . ...", a(3), "   ###" },
                  { "... . ", a(3), "###   " },
                  { "... ...", a(3), "... ..." },
                  { " . . . ", a(1, 1), " . . . " },
                  { " . . . ", a(1, 1, 1), " # # # " },
                  { " . .. . ", a(2, 1), "   ## # " },
                  { ".#......", a(4), ".###.   " },
                  { "......#.", a(4), "   .###." },
                  { "...#...", a(3), " ..#.. " },
          });
    }

    @Parameter(0)
    public String input;

    @Parameter(1)
    public int[] numbers;

    @Parameter(2)
    public String expectedOutput;

    @Test
    public void testSolveLine() {
        System.err.println("'" + input + "' + " + Arrays.toString(numbers) + " -> '" + expectedOutput + "'");
        String actualOutput = nonogramSolver.solveLine(input, numbers);
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    // helper

    private static int[] a(int... numbers) {
        return numbers;
    }
}
