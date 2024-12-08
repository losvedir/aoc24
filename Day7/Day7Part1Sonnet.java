// correct, first try. no int -> long issue

package Day7;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day7Part1Sonnet {
    record Equation(long targetValue, List<Integer> numbers) {
    }

    public static void main(String[] args) {
        try {
            var result = new Day7Part1Sonnet().solve();
            System.out.println("Total calibration result: " + result);
        } catch (Exception e) {
            System.err.println("Error solving puzzle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public long solve() throws Exception {
        var equations = parseInput();
        return equations.stream()
                .filter(this::canMakeTarget)
                .mapToLong(Equation::targetValue)
                .sum();
    }

    private List<Equation> parseInput() throws Exception {
        var equations = new ArrayList<Equation>();
        var lines = Files.readAllLines(Path.of("input/day7.txt"));

        for (var line : lines) {
            var parts = line.split(": ");
            var targetValue = Long.parseLong(parts[0]);

            var numbers = new ArrayList<Integer>();
            for (var num : parts[1].split(" ")) {
                numbers.add(Integer.parseInt(num));
            }

            equations.add(new Equation(targetValue, numbers));
        }

        return equations;
    }

    private boolean canMakeTarget(Equation eq) {
        return tryAllOperatorCombinations(eq.numbers(), 0, eq.numbers().get(0), eq.targetValue());
    }

    private boolean tryAllOperatorCombinations(List<Integer> numbers, int index, long currentResult, long target) {
        if (index == numbers.size() - 1) {
            return currentResult == target;
        }

        var nextNum = numbers.get(index + 1);

        // Try addition
        if (tryAllOperatorCombinations(numbers, index + 1, currentResult + nextNum, target)) {
            return true;
        }

        // Try multiplication
        return tryAllOperatorCombinations(numbers, index + 1, currentResult * nextNum, target);
    }
}
