package de.enricopilz.constraints.api;

import java.util.List;

/**
 * S : Type of symbols
 */
public interface Solver<S> {

    List<Solution<S>> solve();
}
