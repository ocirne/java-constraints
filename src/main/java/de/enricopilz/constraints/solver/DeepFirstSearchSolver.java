package de.enricopilz.constraints.solver;

import de.enricopilz.constraints.UnsatisfiableException;
import de.enricopilz.constraints.api.*;
import de.enricopilz.constraints.problem.Variable;
import de.enricopilz.constraints.problem.Variables;
import de.enricopilz.constraints.problem.constraints.BiConstraint;
import de.enricopilz.constraints.problem.constraints.SimConstraint;

import java.util.*;
import java.util.function.Function;

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
        try {
            initialPart(problem, problem.getVariables());
        } catch (UnsatisfiableException e) {
            // fine, return empty list
        }
        return solutions;
    }

    private void initialPart(final Problem<S> problem, Variables<S> variables)
        throws UnsatisfiableException {
        for (SimConstraint<S> constraint : problem.getSimConstraints()) {
            useSimConstraint(variables, constraint);
        }
        reasoningPart(problem, variables.deepClone());
    }

    private void reasoningPart(final Problem<S> problem, Variables<S> variables)
            throws UnsatisfiableException {
        while (true) {
            final long before = variables.countSolvedVariables();
            for (BiConstraint<S> constraint : problem.getBiConstraints()) {
                useBiConstraint(variables, constraint);
            }
            final long after = variables.countSolvedVariables();
            // no changes from constraints: solved, or need to guess
            if (before == after) {
                if (variables.isSolved(after)) {
                    this.solutions.add(variables.extractSolution());
                } else {
                    guessPart(problem, variables.deepClone());
                }
                return;
            }
        }
    }

    private void guessPart(final Problem<S> problem, Variables<S> variables) {
        Variable<S> unsolvedVariable = variables.chooseUnsolvedVariable();
        for (var possibility : unsolvedVariable.getPossibilities()) {
            unsolvedVariable.guessValue(possibility);
            try {
                reasoningPart(problem, variables.deepClone());
            } catch (UnsatisfiableException e) {
                // fine, next
            }
        }
    }

    private void useSimConstraint(final Variables<S> variables, final SimConstraint<S> constraint)
            throws UnsatisfiableException {
        final Variable<S> variable = variables.get(constraint.getSymbol());
        removeNonMatching(variable, constraint::match);
    }

    private void useBiConstraint(final Variables<S> variables, final BiConstraint<S> constraint)
            throws UnsatisfiableException {
        final Variable<S> a = variables.get(constraint.getA());
        final Variable<S> b = variables.get(constraint.getB());
        if (a.value().isPresent()) {
            removeNonMatching(b, x -> constraint.match(a.value().get(), x));
        }
        if (b.value().isPresent()) {
            removeNonMatching(a, x -> constraint.match(x, b.value().get()));
        }
    }

    private void removeNonMatching(final Variable<S> variable, final Function<Integer, Boolean> fun)
            throws UnsatisfiableException {
        // If already solved, then only check (faster detection of wrong guesses)
        if (variable.value().isPresent()) {
            if (!fun.apply(variable.value().get())) {
                throw new UnsatisfiableException("not matching assignment");
            }
            return;
        }
        // remove everything which doesn't match
        List<Integer> removals = new LinkedList<>();
        for (Integer value : variable.getPossibilities()) {
            if (!fun.apply(value)) {
                removals.add(value);
            }
        }
        // No more possibilities? Then a guess was wrong.
        if (variable.getPossibilities().size() == removals.size()) {
            throw new UnsatisfiableException("all possibilities removed");
        }
        variable.removePossibilities(removals);
    }
}
