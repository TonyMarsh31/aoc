package org.thermoweb.aoc.days;

import java.lang.Override;
import java.lang.String;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;

import javax.swing.text.html.Option;

@DaySolver(1)
public class Day1 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        final AtomicInteger count = new AtomicInteger(0);
        int init = 50;
        int minRange = 0;
        int maxRange = 99;
        String finalNum = input.lines()
                .reduce(
                        String.valueOf(init),
                        (currentNum, cmd) -> {
                            char direction = cmd.charAt(0);
                            int distance = Integer.parseInt(cmd.substring(1));
                            int next = Integer.parseInt(currentNum);
                            if (direction == 'L') {
                                next -= distance;
                                while (next < minRange) {
                                    next += 100;
                                }
                            } else if (direction == 'R') {
                                next += distance;
                                while (next > maxRange) {
                                    // better use mod
                                    next -= 100;
                                }
                            }
                            if (next == 0) {
                                count.getAndIncrement();
                            }
                            return String.valueOf(next);
                        }
                );

        return Optional.of(BigInteger.valueOf(count.get()));

    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        // We use a long just in case, though int is probably fine
        final AtomicInteger count = new AtomicInteger(0);
        int init = 50;

        input.lines().reduce(
                String.valueOf(init),
                (currentNumStr, cmd) -> {
                    char direction = cmd.charAt(0);
                    int distance = Integer.parseInt(cmd.substring(1));
                    int currentPos = Integer.parseInt(currentNumStr);

                    // SIMULATION: Just move 1 step at a time!
                    for (int i = 0; i < distance; i++) {
                        if (direction == 'R') {
                            currentPos++;
                            // If we hit 100, we actually wrap to 0.
                            // That counts as hitting 0!
                            if (currentPos == 100) {
                                currentPos = 0;
                                count.incrementAndGet();
                            }
                        } else { // Direction is 'L'
                            currentPos--;
                            // If we go below 0, we wrap to 99.
                            // We do NOT count this, because we just LEFT 0, we didn't ARRIVE at it.
                            if (currentPos < 0) {
                                currentPos = 99;
                            }
                            // If the step made us land on 0 (e.g. went from 1 to 0)
                            else if (currentPos == 0) {
                                count.incrementAndGet();
                            }
                        }
                    }
                    return String.valueOf(currentPos);
                }
        );

        return Optional.of(BigInteger.valueOf(count.get()));

    }
}
