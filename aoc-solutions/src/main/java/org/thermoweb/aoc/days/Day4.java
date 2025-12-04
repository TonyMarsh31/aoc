package org.thermoweb.aoc.days;

import java.lang.Override;
import java.lang.String;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;

@DaySolver(4)
public class Day4 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        /*
        the key is how to express the eight adjacent positions procedurally
        got a count of that ,then when scan the firt line , i need care left and right
        then the sec line, i nned care left and right for curent line ,
        but this time , also current one is the previous line's bottm
        and previous line is the current index's top
        so the eight adjacent count =  my [left] + my [right]
        + top [itself] + top [left] + top [right]
        + bottm [itself] + bottm [left] + bottm [right]

          --- in every node i can parse it as a elenet of (index , boolean itself, boolean left , boolean right);
         till i scan the i+1 line , the i line is not ready to check ---

         in procedural perscept , i am iter the char
         the if current char is target , just update surrrand eight count
         should i update self count as well? no
         */
        int res = 0;
        List<String> lines = input.lines().toList();
        int[][] counts = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            // every line
            String line = lines.get(i);
            char[] chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                // every char of cur line
                if (chars[j] == '@') {
//                    counts[i][j] += 1; // self count

                    if (j - 1 >= 0) counts[i][j - 1] += 1;// left count
                    if (j + 1 < chars.length) counts[i][j + 1] += 1; // right count

                    if (i - 1 >= 0) {
                        counts[i - 1][j] += 1; // top count
                        if (j - 1 >= 0) counts[i - 1][j - 1] += 1; // top left count
                        if (j + 1 < chars.length) counts[i - 1][j + 1] += 1; // top right count
                    }

                    if (i + 1 < lines.size()) {
                        counts[i + 1][j] += 1;// bottom count
                        if (j - 1 >= 0) counts[i + 1][j - 1] += 1; // bottom left count
                        if (j + 1 < chars.length) counts[i + 1][j + 1] += 1; // bottom right count
                    }
                }
            }

            //cur line finished, previous line is ready to check
            if (i - 1 >= 0) {
                String pre_line = lines.get(i - 1);
                char[] pre_chars = pre_line.toCharArray();
                for (int j = 0; j < counts[i].length; j++) {
                    if (counts[i - 1][j] < 4 && pre_chars[j] == '@') {
                        res += 1;
                        System.out.println(i + "," + (j + 1));
                    }
                }
            }
            //if it's last line then check as well
            if (i == lines.size() - 1) {
                String check = lines.get(i);
                char[] check_chars = check.toCharArray();
                for (int j = 0; j < counts[i].length; j++) {
                    if (counts[i][j] < 4 && check_chars[j] == '@') {
                        res += 1;
                        System.out.println(i + "," + (j + 1));
                    }
                }
            }

        }

//        this is wrong casue  if it's not '@'  ,then it did not count
//        can access char[] anymore thus udate res during the char iteration
//        int res = 0;
//        for (int[] row : counts) {
//            for (int cell : row) {
//                if (cell <= 4) res += 1;
//                System.out.println(cell);
//            }
//        }

        return Optional.of(BigInteger.valueOf(res));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        int res = 0;
        int pre_res = 0;

        // 1. 初始化：把 List<String> 彻底转为 char[][]，方便修改
        List<String> lines = input.lines().toList();
        int rows = lines.size();
        int cols = lines.get(0).length();

        char[][] grid = new char[rows][cols];
        int[][] counts = new int[rows][cols];

        // 填充 grid 和初始 counts
        for (int r = 0; r < rows; r++) {
            grid[r] = lines.get(r).toCharArray();
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '@') {
                    // 这里的 addThing 逻辑和你之前的一样，初始化周围计数
                    // 注意：为了代码整洁，建议也把加分逻辑提取成 helper 方法
                    addThing(counts, r, c, rows, cols);
                }
            }
        }

        boolean changed = true;

        // 2. 主循环：只要上一轮有变化（changed == true），就继续扫
        while (changed) {
            changed = false; // 假设这一轮没变化

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    // 核心修复：直接检查 grid[r][c] 是否还是 '@'
                    if (grid[r][c] == '@' && counts[r][c] < 4) {

                        // A. 标记变化，移除计数
                        res += 1;
                        minusThing(counts, r, c); // 你的减分逻辑

                        // B. 【关键步骤】从地图上抹掉它！
                        grid[r][c] = '.';

                        // C. 标记发生了改变，需要再跑一轮
                        changed = true;
                    }
                }

//                System.out.println(res + "removed this time");
            }
        }

        return Optional.of(BigInteger.valueOf(res));
    }

//    @Override
    public Optional<BigInteger> myPartTwo(String input) {
        /*
        first though , involving io. rewrite the input ,
        ... there have to be an outer loop wrapping the slotion 1
        so what status change each loop ?  the count array
        so by removeing the @ , what will it effect the count array?
        just do the reverse , minus count arouhgt eight positon
        so in the res check loop , insteal of just updat the res, i should update the count as well
         */


        /*
        todo
        a loop that never break. turn out i have to , but for got to rewrite the input
        speend enough time today ,just using ai code , review it tomorrow
         */

        int pre_res = 0;
        int res = 0;
        List<String> lines = input.lines().toList();
        int[][] counts = new int[lines.size()][lines.get(0).length()];
        boolean skipAdd = false;
        for (int i = 0; i < lines.size(); i++) {
            // every line
            String line = lines.get(i);
            char[] chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                // every char of cur line

                // need a flag to skip the add part when aftre beging removeing
                if (!skipAdd) {
                    if (chars[j] == '@') {
//                    counts[i][j] += 1; // self count

                        if (j - 1 >= 0) counts[i][j - 1] += 1;// left count
                        if (j + 1 < chars.length) counts[i][j + 1] += 1; // right count

                        if (i - 1 >= 0) {
                            counts[i - 1][j] += 1; // top count
                            if (j - 1 >= 0) counts[i - 1][j - 1] += 1; // top left count
                            if (j + 1 < chars.length) counts[i - 1][j + 1] += 1; // top right count
                        }

                        if (i + 1 < lines.size()) {
                            counts[i + 1][j] += 1;// bottom count
                            if (j - 1 >= 0) counts[i + 1][j - 1] += 1; // bottom left count
                            if (j + 1 < chars.length) counts[i + 1][j + 1] += 1; // bottom right count
                        }
                    }
                }
            }

            //cur line finished, previous line is ready to check
            if (i - 1 >= 0) {
                String pre_line = lines.get(i - 1);
                char[] pre_chars = pre_line.toCharArray();
                for (int j = 0; j < counts[i].length; j++) {
                    if ( counts[i - 1][j] < 4  && pre_chars[j] == '@'&& counts[i-1][j] >0) {
                        res += 1;
//                        do the minus thing at
                        minusThing(counts, i - 1, j);
                    }
                }
            }
            //if it's last line then check as well
            if (i == lines.size() - 1) {
                String check = lines.get(i);
                char[] check_chars = check.toCharArray();
                for (int j = 0; j < counts[i].length; j++) {
                    if (counts[i][j] < 4 && check_chars[j] == '@' && counts[i][j] > 0) {
                        res += 1;
//                        do the minus thing at
                        minusThing(counts, i, j);
                    }
                }

//                if res is unchgeed after another round , then beak the loop
//                else contine with new flag, skip the add count part,since there is no new @
                if (pre_res != res) {
                    System.out.println(res + "removed this time");
                    pre_res = res;
                    i = -1; // reset the loop
                    skipAdd = true;
                }
            }

        }


        return Optional.of(BigInteger.valueOf(res));
    }

    private static int[][] minusThing(int[][] counts, int i, int j) {


//        if (j - 1 >= 0) counts[i][j - 1] -= 1;// left count
//        if (j + 1 < counts[0].length) counts[i][j + 1] -= 1; // right count
//
//        if (i - 1 >= 0) {
//            counts[i - 1][j] -= 1; // top count
//            if (j - 1 >= 0) counts[i - 1][j - 1] -= 1; // top left count
//            if (j + 1 < counts[0].length) counts[i - 1][j + 1] -= 1; // top right count
//        }
//
//        if (i + 1 < counts.length) {
//            counts[i + 1][j] -= 1;// bottom count
//            if (j - 1 >= 0) counts[i + 1][j - 1] -= 1; // bottom left count
//            if (j + 1 < counts[0].length) counts[i + 1][j + 1] -= 1; // bottom right count
//        }

//        check prevent sub zero
        // Top Row
        safeDecrement(counts, i - 1, j - 1);
        safeDecrement(counts, i - 1, j);
        safeDecrement(counts, i - 1, j + 1);

        // Middle Row
        safeDecrement(counts, i, j - 1);
        safeDecrement(counts, i, j + 1);

        // Bottom Row
        safeDecrement(counts, i + 1, j - 1);
        safeDecrement(counts, i + 1, j);
        safeDecrement(counts, i + 1, j + 1);


        return counts;


    }


    private static void safeDecrement(int[][] grid, int r, int c) {
        // 1. Bounds Check (is it inside the grid?)
        if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length) {
            // 2. Sub-zero Check (is it positive?)
            if (grid[r][c] > 0) {
                grid[r][c]--;
            }
            // Alternatively using Math.max:
            // grid[r][c] = Math.max(0, grid[r][c] - 1);
        }
    }

    // 简单的辅助方法，避免重复代码
    private void addThing(int[][] counts, int i, int j, int maxR, int maxC) {
        // 这里放你原来的那些 if (j-1>=0)... 逻辑，只是把 -= 换成 +=
        // ...

        if (j - 1 >= 0) counts[i][j - 1] += 1;// left count
        if (j + 1 < maxC) counts[i][j + 1] += 1; // right count

        if (i - 1 >= 0) {
            counts[i - 1][j] += 1; // top count
            if (j - 1 >= 0) counts[i - 1][j - 1] += 1; // top left count
            if (j + 1 < maxC) counts[i - 1][j + 1] += 1; // top right count
        }

        if (i + 1 < maxR) {
            counts[i + 1][j] += 1;// bottom count
            if (j - 1 >= 0) counts[i + 1][j - 1] += 1; // bottom left count
            if (j + 1 < maxC) counts[i + 1][j + 1] += 1; // bottom right count
        }
    }


}



