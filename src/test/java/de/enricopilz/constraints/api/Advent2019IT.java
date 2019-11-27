package de.enricopilz.constraints.api;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.enricopilz.constraints.api.Advent2019IT.Opfer.*;
import static de.enricopilz.constraints.api.Advent2019IT.Tatort.*;
import static de.enricopilz.constraints.api.Advent2019IT.Todesart.*;
import static de.enricopilz.constraints.api.Advent2019IT.Taeter.*;
import static de.enricopilz.constraints.api.Advent2019IT.Motiv.*;
import static de.enricopilz.constraints.api.Advent2019IT.Tatsache.*;
import static de.enricopilz.constraints.api.SolverFactory.SolverEnum.DFS;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;


public class Advent2019IT {

    private static final List<Integer> RANGE_1_6 = IntStream.rangeClosed(1, 6).boxed().collect(Collectors.toList());

    public enum Opfer {oHase, oPinguin, oThekla, oTed, oFrosch, oSven}

    public enum Tatort {Schlafzimmer, Balkon, Arbeitszimmer, Wintergarten, Bad, Zug}

    public enum Todesart {zermalmt, ausgesperrt, erschlagen, vergiftet, ertraenkt, aufgerissen}

    public enum Taeter {tTed, tFrosch, tSven, tThekla, tGrummelgrunz, tSchmollmops}

    public enum Motiv {Eifersucht, Rache, Liebe, Selbstschutz, Selbstmord, wusste_zuviel}

    public enum Tatsache {gekuschelt, Gesellschaft, SM, natuerlich, Nackenkissen, baden}

    @Test
    public void canSolveAdvent2019Puzzle() {
        Problem.Builder<Enum<?>> advent2019Puzzle = new Problem.Builder<>();

        advent2019Puzzle.addVariables(Arrays.asList(Opfer.values()), RANGE_1_6);
        advent2019Puzzle.addVariables(Arrays.asList(Tatort.values()), RANGE_1_6);
        advent2019Puzzle.addVariables(Arrays.asList(Todesart.values()), RANGE_1_6);
        advent2019Puzzle.addVariables(Arrays.asList(Taeter.values()), RANGE_1_6);
        advent2019Puzzle.addVariables(Arrays.asList(Motiv.values()), RANGE_1_6);
        advent2019Puzzle.addVariables(Arrays.asList(Tatsache.values()), RANGE_1_6);

        advent2019Puzzle.addAllDifferentConstraint(Arrays.asList(Opfer.values()));
        advent2019Puzzle.addAllDifferentConstraint(Arrays.asList(Tatort.values()));
        advent2019Puzzle.addAllDifferentConstraint(Arrays.asList(Todesart.values()));
        advent2019Puzzle.addAllDifferentConstraint(Arrays.asList(Taeter.values()));
        advent2019Puzzle.addAllDifferentConstraint(Arrays.asList(Motiv.values()));
        advent2019Puzzle.addAllDifferentConstraint(Arrays.asList(Tatsache.values()));

        // Offensichtliches

        // Frosch wurde ertränkt.
        advent2019Puzzle.addConstraint(oFrosch, ertraenkt, Integer::equals);
        // Ted ermordete Hase.
        advent2019Puzzle.addConstraint(oHase, tTed, Integer::equals);
        // Pinguin wurde der Arsch aufgerissen und die Gedärme herausgezogen.
        advent2019Puzzle.addConstraint(oPinguin, aufgerissen, Integer::equals);
        // Der Grummelgrunz ermordete Frosch.
        advent2019Puzzle.addConstraint(oFrosch, tGrummelgrunz, Integer::equals);
        // Im Schlafzimmer wurde nichts aufgerissen.
        advent2019Puzzle.addConstraint(Schlafzimmer, aufgerissen, (a, b) -> !a.equals(b));

        // Skurriles

        // Pinguin wusste zuviel.
        advent2019Puzzle.addConstraint(oPinguin, wusste_zuviel, Integer::equals);
        // Sven schlug mit der Fliegenklatsche einmal kräftig zu und dann war es vorbei.
        advent2019Puzzle.addConstraint(tSven, erschlagen, Integer::equals);
        // Jemand wurde aus Eifersucht regelrecht zermalmt.
        advent2019Puzzle.addConstraint(zermalmt, Eifersucht, Integer::equals);
        // Das Motiv von Frosch war Rache.
        advent2019Puzzle.addConstraint(tFrosch, Rache, Integer::equals);
        // Der letzte Mord geschah im Zug.
        advent2019Puzzle.addConstraint(Zug, a -> a.equals(6));
        // Frosch mordete vor Sven.
        advent2019Puzzle.addConstraint(tFrosch, tSven, (a, b) -> a < b);
        // Sven starb vor Frosch.
        advent2019Puzzle.addConstraint(oSven, oFrosch, (a, b) -> a < b);
        // Thekla starb vor dem Kuscheltier, welches vergiftet wurde.
        advent2019Puzzle.addConstraint(oThekla, vergiftet, (a, b) -> a < b);
        // Jemand wurde auf dem Balkon ausgesperrt und verdurstete.
        advent2019Puzzle.addConstraint(Balkon, ausgesperrt, Integer::equals);
        // Sven starb im Wintergarten.
        advent2019Puzzle.addConstraint(oSven, Wintergarten, Integer::equals);
        // Der Grummelgrunz half beim Freitod.
        advent2019Puzzle.addConstraint(tGrummelgrunz, Selbstmord, Integer::equals);
        // Thekla handelte aus unerwiderter Liebe zu Frosch.
        advent2019Puzzle.addConstraint(tThekla, Liebe, Integer::equals);
        // Thekla starb im Arbeitszimmer.
        advent2019Puzzle.addConstraint(oThekla, Arbeitszimmer, Integer::equals);

        // Auflösung

        // Sven hat eine SM-Beziehung und schlägt gerne mal zu.
        advent2019Puzzle.addConstraint(tSven, SM, Integer::equals);
        // Der wurde nicht vergiftet, das ist sein natürliches Aussehen.
        advent2019Puzzle.addConstraint(vergiftet, natuerlich, Integer::equals);
        // Im Bad wurde jemand gebadet.
        advent2019Puzzle.addConstraint(Bad, baden, Integer::equals);
        // Als erstes wurde gekuschelt.
        advent2019Puzzle.addConstraint(gekuschelt, a -> a.equals(1));
        // Der Schmollmops wollte ein Nackenkissen.
        advent2019Puzzle.addConstraint(tSchmollmops, Nackenkissen, Integer::equals);
        // Ted wollte auf dem Balkon eine Rede an das Volk halten?
        advent2019Puzzle.addConstraint(oTed, Gesellschaft, Integer::equals);

        Solver<Enum<?>> solver = SolverFactory.constructSolver(DFS, advent2019Puzzle.build());
        List<Solution<Enum<?>>> adventSolutions = solver.solve();
        assertThat(adventSolutions).hasSize(1);
        Solution<Enum<?>> adventSolution = adventSolutions.get(0);
        printSolution(adventSolution);
    }

    private void printSolution(Solution<Enum<?>> sol) {
        String[][] d = new String[6][7];

        preparePrint(0, Opfer.class, sol, d);
        preparePrint(1, Tatort.class, sol, d);
        preparePrint(2, Todesart.class, sol, d);
        preparePrint(3, Taeter.class, sol, d);
        preparePrint(4, Motiv.class, sol, d);
        preparePrint(5, Tatsache.class, sol, d);

        for (int y = 0; y < 6; y++) {
            System.out.println(format("%8s: %14s%14s%14s%14s%14s%14s", d[y][0], d[y][1], d[y][2], d[y][3], d[y][4], d[y][5], d[y][6]));
        }
    }

    private void preparePrint(int y, Class<? extends Enum<?>> symbols, Solution<Enum<?>> sol, String[][] d) {
        d[y][0] = symbols.getSimpleName();
        for (Enum<?> symbol : symbols.getEnumConstants()) {
            d[y][sol.getValue(symbol)] = symbol.name();
        }
    }
}
