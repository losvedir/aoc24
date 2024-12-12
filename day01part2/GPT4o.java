package day01part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GPT4o {

    public static void main(String[] args) {
        try {
            // Read input from file
            String input = new String(Files.readAllBytes(Paths.get("input/day1.txt")));

            // Parse the input into two lists
            String[] lines = input.split("\\n");
            int[] leftList = new int[lines.length];
            int[] rightList = new int[lines.length];

            for (int i = 0; i < lines.length; i++) {
                String[] parts = lines[i].split("\\s+");
                leftList[i] = Integer.parseInt(parts[0]);
                rightList[i] = Integer.parseInt(parts[1]);
            }

            // Count occurrences in the right list
            Map<Integer, Integer> rightCountMap = new HashMap<>();
            for (int num : rightList) {
                rightCountMap.put(num, rightCountMap.getOrDefault(num, 0) + 1);
            }

            // Calculate similarity score
            int similarityScore = 0;
            for (int num : leftList) {
                similarityScore += num * rightCountMap.getOrDefault(num, 0);
            }

            // Output the result
            System.out.println("Similarity Score: " + similarityScore);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input: " + e.getMessage());
        }
    }
}
