package day01part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
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

        // Count the frequency of each number in the right list
        Map<Integer, Long> rightFrequency = rightList.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        // Calculate the similarity score
        long similarityScore = 0L;
        for (int num : leftList) {
            long countInRight = rightFrequency.getOrDefault(num, 0L);
            similarityScore += num * countInRight;
        }

        // Print the result
        System.out.println(similarityScore);
    }
}
