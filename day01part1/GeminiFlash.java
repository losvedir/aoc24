package day01part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeminiFlash {

    public static int calculateTotalDistance(List<Integer> leftList, List<Integer> rightList) {
        int totalDistance = 0;
        for (int i = 0; i < Math.min(leftList.size(), rightList.size()); i++) {
            totalDistance += Math.abs(leftList.get(i) - rightList.get(i));
        }
        return totalDistance;
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day1.txt"));

        List<Integer> leftList = lines.stream()
                .map(line -> line.split("\\s+"))
                .map(parts -> parts[0])
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        List<Integer> rightList = lines.stream()
                .map(line -> line.split("\\s+"))
                .map(parts -> parts.length > 1 ? parts[1] : "")
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        int totalDistance = calculateTotalDistance(leftList, rightList);
        System.out.println("Total distance: " + totalDistance);
    }
}
