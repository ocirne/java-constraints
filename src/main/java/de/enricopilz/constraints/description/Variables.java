package de.enricopilz.constraints.description;

import de.enricopilz.constraints.api.Solution;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Variables<S> {

    private final Map<S, Variable<S>> map;

    public Variables(final Map<S, Variable<S>> map) {
        this.map = map;
    }

    public Variable<S> get(final S symbol) {
        Variable<S> variable = map.get(symbol);
        if (variable.getPossibilities().size() < 1) {
            throw new IllegalStateException();
        }
        return variable;
    }

    public boolean isSolved(final long countSolved) {
        return this.map.size() == countSolved;
    }

    public Variables<S> deepClone() {
        final Map<S, Variable<S>> clonedMap = map.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Entry::getKey,
                        entry -> entry.getValue().clone()
                ));
        return new Variables<>(clonedMap);
    }

    public long countSolvedVariables() {
        return map.values().stream().filter(v -> v.value().isPresent()).count();
    }

    // TODO könnte besser ausgewählt werden, Heuristiken ausprobieren
    public Variable<S> chooseUnsolvedVariable() {
        return map.values().stream()
                .filter(v -> v.value().isEmpty())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Seems like everything is already solved."));
    }

    public Solution<S> extractSolution() {
        return new Solution<>(map.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().value().orElseThrow())));
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
