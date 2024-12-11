package Day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day11Part14o {

    public static void main(String[] args) throws IOException {
        // Load input from file
        List<String> lines = Files.readAllLines(Paths.get("input/day11.txt"));
        List<Integer> stones = parseInput(lines);

        int blinks = 25; // Number of blinks
        List<Integer> result = simulateBlinks(stones, blinks);

        System.out.println("Number of stones after " + blinks + " blinks: " + result.size());
    }

    private static List<Integer> parseInput(List<String> lines) {
        List<Integer> stones = new ArrayList<>();
        for (String line : lines) {
            for (String num : line.split(" ")) {
                stones.add(Integer.parseInt(num));
            }
        }
        return stones;
    }

    private static List<Integer> simulateBlinks(List<Integer> stones, int blinks) {
        for (int i = 0; i < blinks; i++) {
            stones = blink(stones);
        }
        return stones;
    }

    private static List<Integer> blink(List<Integer> stones) {
        List<Integer> newStones = new ArrayList<>();

        for (int stone : stones) {
            if (stone == 0) {
                newStones.add(1);
            } else if (hasEvenDigits(stone)) {
                splitStone(stone, newStones);
            } else {
                newStones.add(stone * 2024);
            }
        }

        return newStones;
    }

    private static boolean hasEvenDigits(int number) {
        int digits = String.valueOf(number).length();
        return digits % 2 == 0;
    }

    private static void splitStone(int stone, List<Integer> newStones) {
        String stoneStr = String.valueOf(stone);
        int mid = stoneStr.length() / 2;
        int left = Integer.parseInt(stoneStr.substring(0, mid));
        int right = Integer.parseInt(stoneStr.substring(mid));

        newStones.add(left);
        newStones.add(right);
    }
}
