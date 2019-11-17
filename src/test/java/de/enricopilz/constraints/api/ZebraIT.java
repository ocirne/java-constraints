package de.enricopilz.constraints.api;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.enricopilz.constraints.api.SolverFactory.SolverEnum.DFS;
import static de.enricopilz.constraints.api.ZebraIT.Nation.*;
import static de.enricopilz.constraints.api.ZebraIT.Color.*;
import static de.enricopilz.constraints.api.ZebraIT.Smoke.*;
import static de.enricopilz.constraints.api.ZebraIT.Pet.*;
import static de.enricopilz.constraints.api.ZebraIT.Drink.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/** This API usage is influenced by (stolen from?) https://rosettacode.org/wiki/Zebra_puzzle#Constraint_Programming_Version_2 */
public class ZebraIT {

    private static final List<Integer> RANGE_1_5 = IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());

    public enum Nation {ENGLISHMAN, SPANIARD, JAPANESE, UKRAINIAN, NORWEGIAN}
    public enum Color {RED, GREEN, IVORY, YELLOW, BLUE}
    public enum Smoke {OLDGOLD, KOOLS, CHESTERFIELD, LUCKYSTRIKE, PARLIAMENT}
    public enum Pet {DOG, SNAILS, FOX, HORSE, ZEBRA}
    public enum Drink {TEA, COFFEE, MILK, ORANGEJUICE, WATER}

    /** Problem definition from https://en.wikipedia.org/wiki/Zebra_Puzzle */
    @Test
    public void canSolveZebraPuzzle() {
        Problem.Builder<Enum<?>> zebraPuzzle = new Problem.Builder<>();

        // 1. There are five houses.
        zebraPuzzle.addVariables(Arrays.asList(Nation.values()), RANGE_1_5);
        zebraPuzzle.addVariables(Arrays.asList(Color.values()), RANGE_1_5);
        zebraPuzzle.addVariables(Arrays.asList(Smoke.values()), RANGE_1_5);
        zebraPuzzle.addVariables(Arrays.asList(Pet.values()), RANGE_1_5);
        zebraPuzzle.addVariables(Arrays.asList(Drink.values()), RANGE_1_5);

        // the values in each list are exclusive
        zebraPuzzle.addAllDifferentConstraint(Arrays.asList(Nation.values()));
        zebraPuzzle.addAllDifferentConstraint(Arrays.asList(Color.values()));
        zebraPuzzle.addAllDifferentConstraint(Arrays.asList(Smoke.values()));
        zebraPuzzle.addAllDifferentConstraint(Arrays.asList(Pet.values()));
        zebraPuzzle.addAllDifferentConstraint(Arrays.asList(Drink.values()));

        // 2. The Englishman lives in the red house.
        zebraPuzzle.addConstraint(Nation.ENGLISHMAN, Color.RED, Integer::equals);
        // 3. The Spaniard owns the dog.
        zebraPuzzle.addConstraint(Nation.SPANIARD, Pet.DOG, Integer::equals);
        // 4. Coffee is drunk in the green house.
        zebraPuzzle.addConstraint(Color.GREEN, COFFEE, Integer::equals);
        // 5. The Ukrainian drinks tea.
        zebraPuzzle.addConstraint(Nation.UKRAINIAN, Drink.TEA, Integer::equals);
        // 6. The green house is immediately to the right of the ivory house.
        zebraPuzzle.addConstraint(Color.GREEN, Color.IVORY, (a, b) -> a.equals(b + 1));
        // 7. The Old Gold smoker owns snails.
        zebraPuzzle.addConstraint(OLDGOLD, SNAILS, Integer::equals);
        // 8. Kools are smoked in the yellow house.
        zebraPuzzle.addConstraint(KOOLS, YELLOW, Integer::equals);
        // 9. Milk is drunk in the middle house.
        zebraPuzzle.addConstraint(MILK, (a) -> a.equals(3));
        // 10. The Norwegian lives in the first house.
        zebraPuzzle.addConstraint(NORWEGIAN, (a) -> a.equals(1));
        // 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
        zebraPuzzle.addConstraint(CHESTERFIELD, FOX, (a, b) -> a.equals(b - 1) || a.equals(b + 1));
        // 12. Kools are smoked in the house next to the house where the horse is kept.
        zebraPuzzle.addConstraint(KOOLS, HORSE, (a, b) -> a.equals(b - 1) || a.equals(b + 1));
        // 13. The Lucky Strike smoker drinks orange juice.
        zebraPuzzle.addConstraint(LUCKYSTRIKE, ORANGEJUICE, Integer::equals);
        // 14. The Japanese smokes Parliaments.
        zebraPuzzle.addConstraint(JAPANESE, PARLIAMENT, Integer::equals);
        // 15. The Norwegian lives next to the blue house.
        zebraPuzzle.addConstraint(NORWEGIAN, BLUE, (a, b) -> a.equals(b - 1) || a.equals(b + 1));

        Solver<Enum<?>> solver = SolverFactory.constructSolver(DFS, zebraPuzzle.build());
        List<Solution<Enum<?>>> zebraSolutions = solver.solve();
        assertThat(zebraSolutions).hasSize(1);
        Solution<Enum<?>> zebraSolution = zebraSolutions.get(0);
        // printSolution(zebraSolution);

        // Now, who drinks water?
        assertThat(zebraSolution.getValue(ZEBRA)).isEqualTo(zebraSolution.getValue(JAPANESE));
        // Who owns the zebra?
        assertThat(zebraSolution.getValue(WATER)).isEqualTo(zebraSolution.getValue(NORWEGIAN));
    }

    private void printSolution(Solution<Enum<?>> sol) {
        String[][] d = new String[5][6];

        preparePrint(0, Nation.class, sol, d);
        preparePrint(1, Color.class, sol, d);
        preparePrint(2, Smoke.class, sol, d);
        preparePrint(3, Pet.class, sol, d);
        preparePrint(4, Drink.class, sol, d);

        for (int y = 0; y < 5; y++) {
            System.out.println(format("%6s: %14s%14s%14s%14s%14s", d[y][0], d[y][1], d[y][2], d[y][3], d[y][4], d[y][5]));
        }
    }

    private void preparePrint(int y, Class<? extends Enum<?>> symbols, Solution<Enum<?>> sol, String[][] d) {
        d[y][0] = symbols.getSimpleName();
        for (Enum<?> symbol : symbols.getEnumConstants()) {
            d[y][sol.getValue(symbol)] = symbol.name();
        }
    }
}
