package de.enricopilz.constraints.api;

import java.util.Map;

public class Solution<S> {

    private final Map<S, Integer> map;

    public Solution(final Map<S, Integer> map) {
        this.map = map;
    }

    public Integer getValue(final S symbol) {
        return map.get(symbol);
    }
}
