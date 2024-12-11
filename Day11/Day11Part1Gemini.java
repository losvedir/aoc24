package Day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day11Part1Gemini {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput("input/day11.txt");
        List<Long> stones = parseStones(lines.get(0));

        for (int i = 0; i < 25; i++) {
            stones = blink(stones);
        }

        System.out.println("Number of stones after 25 blinks: " + stones.size());
    }

    private static List<Long> blink(List<Long> stones) {
        List<Long> newStones = new ArrayList<>();
        for (long stone : stones) {
            if (stone == 0) {
                newStones.add(1L);
            } else if (String.valueOf(stone).length() % 2 == 0) {
                String stoneStr = String.valueOf(stone);
                int mid = stoneStr.length() / 2;
                newStones.add(Long.parseLong(stoneStr.substring(0, mid)));
                newStones.add(Long.parseLong(stoneStr.substring(mid)));
            } else {
                newStones.add(stone * 2024);
            }
        }
        return newStones;
    }

    private static List<Long> parseStones(String line) {
        List<Long> stones = new ArrayList<>();
        for (String s : line.split(" ")) {
            stones.add(Long.parseLong(s));
        }
        return stones;
    }

    private static List<String> readInput(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
