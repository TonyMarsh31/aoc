package org.thermoweb.aoc.days;

import java.lang.Override;
import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;

record Point(int index, int value) {
}

@DaySolver(3)
public class Day3 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        /*
         * 1. Find the max number in the line.
         * 2. Once the max is found, look for the second max number specifically located AFTER the max's index.
         * 3 .If there is no number after the max (e.g., max is the last digit), then look for the max number BEFORE it.
         * To optimize speed, I think we can store the second max while we are scanning to avoid a second pass."
         *
         *
         * since the logic depends heavily on order (before/after) and state (tracking max, tracking specific candidates),
         * the Stream API will not be the best tool here. Streams are designed to be stateless parallel pipes.
         * Forcing complex stateful logic (like "remember the second max only if it appeared after the first max")
         * into a Stream .reduce() is possible but very unreadable and hard to debug.
         *
         * just use a for loop
         */
        ArrayList<Integer> res_list = new ArrayList<>(100);

        input.lines().forEach(line -> {
            int globalMax = -1;
            int maxAfter = -1;   // The best number found AFTER the current globalMax
            int maxBefore = -1;  // The best number found BEFORE the current globalMax


            for (char c : line.toCharArray()) {
                int val = c - '0'; // Fast convert '5' -> 5

                if (val > globalMax) {
                    // New King found!
                    // 1. The old King retires to become the best "Before" candidate
                    maxBefore = globalMax;

                    // 2. Crown the new King
                    globalMax = val;

                    // 3. Reset the "After" list (nothing is after the new King yet)
                    maxAfter = -1;
                } else {
                    // We are strictly AFTER the current King.
                    // Is this the best "After" candidate we've seen so far?
                    if (val > maxAfter) {
                        maxAfter = val;
                    }
                }
            }
            if (maxAfter != -1) {
                res_list.add(Integer.valueOf(globalMax + "" + maxAfter));
            } else {
                // the global max happens to be the last digit in the line.
                res_list.add(Integer.valueOf(maxBefore + "" + globalMax));
            }
        });

        System.out.println();
        int res = res_list.stream().mapToInt(Integer::intValue).sum();

        return Optional.of(BigInteger.valueOf(res));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        /*
         index from 0 to length -11 , find the global max as the first digit
        then take max after  as the second digit,if it occur multiple time ,take as following digit as well
        ....
        that does not work in the case of 8888888891111111118  if y take 9 as first dig and 8 as sec dig
        being constrained by the remaining length of the string
        so i need at least (12 - 2) slot left after the sec dig ,
        that's  (goal.length - seq) left after the cur seq dig , if that need doe's fit, i need to take the next max dig
        so what state do i need to keep ? , max ,index , howto deal with multiple max value  with different index
        emm.. just map the char list to  map <number , list of index> fist
        then iter the number descingly , ,keep trick the index ,take number after current index only
        if run out of the slot, then reset ,take next number as next dig ,
        but the dig after that ,still sreaching from begining , so a count might be needed ?
        that's it , it might be the solution
         */

        List<BigInteger> results = new ArrayList<>();

        input.lines().forEach(line -> {
            // 1. Pre-process: Map digit -> List of indices [0, 5, 12...]
            // Using an array of lists is faster than a HashMap for digits 0-9
            List<Integer>[] positions = new List[10];
            for (int i = 0; i < 10; i++) {
                positions[i] = new ArrayList<>();
            }

            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int digit = chars[i] - '0';
                positions[digit].add(i);
            }

            // 2. The State tracking
            int currentIdx = -1; // We haven't picked any index yet
            StringBuilder sb = new StringBuilder();

            // We need exactly 12 digits
            for (int i = 0; i < 12; i++) {
                int digitsNeededAfterThis = 12 - 1 - i;

                // 3. Greedy Choice: Try 9, then 8, then 7...
                for (int d = 9; d >= 0; d--) {
                    boolean foundCandidate = false;

                    // Look through all occurrences of this digit
                    for (int idx : positions[d]) {

                        // Rule A: Must be after the previous digit we picked
                        if (idx <= currentIdx) {
                            continue;
                        }

                        // Rule B (Your Logic): Do we have enough string left?
                        // "remaining length of string" >= "slots we need to fill after this"
                        int charsRemainingInString = line.length() - 1 - idx;

                        if (charsRemainingInString >= digitsNeededAfterThis) {
                            // We found the best digit for this slot!
                            sb.append(d);
                            currentIdx = idx;
                            foundCandidate = true;
                            break; // Stop, and looking at other indices for this digit
                        }
                    }

                    if (foundCandidate) {
                        break; // Stop, and looking for smaller digits (8, 7...)
                    }
                }
            }
            results.add(new BigInteger(sb.toString()));
        });

        // Sum them all up
        BigInteger total = results.stream() .reduce(BigInteger.ZERO, BigInteger::add);

        return Optional.of(total);


    }
}
