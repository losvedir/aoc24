package Day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11Part2Opus {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("input/day11.txt"));
        Map<Long, Long> stones = Arrays.stream(input.split(" "))
                .map(Long::parseLong)
                .collect(Collectors.groupingBy(num -> num, Collectors.counting()));

        int blinks = 75;
        for (int i = 0; i < blinks; i++) {
            stones = transformStones(stones);
        }

        long totalStones = stones.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("Number of stones after " + blinks + " blinks: " + totalStones);
    }

    private static Map<Long, Long> transformStones(Map<Long, Long> stones) {
        Map<Long, Long> transformedStones = new HashMap<>();

        for (Map.Entry<Long, Long> entry : stones.entrySet()) {
            long stone = entry.getKey();
            long count = entry.getValue();

            if (stone == 0) {
                transformedStones.merge(1L, count, Long::sum);
            } else if (String.valueOf(stone).length() % 2 == 0) {
                String stoneStr = String.valueOf(stone);
                int midIndex = stoneStr.length() / 2;
                long leftStone = Long.parseLong(stoneStr.substring(0, midIndex));
                long rightStone = Long.parseLong(stoneStr.substring(midIndex));
                transformedStones.merge(leftStone, count, Long::sum);
                transformedStones.merge(rightStone, count, Long::sum);
            } else {
                transformedStones.merge(stone * 2024, count, Long::sum);
            }
        }

        return transformedStones;
    }
}
