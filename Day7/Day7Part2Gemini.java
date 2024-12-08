// didn't work. answer was too low, it tried to fix it (had an idea where the logic was wrong)
// but still failed.

package Day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day7Part2Gemini {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day7.txt"));
        long totalCalibrationResult = 0;

        for (String line : lines) {
            String[] parts = line.split(":");
            long testValue = Long.parseLong(parts[0].trim());
            String[] numbers = parts[1].trim().split(" ");

            if (canBeTrue(testValue, numbers)) {
                totalCalibrationResult += testValue;
            }
        }

        System.out.println("Total calibration result: " + totalCalibrationResult);
    }

    private static boolean canBeTrue(long testValue, String[] numbers) {
        return canBeTrueRecursive(testValue, numbers, 0, 0);
    }

    // private static boolean canBeTrueRecursive(long testValue, String[] numbers,
    // int index, long currentValue) {
    // if (index == numbers.length) {
    // return testValue == currentValue;
    // }

    // long number = Long.parseLong(numbers[index]);

    // // Try adding the next number
    // if (canBeTrueRecursive(testValue, numbers, index + 1, currentValue + number))
    // {
    // return true;
    // }

    // // Try multiplying the next number
    // if (canBeTrueRecursive(testValue, numbers, index + 1, currentValue * number))
    // {
    // return true;
    // }

    // // Try concatenating the next number
    // if (index < numbers.length - 1) {
    // long nextNumber = Long.parseLong(numbers[index + 1]);
    // String concatenated = number + "" + nextNumber;
    // long concatenatedValue = Long.parseLong(concatenated);
    // if (canBeTrueRecursive(testValue, numbers, index + 2, currentValue *
    // concatenatedValue)) {
    // return true;
    // }
    // if (canBeTrueRecursive(testValue, numbers, index + 2, currentValue +
    // concatenatedValue)) {
    // return true;
    // }
    // }

    // return false;
    // }

    private static boolean canBeTrueRecursive(long testValue, String[] numbers, int index, long currentValue) {
        if (index == numbers.length) {
            return testValue == currentValue;
        }

        long number = Long.parseLong(numbers[index]);

        // Try adding the next number
        if (canBeTrueRecursive(testValue, numbers, index + 1, currentValue + number)) {
            return true;
        }

        // Try multiplying the next number
        if (canBeTrueRecursive(testValue, numbers, index + 1, currentValue * number)) {
            return true;
        }

        // Try concatenating the next number
        if (index < numbers.length - 1) {
            long nextNumber = Long.parseLong(numbers[index + 1]);
            String concatenated = number + "" + nextNumber;
            long concatenatedValue = Long.parseLong(concatenated);
            // The issue was here: I was multiplying/adding currentValue with
            // concatenatedValue,
            // but it should be applied to the result of the next recursive call.
            if (canBeTrueRecursive(testValue, numbers, index + 2, concatenatedValue)) {
                return true;
            }
        }

        return false;
    }
}
