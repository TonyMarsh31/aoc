package org.thermoweb.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.Exception;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DayRunner;

class Day2Test {
    private final Day day = new Day2();

    @Test
    void test_part_one() throws Exception {
        System.out.println(
                day.partTwo(DayRunner.getInput(2))

        );
//        assertEquals(Optional.of(BigInteger.valueOf(1227775554)), day.partOne(DayRunner.getExample(2)));
    }

    @Test
    void test_part_two() throws Exception {
        assertEquals(Optional.of(BigInteger.valueOf(4174379265L)), day.partTwo(DayRunner.getExample(2)));
    }
}
