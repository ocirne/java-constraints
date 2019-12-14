package de.enricopilz.constraints.api;

import de.enricopilz.constraints.description.Variable;
import de.enricopilz.constraints.description.Variables;
import de.enricopilz.constraints.description.constraint.BiConstraint;
import de.enricopilz.constraints.description.constraint.SimConstraint;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Problem<S> {

    private final Variables<S> variables;

    private final Set<SimConstraint<S>> simConstraints;

    private final Set<BiConstraint<S>> biConstraints;

    private Problem(final Variables<S> variables, final Set<SimConstraint<S>> simConstraints, final Set<BiConstraint<S>> biConstraints) {
        this.variables = variables;
        this.simConstraints = simConstraints;
        this.biConstraints = biConstraints;
    }

    public Variables<S> getVariables() {
        return variables;
    }

    public Set<SimConstraint<S>> getSimConstraints() {
        return simConstraints;
    }

    public Set<BiConstraint<S>> getBiConstraints() {
        return biConstraints;
    }

    public static class Builder<S> {

        private Map<S, Variable<S>> variables = new HashMap<>();

        private Set<SimConstraint<S>> simConstraints = new HashSet<>();

        private Set<BiConstraint<S>> biConstraints = new HashSet<>();

        public Problem<S> build() {
            return new Problem<>(new Variables<>(variables), simConstraints, biConstraints);
        }

        public void addVariable(final S symbol, final List<Integer> possibilities) {
            variables.put(symbol, new Variable<>(symbol, possibilities));
        }

        public void addVariables(final List<S> symbols, final List<Integer> possibilities) {
            for (S symbol : symbols) {
                addVariable(symbol, possibilities);
            }
        }

        public void addConstraint(final S symbol, final Function<Integer, Boolean> f) {
            checkSymbolExists(symbol);
            simConstraints.add(new SimConstraint<>(symbol, f));
        }

        public void addConstraint(final S a, final S b, final BiFunction<Integer, Integer, Boolean> f) {
            checkSymbolExists(a);
            checkSymbolExists(b);
            biConstraints.add(new BiConstraint<>(a, b, f));
        }

        public void addAllDifferentConstraint(final List<S> symbols) {
            for (final S s : symbols) {
                checkSymbolExists(s);
            }
            for (final S a : symbols) {
                for (final S b : symbols) {
                    if (a.hashCode() <= b.hashCode()) {
                        continue;
                    }
                    biConstraints.add(new BiConstraint<>(a, b, (x, y) -> !x.equals(y)));
                }
            }
        }

        private void checkSymbolExists(final S symbol) {
            if (!variables.containsKey(symbol)) {
                throw new IllegalArgumentException("Illegal variable in constraint: " + symbol);
            }
        }
    }
}
