package day02part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GPT4o {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day2.txt"));
        long safeCount = lines.stream()
                .map(GPT4o::parseLevels)
                .filter(GPT4o::isSafeOrCanBeMadeSafe)
                .count();

        System.out.println("Safe reports with Problem Dampener: " + safeCount);
    }

    private static List<Integer> parseLevels(String line) {
        return List.of(line.split(" ")).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static boolean isSafeOrCanBeMadeSafe(List<Integer> levels) {
        if (isSafe(levels)) {
            return true;
        }
        // Try removing each level and check if it becomes safe
        for (int i = 0; i < levels.size(); i++) {
            List<Integer> modifiedLevels = new ArrayList<>(levels); // Copy original list
            modifiedLevels.remove(i); // Remove one element

            if (isSafe(modifiedLevels)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSafe(List<Integer> levels) {
        if (levels.size() < 2) {
            return false;
        }

        boolean increasing = true, decreasing = true;
        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);
            if (diff < -3 || diff > 3 || diff == 0) {
                return false; // Violates the difference constraint
            }
            if (diff > 0) {
                decreasing = false;
            }
            if (diff < 0) {
                increasing = false;
            }
        }
        return increasing || decreasing;
    }
}
