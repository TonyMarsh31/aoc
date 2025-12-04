package org.thermoweb.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.Exception;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DayRunner;

class Day4Test {
    private final Day day = new Day4();

    @Test
    void test_part_one() throws Exception {
//        assertEquals(Optional.of(BigInteger.valueOf(13L)), day.partOne(DayRunner.getExample(4)));
        assertEquals(Optional.of(BigInteger.valueOf(13L)), day.partOne(DayRunner.getExample(4)));
    }

    @Test
    void test_part_two() throws Exception {
//        assertEquals(Optional.of(BigInteger.valueOf(43L)), day.partTwo(DayRunner.getExample(4)));
        System.out.println(day.partTwo(DayRunner.getInput(4)));
    }
}
