package day02part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class GPT4o {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of("input/day2.txt"));
            long safeReports = lines.stream()
                    .map(GPT4o::parseLevels)
                    .filter(GPT4o::isSafeReport)
                    .count();

            System.out.println("Safe reports: " + safeReports);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static List<Integer> parseLevels(String line) {
        return List.of(line.split(" ")).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static boolean isSafeReport(List<Integer> levels) {
        if (levels.size() < 2)
            return true; // A single level is trivially safe

        boolean increasing = true, decreasing = true;
        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);
            if (diff < -3 || diff > 3 || diff == 0)
                return false; // Violates the difference constraint
            if (diff > 0)
                decreasing = false; // If increasing, it's not decreasing
            if (diff < 0)
                increasing = false; // If decreasing, it's not increasing
        }

        return increasing || decreasing; // Must be strictly increasing or decreasing
    }
}
