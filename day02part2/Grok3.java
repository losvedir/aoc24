package day02part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Grok3 {
    public static void main(String[] args) {
        try {
            long safeReports = Files.lines(Paths.get("input/day2.txt"))
                    .map(Grok3::parseReport)
                    .filter(Grok3::isSafeWithDampener)
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
            return true;
        }

        boolean increasing = levels[1] > levels[0];
        for (int i = 1; i < levels.length; i++) {
            int diff = levels[i] - levels[i - 1];
            int absDiff = Math.abs(diff);

            if (absDiff < 1 || absDiff > 3) {
                return false;
            }

            if (increasing && diff <= 0) {
                return false;
            }
            if (!increasing && diff >= 0) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSafeWithDampener(int[] levels) {
        // If already safe, no need to use dampener
        if (isSafeReport(levels)) {
            return true;
        }

        // Try removing each level one at a time
        for (int i = 0; i < levels.length; i++) {
            int[] modified = new int[levels.length - 1];
            int pos = 0;

            // Copy all levels except the one at index i
            for (int j = 0; j < levels.length; j++) {
                if (j != i) {
                    modified[pos++] = levels[j];
                }
            }

            if (isSafeReport(modified)) {
                return true;
            }
        }

        return false;
    }
}
