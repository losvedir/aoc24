package day01part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // Create a frequency map for the right list
        Map<Integer, Integer> freqRight = new HashMap<>();
        for (Integer num : right) {
            freqRight.put(num, freqRight.getOrDefault(num, 0) + 1);
        }

        // Compute the similarity score
        long similarityScore = 0L;
        for (Integer num : left) {
            int countInRight = freqRight.getOrDefault(num, 0);
            similarityScore += (long) num * countInRight;
        }

        // Print the result
        System.out.println(similarityScore);
    }
}
