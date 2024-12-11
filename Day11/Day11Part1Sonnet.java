package Day11;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day11Part1Sonnet {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("input/day11.txt"));
        List<String> stones = parseInput(input);
        int result = countStonesAfterBlinks(stones, 25);
        System.out.println("Number of stones after 25 blinks: " + result);
    }

    private static List<String> parseInput(String input) {
        return List.of(input.trim().split("\\s+"));
    }

    private static int countStonesAfterBlinks(List<String> initialStones, int numBlinks) {
        List<String> stones = new ArrayList<>(initialStones);

        for (int blink = 0; blink < numBlinks; blink++) {
            stones = processOneBlink(stones);
        }

        return stones.size();
    }

    private static List<String> processOneBlink(List<String> stones) {
        List<String> newStones = new ArrayList<>();

        for (String stone : stones) {
            if (stone.equals("0")) {
                // Rule 1: Replace 0 with 1
                newStones.add("1");
            } else if (stone.length() % 2 == 0) {
                // Rule 2: Split even-length numbers in half
                int halfLength = stone.length() / 2;
                String leftHalf = stone.substring(0, halfLength);
                String rightHalf = stone.substring(halfLength);

                // Remove leading zeros but keep single "0"
                leftHalf = removeLeadingZeros(leftHalf);
                rightHalf = removeLeadingZeros(rightHalf);

                newStones.add(leftHalf);
                newStones.add(rightHalf);
            } else {
                // Rule 3: Multiply by 2024
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
