package day01part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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

            // Sort both lists
            Arrays.sort(leftList);
            Arrays.sort(rightList);

            // Calculate total distance
            int totalDistance = 0;
            for (int i = 0; i < leftList.length; i++) {
                totalDistance += Math.abs(leftList[i] - rightList[i]);
            }

            // Output the result
            System.out.println("Total Distance: " + totalDistance);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input: " + e.getMessage());
        }
    }
}
