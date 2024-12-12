package day01part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeminiFlash {

    public static int calculateSimilarityScore(List<Integer> leftList, List<Integer> rightList) {
        Map<Integer, Integer> rightListCounts = new HashMap<>();
        for (int num : rightList) {
            rightListCounts.put(num, rightListCounts.getOrDefault(num, 0) + 1);
        }

        int similarityScore = 0;
        for (int num : leftList) {
            similarityScore += num * rightListCounts.getOrDefault(num, 0);
        }
        return similarityScore;
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day1.txt"));

        List<Integer> leftList = lines.stream()
                .map(line -> line.split("\\s+"))
                .map(parts -> parts[0])
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> rightList = lines.stream()
                .map(line -> line.split("\\s+"))
                .map(parts -> parts.length > 1 ? parts[1] : "")
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int similarityScore = calculateSimilarityScore(leftList, rightList);
        System.out.println("Similarity score: " + similarityScore);
    }
}
