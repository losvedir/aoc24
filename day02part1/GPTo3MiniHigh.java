package day02part1;

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
            System.out.println("Safe reports: " + safeCount);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static boolean isReportSafe(String line) {
        // Split the line by one or more spaces and convert to integers.
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
            // In case of malformed number input.
            return false;
        }

        // Determine the first difference.
        int diff = levels[1] - levels[0];
        if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
            return false;
        }
        // Determine whether the report is supposed to be increasing.
        boolean increasing = diff > 0;

        // Check each pair of adjacent levels.
        for (int i = 1; i < levels.length; i++) {
            int currentDiff = levels[i] - levels[i - 1];
            // Each adjacent difference must be between 1 and 3 (inclusive).
            if (Math.abs(currentDiff) < 1 || Math.abs(currentDiff) > 3) {
                return false;
            }
            // The sign must remain consistent.
            if ((increasing && currentDiff <= 0) || (!increasing && currentDiff >= 0)) {
                return false;
            }
        }
        return true;
    }
}
