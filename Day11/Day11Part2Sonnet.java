package Day11;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11Part2Sonnet {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("input/day11.txt"));
        List<String> stones = parseInput(input);
        long result = countStonesAfterBlinks(stones, 75);
        System.out.println("Number of stones after 75 blinks: " + result);
    }

    private static List<String> parseInput(String input) {
        return List.of(input.trim().split("\\s+"));
    }

    private static long countStonesAfterBlinks(List<String> initialStones, int targetBlinks) {
        // Key observation 1: Each stone's evolution is independent
        // Key observation 2: Stone patterns might repeat
        Map<String, List<Long>> stonePatternCache = new HashMap<>();
        long totalStones = 0;

        // Process each initial stone independently
        for (String stone : initialStones) {
            totalStones += processStoneOptimized(stone, targetBlinks, stonePatternCache);
        }

        return totalStones;
    }

    private static long processStoneOptimized(String stone, int targetBlinks, Map<String, List<Long>> patternCache) {
        // Check if we've seen this pattern before
        String cacheKey = stone;
        if (patternCache.containsKey(cacheKey)) {
            List<Long> pattern = patternCache.get(cacheKey);
            int cycleLength = pattern.size();
            int remainingBlinks = targetBlinks % cycleLength;
            return pattern.get(remainingBlinks - 1);
        }

        // Store the number of stones at each blink for this initial stone
        List<Long> stonesAtEachBlink = new ArrayList<>();
        List<String> currentStones = List.of(stone);

        for (int blink = 0; blink < targetBlinks; blink++) {
            currentStones = processOneBlink(currentStones);
            stonesAtEachBlink.add((long) currentStones.size());

            // Look for a repeating pattern
            if (hasRepeatingPattern(stonesAtEachBlink)) {
                patternCache.put(cacheKey, extractPattern(stonesAtEachBlink));
                return processStoneOptimized(stone, targetBlinks, patternCache); // Retry with cached pattern
            }
        }

        return currentStones.size();
    }

    private static boolean hasRepeatingPattern(List<Long> numbers) {
        int size = numbers.size();
        if (size < 4)
            return false; // Need at least a few numbers to detect a pattern

        for (int patternLength = 1; patternLength <= size / 2; patternLength++) {
            boolean isPattern = true;
            for (int i = size - patternLength; i < size; i++) {
                if (!numbers.get(i).equals(numbers.get(i - patternLength))) {
                    isPattern = false;
                    break;
                }
            }
            if (isPattern)
                return true;
        }
        return false;
    }

    private static List<Long> extractPattern(List<Long> numbers) {
        int size = numbers.size();
        for (int patternLength = 1; patternLength <= size / 2; patternLength++) {
            boolean isPattern = true;
            for (int i = size - patternLength; i < size; i++) {
                if (!numbers.get(i).equals(numbers.get(i - patternLength))) {
                    isPattern = false;
                    break;
                }
            }
            if (isPattern) {
                return numbers.subList(size - patternLength, size);
            }
        }
        return numbers; // Fallback if no pattern found
    }

    private static List<String> processOneBlink(List<String> stones) {
        List<String> newStones = new ArrayList<>();

        for (String stone : stones) {
            if (stone.equals("0")) {
                newStones.add("1");
            } else if (stone.length() % 2 == 0) {
                int halfLength = stone.length() / 2;
                String leftHalf = stone.substring(0, halfLength);
                String rightHalf = stone.substring(halfLength);

                leftHalf = removeLeadingZeros(leftHalf);
                rightHalf = removeLeadingZeros(rightHalf);

                newStones.add(leftHalf);
                newStones.add(rightHalf);
            } else {
                long newValue = Long.parseLong(stone) * 2024L;
                newStones.add(String.valueOf(newValue));
            }
        }

        return newStones;
    }

    private static String removeLeadingZeros(String number) {
        String result = number.replaceFirst("^0+", "");
        return result.isEmpty() ? "0" : result;
    }
}
