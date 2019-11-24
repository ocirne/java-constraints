package de.enricopilz.constraints.api;

import org.junit.Test;

import static org.junit.Assert.fail;

public class NonogramIT {

    /*
          1 1 1 1
      1 1 . . . .
        2 . . . .
     */
    @Test
    public void testGuessNonogram() {
        fail("TODO");
    }

    @Test
    public void testWikipediaP() {
        // size 8 cols x 11 rows
        int[][] rowNumbers = new int[][] {
                {0},
                {4},
                {6},
                {2, 2},
                {2, 2},
                {6},
                {4},
                {2},
                {2},
                {2},
                {0}};
        int[][] colNumbers = new int[][] {
                {0},
                {9},
                {9},
                {2, 2},
                {2, 2},
                {4},
                {4},
                {0}};
        String expectedSolution =
        "........" +
        ".####..." +
        ".######." +
        ".##..##." +
        ".##..##." +
        ".######." +
        ".####..." +
        ".##....." +
        ".##....." +
        ".##....." +
        ".##....." +
        "........";
    }
}
