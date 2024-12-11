package Day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day11Part1Opus {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day11.txt"));
        List<Long> stones = Arrays.stream(lines.get(0).split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        int blinks = 25;
        for (int i = 0; i < blinks; i++) {
            stones = transformStones(stones);
        }

        System.out.println("Number of stones after " + blinks + " blinks: " + stones.size());
    }

    private static List<Long> transformStones(List<Long> stones) {
        List<Long> transformedStones = new ArrayList<>();

        for (long stone : stones) {
            if (stone == 0) {
                transformedStones.add(1L);
            } else if (String.valueOf(stone).length() % 2 == 0) {
                String stoneStr = String.valueOf(stone);
                int midIndex = stoneStr.length() / 2;
                transformedStones.add(Long.parseLong(stoneStr.substring(0, midIndex)));
                transformedStones.add(Long.parseLong(stoneStr.substring(midIndex)));
            } else {
                transformedStones.add(stone * 2024);
            }
        }

        return transformedStones;
    }
}
