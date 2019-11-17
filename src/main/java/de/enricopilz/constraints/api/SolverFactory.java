package de.enricopilz.constraints.api;

import de.enricopilz.constraints.solver.DeepFirstSearchSolver;

import java.lang.reflect.InvocationTargetException;

public class SolverFactory {

    enum SolverEnum {
        DFS(DeepFirstSearchSolver.class);

        private Class<? extends Solver> clazz;

        SolverEnum(final Class<? extends Solver> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends Solver> getClazz() {
            return clazz;
        }
    }

    public static <S> Solver<S> constructSolver(final SolverEnum solver, final Problem<S> problem) {
        try {
            return solver.getClazz().getConstructor(Problem.class).newInstance(problem);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
