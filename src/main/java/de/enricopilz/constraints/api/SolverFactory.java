package de.enricopilz.constraints.api;

import de.enricopilz.constraints.solver.DeepFirstSearchSolver;

public class SolverFactory {

    public static <S> Solver<S> constructDeepFirstSearchSolver(Problem<S> problem) {
        return new DeepFirstSearchSolver<>(problem);
    }
}
