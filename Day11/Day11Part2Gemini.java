package Day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11Part2Gemini {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput("input/day11.txt");
        Map<Long, Long> stones = parseStonesToMap(lines.get(0));

        for (int i = 0; i < 75; i++) {
            stones = blink(stones);
        }

        long totalCount = stones.values().stream().reduce(0L, Long::sum);
        System.out.println("Number of stones after 75 blinks: " + totalCount);
    }

    private static Map<Long, Long> blink(Map<Long, Long> stones) {
        Map<Long, Long> newStones = new HashMap<>();
        for (Map.Entry<Long, Long> entry : stones.entrySet()) {
            long stone = entry.getKey();
            long count = entry.getValue();

            if (stone == 0) {
                newStones.merge(1L, count, Long::sum);
            } else if (String.valueOf(stone).length() % 2 == 0) {
                String stoneStr = String.valueOf(stone);
                int mid = stoneStr.length() / 2;
                long left = Long.parseLong(stoneStr.substring(0, mid));
                long right = Long.parseLong(stoneStr.substring(mid));
                newStones.merge(left, count, Long::sum);
                newStones.merge(right, count, Long::sum);
            } else {
                newStones.merge(stone * 2024, count, Long::sum);
            }
        }
        return newStones;
    }

    private static Map<Long, Long> parseStonesToMap(String line) {
        Map<Long, Long> stones = new HashMap<>();
        for (String s : line.split(" ")) {
            long stone = Long.parseLong(s);
            stones.merge(stone, 1L, Long::sum);
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
