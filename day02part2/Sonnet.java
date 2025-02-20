package day02part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Sonnet {
    public static void main(String[] args) throws Exception {
        var result = Files.lines(Path.of("input/day2.txt"))
                .map(Sonnet::parseLevels)
                .filter(Sonnet::isSafeWithDampener)
                .count();

        System.out.println("Number of safe reports with Problem Dampener: " + result);
    }

    private static int[] parseLevels(String line) {
        return Arrays.stream(line.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static boolean isSafeWithDampener(int[] levels) {
        // First check if it's safe without dampener
        if (isSafeReport(levels)) {
            return true;
        }

        // Try removing each level one at a time
        for (int i = 0; i < levels.length; i++) {
            int[] dampened = createArrayWithoutIndex(levels, i);
            if (isSafeReport(dampened)) {
                return true;
            }
        }

        return false;
    }

    private static int[] createArrayWithoutIndex(int[] original, int indexToSkip) {
        int[] result = new int[original.length - 1];
        for (int i = 0, j = 0; i < original.length; i++) {
            if (i != indexToSkip) {
                result[j++] = original[i];
            }
        }
        return result;
    }

    private static boolean isSafeReport(int[] levels) {
        if (levels.length < 2) {
            return true;
        }

        boolean isIncreasing = levels[1] > levels[0];
        int previousLevel = levels[0];

        for (int i = 1; i < levels.length; i++) {
            int currentLevel = levels[i];
            int difference = currentLevel - previousLevel;

            // Check if difference is within valid range (1-3)
            if (Math.abs(difference) < 1 || Math.abs(difference) > 3) {
                return false;
            }

            // Check if direction remains consistent
            if (isIncreasing && difference < 0 || !isIncreasing && difference > 0) {
                return false;
            }

            previousLevel = currentLevel;
        }

        return true;
    }
}
