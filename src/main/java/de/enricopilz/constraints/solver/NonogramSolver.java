package de.enricopilz.constraints.solver;

public class NonogramSolver {

    private static final char BLACK = '#';
    private static final char WHITE = ' ';

    public String solveLine(String input, int[] numbers) {
        return String.valueOf(solveLine(input.toCharArray(), numbers));
    }

    public char[] solveLine(char[] output, int[] numbers) {
        if (numbers.length == 0) {
            for (int i = 0; i < output.length; i++) {
                output[i] = WHITE;
            }
            return output;
        }
        // untergrenzen
        int[] ug = new int[numbers.length];
        ug[0] = 0;
        for (int i = 1; i < numbers.length; i++) {
            ug[i] = ug[i-1] + numbers[i-1] + 1;
        }
        // obergrenzen
        int[] og = new int[numbers.length];
        og[numbers.length-1] = output.length;
        for (int i = numbers.length - 2; i >= 0; i--) {
            og[i] = og[i+1] - numbers[i+1] - 1;
        }

        for (int i = 0; i < numbers.length; i++) {
            System.out.println("index: " + i + ", zahl: " + numbers[i] + ", von " + ug[i] + " bis " + og[i]);
            if (og[i] - ug[i] < numbers[i]) {
                throw new IllegalArgumentException("nicht möglich");
            }
            if (og[i] - ug[i] == numbers[i]) {
                if (ug[i] > 0) {
                    output[ug[i] - 1] = WHITE;
                }
                for (int j = ug[i]; j < og[i]; j++) {
                    output[j] = BLACK;
                }
                if (og[i] < output.length) {
                    output[og[i]] = WHITE;
                }
            }
        }
        return output;
    }
}
