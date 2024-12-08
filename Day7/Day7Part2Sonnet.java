package Day7;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day7Part2Sonnet {
    record Equation(long targetValue, List<Integer> numbers) {
    }

    private enum Operator {
        ADD, MULTIPLY, CONCATENATE
    }

    public static void main(String[] args) {
        try {
            var result = new Day7Part2Sonnet().solve();
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
        if (tryAllOperatorCombinations(numbers, index + 1, currentResult * nextNum, target)) {
            return true;
        }

        // Try concatenation
        // Convert current result and next number to strings and concatenate
        var concatenated = Long.parseLong(String.valueOf(currentResult) + nextNum);
        // Check if concatenated result is within valid long range
        if (concatenated > 0) { // Ensure we haven't overflowed
            return tryAllOperatorCombinations(numbers, index + 1, concatenated, target);
        }

        return false;
    }

    // Helper method to apply an operator to two numbers
    private long applyOperator(long a, long b, Operator op) {
        return switch (op) {
            case ADD -> a + b;
            case MULTIPLY -> a * b;
            case CONCATENATE -> Long.parseLong(String.valueOf(a) + b);
        };
    }
}
