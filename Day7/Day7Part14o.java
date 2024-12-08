// // didn't work, an exception 
// // Exception in thread "main" java.lang.NumberFormatException: For input string: "276737138893"
// //	at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:67)

// package Day7;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.List;

// public class Day7Part14o {

//     public static void main(String[] args) {
//         String filePath = "input/day7.txt";
//         try {
//             List<String> lines = Files.readAllLines(Path.of(filePath));
//             int totalCalibrationResult = lines.stream()
//                     .mapToInt(Day7Part14o::processLine)
//                     .sum();
//             System.out.println("Total Calibration Result: " + totalCalibrationResult);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     /**
//      * Processes a single line of the input and returns the test value if the
//      * equation can be made valid.
//      * Otherwise, returns 0.
//      */
//     private static int processLine(String line) {
//         String[] parts = line.split(":");
//         int testValue = Integer.parseInt(parts[0].trim());
//         String[] numberStrings = parts[1].trim().split("\\s+");
//         int[] numbers = new int[numberStrings.length];

//         for (int i = 0; i < numberStrings.length; i++) {
//             numbers[i] = Integer.parseInt(numberStrings[i]);
//         }

//         // If any combination of operators makes the equation valid, return the test
//         // value; otherwise, 0
//         return canEvaluateToTarget(numbers, testValue, numbers[0], 1) ? testValue : 0;
//     }

//     /**
//      * Recursively checks if the numbers can be combined with operators (+ or *) to
//      * match the target.
//      */
//     private static boolean canEvaluateToTarget(int[] numbers, int target, int currentValue, int index) {
//         // Base case: if we've processed all numbers, check if the current value matches
//         // the target
//         if (index == numbers.length) {
//             return currentValue == target;
//         }

//         // Try adding the next number
//         if (canEvaluateToTarget(numbers, target, currentValue + numbers[index], index + 1)) {
//             return true;
//         }

//         // Try multiplying by the next number
//         if (canEvaluateToTarget(numbers, target, currentValue * numbers[index], index + 1)) {
//             return true;
//         }

//         // No valid combination found at this step
//         return false;
//     }
// }

// after pasting about it, it says the number was too big for int, let's change to long:

package Day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day7Part14o {

    public static void main(String[] args) {
        String filePath = "input/day7.txt";
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            long totalCalibrationResult = lines.stream()
                    .mapToLong(Day7Part14o::processLine)
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
     * Recursively checks if the numbers can be combined with operators (+ or *) to
     * match the target.
     */
    private static boolean canEvaluateToTarget(long[] numbers, long target, long currentValue, int index) {
        // Base case: if we've processed all numbers, check if the current value matches
        // the target
        if (index == numbers.length) {
            return currentValue == target;
        }

        // Try adding the next number
        if (canEvaluateToTarget(numbers, target, currentValue + numbers[index], index + 1)) {
            return true;
        }

        // Try multiplying by the next number
        if (canEvaluateToTarget(numbers, target, currentValue * numbers[index], index + 1)) {
            return true;
        }

        // No valid combination found at this step
        return false;
    }
}
