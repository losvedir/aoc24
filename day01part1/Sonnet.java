package day01part1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sonnet {
    public static void main(String[] args) {
        try {
            String result = solve("input/day1.txt");
            System.out.println("Total distance between lists: " + result);
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

        // Sort both lists independently
        Collections.sort(leftList);
        Collections.sort(rightList);

        // Calculate total distance between paired numbers
        long totalDistance = 0;
        for (int i = 0; i < leftList.size(); i++) {
            totalDistance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        return String.valueOf(totalDistance);
    }
}
