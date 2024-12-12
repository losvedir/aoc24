package day01part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Opus {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day1.txt"));
            List<Integer> leftList = extractNumbers(lines, 0);
            List<Integer> rightList = extractNumbers(lines, 1);

            int totalDistance = calculateTotalDistance(leftList, rightList);
            System.out.println("Total distance between the lists: " + totalDistance);
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }

    private static List<Integer> extractNumbers(List<String> lines, int index) {
        return lines.stream()
                .map(line -> line.split("\\s+")[index])
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static int calculateTotalDistance(List<Integer> leftList, List<Integer> rightList) {
        List<Integer> sortedLeftList = new ArrayList<>(leftList);
        List<Integer> sortedRightList = new ArrayList<>(rightList);

        Collections.sort(sortedLeftList);
        Collections.sort(sortedRightList);

        return IntStream.range(0, sortedLeftList.size())
                .map(i -> Math.abs(sortedLeftList.get(i) - sortedRightList.get(i)))
                .sum();
    }
}
