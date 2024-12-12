package day01part1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class GPTo1 {

    public static void main(String[] args) throws Exception {
        // Read all lines from the input file
        List<String> lines = Files.readAllLines(Path.of("input/day1.txt"));

        // Parse the lines into two separate lists of integers
        List<Integer> leftList = lines.stream()
                .filter(line -> !line.isBlank())
                .map(line -> line.trim().split("\\s+"))
                .map(parts -> Integer.parseInt(parts[0]))
                .collect(Collectors.toList());

        List<Integer> rightList = lines.stream()
                .filter(line -> !line.isBlank())
                .map(line -> line.trim().split("\\s+"))
                .map(parts -> Integer.parseInt(parts[1]))
                .collect(Collectors.toList());

        // Sort both lists
        leftList.sort(Integer::compareTo);
        rightList.sort(Integer::compareTo);

        // Calculate the sum of absolute differences
        long totalDistance = 0L;
        for (int i = 0; i < leftList.size(); i++) {
            totalDistance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        // Print the result
        System.out.println(totalDistance);
    }
}
