package day02part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Grok {
    public static void main(String[] args) {
        try {
            long safeReports = Files.lines(Paths.get("input/day2.txt"))
                    .map(Grok::parseReport)
                    .filter(Grok::isSafeReport)
                    .count();

            System.out.println("Number of safe reports: " + safeReports);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static int[] parseReport(String line) {
        return Arrays.stream(line.split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static boolean isSafeReport(int[] levels) {
        if (levels.length < 2) {
            return true; // Single level or empty is trivially safe
        }

        // Check if strictly increasing or decreasing
        boolean increasing = levels[1] > levels[0];
        for (int i = 1; i < levels.length; i++) {
            int diff = levels[i] - levels[i - 1];

            // Check difference is between 1 and 3
            int absDiff = Math.abs(diff);
            if (absDiff < 1 || absDiff > 3) {
                return false;
            }

            // Check consistent direction
            if (increasing && diff <= 0) {
                return false;
            }
            if (!increasing && diff >= 0) {
                return false;
            }
        }

        return true;
    }
}
