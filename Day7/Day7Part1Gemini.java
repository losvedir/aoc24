// first version didn't work, got an exception

// package Day7;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.List;

// public class Day7Part1Gemini {

//     public static void main(String[] args) throws IOException {
//         List<String> lines = Files.readAllLines(Paths.get("input/day7.txt"));
//         int totalCalibrationResult = 0;

//         for (String line : lines) {
//             String[] parts = line.split(":");
//             int testValue = Integer.parseInt(parts[0].trim());
//             String[] numbers = parts[1].trim().split(" ");

//             if (canBeTrue(testValue, numbers)) {
//                 totalCalibrationResult += testValue;
//             }
//         }

//         System.out.println("Total calibration result: " + totalCalibrationResult);
//     }

//     private static boolean canBeTrue(int testValue, String[] numbers) {
//         return canBeTrueRecursive(testValue, numbers, 0, 0);
//     }

//     private static boolean canBeTrueRecursive(int testValue, String[] numbers, int index, int currentValue) {
//         if (index == numbers.length) {
//             return testValue == currentValue;
//         }

//         int number = Integer.parseInt(numbers[index]);

//         // Try adding the next number
//         if (canBeTrueRecursive(testValue, numbers, index + 1, currentValue + number)) {
//             return true;
//         }

//         // Try multiplying the next number
//         return canBeTrueRecursive(testValue, numbers, index + 1, currentValue * number);
//     }
// }

// Pasted the exception and it identified that the numbers were too large, int -> long

package Day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day7Part1Gemini {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day7.txt"));
        long totalCalibrationResult = 0; // Changed to long

        for (String line : lines) {
            String[] parts = line.split(":");
            long testValue = Long.parseLong(parts[0].trim()); // Changed to long
            String[] numbers = parts[1].trim().split(" ");

            if (canBeTrue(testValue, numbers)) {
                totalCalibrationResult += testValue;
            }
        }

        System.out.println("Total calibration result: " + totalCalibrationResult);
    }

    private static boolean canBeTrue(long testValue, String[] numbers) { // Changed to long
        return canBeTrueRecursive(testValue, numbers, 0, 0);
    }

    private static boolean canBeTrueRecursive(long testValue, String[] numbers, int index, long currentValue) { // Changed
                                                                                                                // to
                                                                                                                // long
        if (index == numbers.length) {
            return testValue == currentValue;
        }

        long number = Long.parseLong(numbers[index]); // Changed to long

        // Try adding the next number
        if (canBeTrueRecursive(testValue, numbers, index + 1, currentValue + number)) {
            return true;
        }

        // Try multiplying the next number
        return canBeTrueRecursive(testValue, numbers, index + 1, currentValue * number);
    }
}
