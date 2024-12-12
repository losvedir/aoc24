package day01part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Opus {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day1.txt"));
            List<Integer> leftList = extractNumbers(lines, 0);
            List<Integer> rightList = extractNumbers(lines, 1);

            int similarityScore = calculateSimilarityScore(leftList, rightList);
            System.out.println("Similarity score: " + similarityScore);
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }

    private static List<Integer> extractNumbers(List<String> lines, int index) {
        return lines.stream()
                .map(line -> line.split("\\s+")[index])
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static int calculateSimilarityScore(List<Integer> leftList, List<Integer> rightList) {
        return leftList.stream()
                .mapToInt(num -> num * countOccurrences(rightList, num))
                .sum();
    }

    private static int countOccurrences(List<Integer> list, int num) {
        return (int) list.stream()
                .filter(n -> n == num)
                .count();
    }
}
