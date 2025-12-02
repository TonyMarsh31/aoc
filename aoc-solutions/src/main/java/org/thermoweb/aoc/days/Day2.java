package org.thermoweb.aoc.days;

import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DayRunner;
import org.thermoweb.aoc.DaySolver;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DaySolver(2)
public class Day2 implements Day {

    public record Range(long start, long end) {
    }

    @Override
    public Optional<BigInteger> partOne(String input) {
        List<Range> rangeList = Arrays.stream(input.split(","))
                .map(s -> s.split("-"))
                .map(part -> new Range(Long.parseLong(part[0]), Long.parseLong(part[1])))
                .collect(Collectors.toList());

        long maxEndValue = rangeList.stream()
                .mapToLong(Range::end)
                .max()
                .orElse(0);

        System.out.println(maxEndValue);
        rangeList.sort(Comparator.comparingLong(r -> r.start));
        rangeList.forEach(System.out::println);

        ArrayList<Long> resultList = new ArrayList<>(100);

        int index = 0;
        for (int i = 1; ; i++) {
            long j = Long.parseLong(i + "" + i);

            if (j > maxEndValue) break;

            while (index < rangeList.size() && rangeList.get(index).end() < j) {
                index++;
            }
            if (index >= rangeList.size()) break;

            Range currentRange = rangeList.get(index);

            if (j >= currentRange.start() && j <= currentRange.end) {
                resultList.add(j);
//                System.out.println("check " + j + " in range of " + currentRange);
            } else {
                // j is smaller than the start.
                // Do nothing with 'index'. Just let 'i' increment to get a bigger 'j'.
//                System.out.println("Value " + j + " falls in the gap before " + currentRange);
                continue;
            }

        }
        long res = resultList.stream().mapToLong(Long::longValue).sum();
        return Optional.of(BigInteger.valueOf(res));

    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        List<Range> rangeList = Arrays.stream(input.split(","))
                .map(s -> s.split("-"))
                .map(part -> new Range(Long.parseLong(part[0]), Long.parseLong(part[1])))
                .collect(Collectors.toList());

        long maxEndValue = rangeList.stream()
                .mapToLong(Range::end)
                .max()
                .orElse(0);

        System.out.println(maxEndValue);
        rangeList.sort(Comparator.comparingLong(r -> r.start));
        rangeList.forEach(System.out::println);

        ArrayList<Long> resultList = new ArrayList<>(100);

        int index = 0;
        int dup = 2;
        for (int i = 1; ; i++) {

            long j = generateRepeatedNumber(i, dup);
//            System.out.println("Generated " + j);

            if (j > maxEndValue) {
                if (generateRepeatedNumber(1, dup + 1) > maxEndValue) {
                    break;
                } else {
                    // reset and continue with dup++
//                    i = 1;
                    // I learn a lesson, forgot about the continue key word imply an increment itself
                    // (i++) happens immediately after the continue key word and before the next iteration starts.
                    // thus when it comes to reset , i got reset it to 0(init value -1 ) instead of the init value of 1
                    i = 0;
                    index = 0;
                    dup++;
                    System.out.println("Resetting to 1,dup = " + dup);
                    continue;
                }
            }

            while (index < rangeList.size() && rangeList.get(index).end() < j) {
                index++;
            }
            if (index >= rangeList.size()) {
                if (generateRepeatedNumber(1, dup + 1) > maxEndValue) {
                    break;
                } else {
                    // reset and continue with dup++
//                    i = 1;
                    i = 0;
                    index = 0;
                    dup++;
                    System.out.println("Resetting to 1,dup = " + dup);
                    continue;
                }
            }

            Range currentRange = rangeList.get(index);

            if (j >= currentRange.start() && j <= currentRange.end()) {
                System.out.println("Found " + j + " in range of " + currentRange);
                resultList.add(j);
            }

        }
        long res = resultList.stream().mapToLong(Long::longValue).distinct().sum();
        resultList.forEach(System.out::println);
        return Optional.of(BigInteger.valueOf(res));
    }


    public static long generateRepeatedNumber(int baseNumber, int count) {
        // 1. Convert the number to a string
        String s = String.valueOf(baseNumber);

        // 2. Repeat it 'count' times
        // This is much cleaner than a for-loop concatenation
        String repeated = s.repeat(count);

        // 3. Parse back to long
        return Long.parseLong(repeated);
    }

}
