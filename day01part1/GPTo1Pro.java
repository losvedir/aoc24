package day01part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class GPTo1Pro {
    public static void main(String[] args) throws IOException {
        // Read all lines from the input file
        List<String> lines = Files.readAllLines(Path.of("input/day1.txt"));

        // Parse the lines into two lists of integers (left and right)
        List<Integer> left = lines.stream()
                .map(line -> line.trim().split("\\s+"))
                .map(parts -> Integer.parseInt(parts[0]))
                .collect(Collectors.toList());

        List<Integer> right = lines.stream()
                .map(line -> line.trim().split("\\s+"))
                .map(parts -> Integer.parseInt(parts[1]))
                .collect(Collectors.toList());

        // Sort both lists
        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        // Compute the total distance
        long totalDistance = 0L;
        for (int i = 0; i < left.size(); i++) {
            totalDistance += Math.abs(left.get(i) - right.get(i));
        }

        // Print the result
        System.out.println(totalDistance);
    }
}
