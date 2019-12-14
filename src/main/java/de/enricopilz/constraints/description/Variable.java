package de.enricopilz.constraints.description;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Variable<S> {

    private final S symbol;

    // possible values as List of Symbols
    private List<Integer> possibilities;

    public Variable(final S symbol, final List<Integer> possibilities) {
        this.symbol = symbol;
        this.possibilities = new ArrayList<>(possibilities);
    }

    public Optional<Integer> value() {
        return possibilities.size() == 1 ? Optional.of(possibilities.get(0)) : Optional.empty();
    }

    public void guessValue(final Integer value) {
        this.possibilities = List.of(value);
    }

    public void removePossibilities(final List<Integer> removals) {
        this.possibilities.removeAll(removals);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Variable<S> clone() {
        return new Variable<>(symbol, new ArrayList<>(possibilities));
    }

    public List<Integer> getPossibilities() {
        return this.possibilities;
    }
}
