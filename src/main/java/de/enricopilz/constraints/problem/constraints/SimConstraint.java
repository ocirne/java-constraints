package de.enricopilz.constraints.problem.constraints;

import java.util.function.Function;

public class SimConstraint<S> {

    private final S symbol;
    private final Function<Integer, Boolean> constraint;

    public SimConstraint(final S symbol, final Function<Integer, Boolean> constraint) {
        this.symbol = symbol;
        this.constraint = constraint;
    }

    public boolean match(final Integer a) {
        return constraint.apply(a);
    }

    public S getSymbol() {
        return symbol;
    }
}
