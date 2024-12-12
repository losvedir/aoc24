package day01part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sonnet {
    public static void main(String[] args) {
        try {
            String result = solve("input/day1.txt");
            System.out.println("Total similarity score: " + result);
        } catch (Exception e) {
            System.err.println("Error processing input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String solve(String inputPath) throws Exception {
        // Read all lines from input file
        List<String> lines = Files.readAllLines(Path.of(inputPath));

        // Parse input into two separate lists
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            leftList.add(Integer.parseInt(parts[0]));
            rightList.add(Integer.parseInt(parts[1]));
        }

        // Count occurrences of numbers in the right list
        Map<Integer, Integer> rightListCounts = new HashMap<>();
        for (int num : rightList) {
            rightListCounts.merge(num, 1, Integer::sum);
        }

        // Calculate similarity score
        long similarityScore = 0;
        for (int leftNum : leftList) {
            // Get count from right list (defaulting to 0 if number doesn't exist)
            int count = rightListCounts.getOrDefault(leftNum, 0);
            similarityScore += (long) leftNum * count;
        }

        return String.valueOf(similarityScore);
    }
}
