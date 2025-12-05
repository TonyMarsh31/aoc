package org.thermoweb.aoc.days;

import java.lang.Override;
import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;

// A simple helper for the range logic
record Range(long min, long max)implements Comparable<Range> {
    public boolean contains(long value) {
        return value >= min && value <= max;
    }

    // 1. Helper to check overlap
    // (min <= other.max) check handles touching ranges (e.g., 1-5 and 5-10)
    // Adjust to < if you only want strict overlaps.
    public boolean overlaps(Range other) {
        return this.min <= other.max && other.min <= this.max;
    }

    // 2. Helper to merge two ranges
    public Range merge(Range other) {
        return new Range(
                Math.min(this.min, other.min),
                Math.max(this.max, other.max)
        );
    }

    // 3. Required for sorting
    @Override
    public int compareTo(Range other) {
        return Long.compare(this.min, other.min);
    }
}

// The status record you requested
record SeedStatus(long seed, boolean isValid) {
}

@DaySolver(5)
public class Day5 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        // PART 1: Parse Ranges into an Immutable List
        // We use .toList() (Java 16+) instead of reduce.
        // It's the standard way to "collect" stream elements.
        List<Range> ranges = input.lines()
                .takeWhile(line -> !line.isBlank())
                .map(line -> {
                    String[] split = line.split("-");
                    return new Range(Long.parseLong(split[0]), Long.parseLong(split[1]));
                })
                .toList(); // Returns an unmodifiable List

        int num = 0;

        // PART 2: Process Seeds -> Map to Record -> Count
        long validCount = input.lines()
                .dropWhile(line -> !line.isBlank())
                .skip(1) // Skip the blank line
                .mapToLong(Long::parseLong) // Convert String to long stream
                .mapToObj(seed -> {
                    // Determine if this seed is in ANY range
                    boolean isValid = ranges.stream().anyMatch(r -> r.contains(seed));
                    return new SeedStatus(seed, isValid);
                })
                // Now you have a Stream<SeedStatus> [(1, true), (5, false)...]

                // If you just want the final count:
                .filter(SeedStatus::isValid)
                .count();

        System.out.println("Valid seeds found: " + validCount);

        return Optional.of(BigInteger.valueOf(validCount));

    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        List<Range> ranges = input.lines()
                .takeWhile(line -> !line.isBlank())
                .map(line -> {
                    String[] split = line.split("-");
                    return new Range(Long.parseLong(split[0]), Long.parseLong(split[1]));
                })
                .toList(); // Returns an unmodifiable List


        // solving overlap
        List<Range> nonOverlappingRanges = ranges.stream()
                .sorted()
                // We use a custom collect to maintain the "last added range" state
                .collect(
                        ArrayList::new,
                        (list, current) -> {
                            if (list.isEmpty()) {
                                list.add(current);
                            } else {
                                Range last = list.get(list.size() - 1);
                                if (last.overlaps(current)) {
                                    // Start of 'current' is inside 'last', so we merge/extend
                                    list.set(list.size() - 1, last.merge(current));
                                } else {
                                    // No overlap, just add it as a new distinct range
                                    list.add(current);
                                }
                            }
                        },
                        ArrayList::addAll // Combiner for parallel streams
                );

        nonOverlappingRanges.forEach(System.out::println);
        long count = nonOverlappingRanges.stream().mapToLong(e -> e.max() - e.min() + 1).sum();
        return Optional.of(BigInteger.valueOf(count));


    }
}
