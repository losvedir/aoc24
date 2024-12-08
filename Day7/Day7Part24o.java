package Day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day7Part24o {

    public static void main(String[] args) {
        String filePath = "input/day7.txt";
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            long totalCalibrationResult = lines.stream()
                    .mapToLong(Day7Part24o::processLine)
                    .sum();
            System.out.println("Total Calibration Result: " + totalCalibrationResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes a single line of the input and returns the test value if the
     * equation can be made valid.
     * Otherwise, returns 0.
     */
    private static long processLine(String line) {
        String[] parts = line.split(":");
        long testValue = Long.parseLong(parts[0].trim());
        String[] numberStrings = parts[1].trim().split("\\s+");
        long[] numbers = new long[numberStrings.length];

        for (int i = 0; i < numberStrings.length; i++) {
            numbers[i] = Long.parseLong(numberStrings[i]);
        }

        // If any combination of operators makes the equation valid, return the test
        // value; otherwise, 0
        return canEvaluateToTarget(numbers, testValue, numbers[0], 1) ? testValue : 0;
    }

    /**
     * Recursively checks if the numbers can be combined with operators (+, *, ||)
     * to match the target.
     */
    private static boolean canEvaluateToTarget(long[] numbers, long target, long currentValue, int index) {
        // Base case: if we've processed all numbers, check if the current value matches
        // the target
        if (index == numbers.length) {
            return currentValue == target;
        }

        long nextNumber = numbers[index];

        // Try adding the next number
        if (canEvaluateToTarget(numbers, target, currentValue + nextNumber, index + 1)) {
            return true;
        }

        // Try multiplying by the next number
        if (canEvaluateToTarget(numbers, target, currentValue * nextNumber, index + 1)) {
            return true;
        }

        // Try concatenating the next number
        long concatenatedValue = concatenateNumbers(currentValue, nextNumber);
        if (canEvaluateToTarget(numbers, target, concatenatedValue, index + 1)) {
            return true;
        }

        // No valid combination found at this step
        return false;
    }

    /**
     * Concatenates two numbers by treating them as strings and combining their
     * digits.
     * For example, concatenateNumbers(12, 345) returns 12345.
     */
    private static long concatenateNumbers(long a, long b) {
        return Long.parseLong(a + "" + b);
    }
}
