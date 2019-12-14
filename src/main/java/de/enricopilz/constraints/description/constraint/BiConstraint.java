package de.enricopilz.constraints.description.constraint;

import java.util.function.BiFunction;

public class BiConstraint<S> {

    private final S a;
    private final S b;
    private final BiFunction<Integer, Integer, Boolean> constraint;

    public BiConstraint(final S a, final S b, final BiFunction<Integer, Integer, Boolean> constraint) {
        this.a = a;
        this.b = b;
        this.constraint = constraint;
    }

    public boolean match(final Integer a, final Integer b) {
        return constraint.apply(a, b);
    }

    public S getA() {
        return a;
    }

    public S getB() {
        return b;
    }
}
