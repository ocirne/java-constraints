package de.enricopilz.constraints.solver;

import de.enricopilz.constraints.api.Problem;
import de.enricopilz.constraints.api.Solution;
import de.enricopilz.constraints.api.Solver;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeepFirstSearchSolverTest {

    @Test(expected = IllegalArgumentException.class)
    public void rejectProblemWithoutVariables() {
        // GIVEN problem with no variables
        Problem.Builder<Integer> problem = new Problem.Builder<>();
        // WHEN instantiating the problem
        new DeepFirstSearchSolver<>(problem.build());
    }

    @Test
    public void canSolveProblemWithoutSolutions() {
        // GIVEN problem with no solution
        Problem.Builder<Integer> problem = new Problem.Builder<>();
        problem.addVariable(1, List.of(1, 2));
        problem.addVariable(2, List.of(3, 4));
        problem.addConstraint(1, 2, Integer::equals);
        // WHEN solving the problem
        Solver<Integer> solver = new DeepFirstSearchSolver<>(problem.build());
        List<Solution<Integer>> solutions = solver.solve();
        // THEN no solution
        assertThat(solutions).isEmpty();
    }

    @Test
    public void canSolveProblemWithExactlySolution() {
        // GIVEN problem with one solution
        Problem.Builder<Integer> problem = new Problem.Builder<>();
        problem.addVariable(1, List.of(1, 2));
        problem.addVariable(2, List.of(2, 3));
        problem.addConstraint(1, 2, Integer::equals);
        // WHEN solving the problem
        Solver<Integer> solver = new DeepFirstSearchSolver<>(problem.build());
        List<Solution<Integer>> solutions = solver.solve();
        // THEN has exactly one solution
        assertThat(solutions).hasSize(1);
    }

    @Test
    public void canSolveProblemWithMultipleSolutions() {
        // GIVEN problem with multiple solutions
        Problem.Builder<Integer> problem = new Problem.Builder<>();
        problem.addVariable(1, List.of(1, 2, 3));
        // WHEN solving the problem
        Solver<Integer> solver = new DeepFirstSearchSolver<>(problem.build());
        List<Solution<Integer>> solutions = solver.solve();
        // THEN there are multiple solutions
        assertThat(solutions).hasSizeGreaterThan(1);
    }
}
