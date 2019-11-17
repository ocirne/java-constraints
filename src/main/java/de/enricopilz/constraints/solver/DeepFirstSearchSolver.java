package de.enricopilz.constraints.solver;

import de.enricopilz.constraints.api.*;
import de.enricopilz.constraints.problem.Variable;
import de.enricopilz.constraints.problem.Variables;
import de.enricopilz.constraints.problem.constraints.BiConstraint;
import de.enricopilz.constraints.problem.constraints.SimConstraint;

import java.util.*;
import java.util.function.BiFunction;

/**
 * S : Type of symbols
 */
public class DeepFirstSearchSolver<S> implements Solver<S> {

    private final Problem<S> problem;

    private List<Solution<S>> solutions;

    public DeepFirstSearchSolver(final Problem<S> problem) {
        if (problem.getVariables().isEmpty()) {
            throw new IllegalArgumentException("Cannot solve a problem without variables.");
        }
        this.problem = problem;
        this.solutions = new LinkedList<>();
    }

    @Override
    public List<Solution<S>> solve() {
        initialPart(problem, problem.getVariables());
        return solutions;
    }

    private void initialPart(final Problem<S> problem, Variables<S> variables) {
        for (SimConstraint<S> constraint : problem.getSimConstraints()) {
            if (!useSimConstraint(variables, constraint)) {
                return;
            }
        }
        reasoningPart(problem, variables.deepClone());
    }

    private void reasoningPart(final Problem<S> problem, Variables<S> variables) {
        while (true) {
            long before = variables.countSolvedVariables();
            for (BiConstraint<S> constraint : problem.getBiConstraints()) {
                if (!useBiConstraint(variables, constraint)) {
                    return;
                }
            }
            long after = variables.countSolvedVariables();
            if (variables.isSolved(after)) {
                this.solutions.add(variables.extractSolution());
                return;
            }
            if (before == after) {
                // assumption, that first guess must bring a solution
                guessPart(problem, variables.deepClone());
                return;
            }
        }
    }

    private void guessPart(final Problem<S> problem, Variables<S> variables) {
        Variable<S> unsolvedVariable = variables.chooseUnsolvedVariable();
        for (var possibility : unsolvedVariable.getPossibilities()) {
            unsolvedVariable.guessValue(possibility);
            reasoningPart(problem, variables.deepClone());
        }
    }

    private boolean useSimConstraint(Variables<S> variables, SimConstraint<S> constraint) {
        Variable<S> variable = variables.get(constraint.getSymbol());
        // If already solved, then only check (faster detection of wrong guesses)
        if (variable.value().isPresent()) {
            return constraint.match(variable.value().get());
        }
        // remove everything which doesn't match
        List<Integer> removals = new LinkedList<>();
        for (Integer value : variable.getPossibilities()) {
            if (!constraint.match(value)) {
                removals.add(value);
            }
        }
        // No more possibilities? Then a guess was wrong.
        if (variable.getPossibilities().size() == removals.size()) {
            return false;
        }
        variable.removePossibilities(removals);
        return true;
    }

    private boolean useBiConstraint(final Variables<S> variables, final BiConstraint<S> constraint) {
        final Variable<S> a = variables.get(constraint.getA());
        final Variable<S> b = variables.get(constraint.getB());
        return removeNonMatching(a, b, constraint::match) &&
                removeNonMatching(b, a, (x, y) -> constraint.match(y, x));
    }

    private boolean removeNonMatching(final Variable<S> aVar, final Variable<S> bVar, final BiFunction<Integer, Integer, Boolean> f) {
        // if the symbol isn't solved we don't remove anything from the other symbol
        if (aVar.value().isEmpty()) {
            return true;
        }
        final Integer aVal = aVar.value().get();
        // remove everything which doesn't match
        List<Integer> removals = new LinkedList<>();
        for (Integer bVal : bVar.getPossibilities()) {
            if (!f.apply(aVal, bVal)) {
                removals.add(bVal);
            }
        }
        // No more possibilities? Then a guess was wrong.
        if (bVar.getPossibilities().size() == removals.size()) {
            return false;
        }
        bVar.removePossibilities(removals);
        return true;
    }
}
