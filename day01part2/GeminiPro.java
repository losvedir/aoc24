package day01part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeminiPro {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day1.txt"));
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split("\\s+");
            leftList.add(Integer.parseInt(parts[0]));
            rightList.add(Integer.parseInt(parts[1]));
        }

        int similarityScore = 0;
        for (Integer left : leftList) {
            similarityScore += left * Collections.frequency(rightList, left);
        }

        System.out.println("Similarity score: " + similarityScore);
    }
}
