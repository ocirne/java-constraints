package de.enricopilz.constraints.api;

import de.enricopilz.constraints.problem.Variables;

import java.util.Map;
import java.util.stream.Collectors;

public class Solution<S> {

    private final Map<S, Integer> map;

    public Solution(final Variables<S> variables) {
        this.map = variables.getMap().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().value().orElseThrow()));
    }

    public Integer getValue(final S symbol) {
        return map.get(symbol);
    }
}
