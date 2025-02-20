package day02part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GPTo3MiniHigh {

    public static void main(String[] args) {
        try {
            long safeCount = Files.lines(Path.of("input/day2.txt"))
                    .filter(line -> !line.trim().isEmpty())
                    .filter(GPTo3MiniHigh::isReportSafe)
                    .count();
            System.out.println("Safe reports with Dampener: " + safeCount);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Returns true if the report represented by the given line is safe according to
     * the rules:
     * - The report is safe if its levels are either all increasing or all
     * decreasing,
     * and every adjacent difference is between 1 and 3 (inclusive).
     * - If the report is not safe, then it is safe if removing exactly one level
     * makes it safe.
     */
    private static boolean isReportSafe(String line) {
        String[] tokens = line.trim().split("\\s+");
        if (tokens.length < 2) {
            return false;
        }
        int[] levels = new int[tokens.length];
        try {
            for (int i = 0; i < tokens.length; i++) {
                levels[i] = Integer.parseInt(tokens[i]);
            }
        } catch (NumberFormatException e) {
            // If there's a malformed number, consider the report unsafe.
            return false;
        }

        // Check if the report is already safe.
        if (isSafe(levels)) {
            return true;
        }

        // Try removing one level at a time to see if the report becomes safe.
        for (int i = 0; i < levels.length; i++) {
            int[] reduced = new int[levels.length - 1];
            System.arraycopy(levels, 0, reduced, 0, i);
            if (i < levels.length - 1) {
                System.arraycopy(levels, i + 1, reduced, i, levels.length - i - 1);
            }
            if (isSafe(reduced)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the given array of levels is safe, meaning:
     * - It contains at least two levels.
     * - The difference between every adjacent pair is between 1 and 3 (inclusive).
     * - The differences are all strictly increasing or all strictly decreasing.
     */
    private static boolean isSafe(int[] levels) {
        if (levels.length < 2) {
            return false;
        }
        int diff = levels[1] - levels[0];
        if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
            return false;
        }
        boolean increasing = diff > 0;
        for (int i = 1; i < levels.length; i++) {
            int currentDiff = levels[i] - levels[i - 1];
            if (Math.abs(currentDiff) < 1 || Math.abs(currentDiff) > 3) {
                return false;
            }
            if ((increasing && currentDiff <= 0) || (!increasing && currentDiff >= 0)) {
                return false;
            }
        }
        return true;
    }
}
