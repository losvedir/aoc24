package Day11;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11Part2o1 {
    // Memoization cache: key = stone_value + ":" + steps_remaining, value = count
    // of stones after that many steps
    private static final Map<String, BigInteger> memo = new HashMap<>();
    private static final BigInteger MULTIPLIER = BigInteger.valueOf(2024);

    public static void main(String[] args) throws IOException {
        // Read input
        List<String> lines = Files.readAllLines(Paths.get("input/day11.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        String[] initialNumbers = lines.get(0).trim().split("\\s+");
        int blinks = 75; // Part 2 requires 75 blinks

        BigInteger totalStones = BigInteger.ZERO;
        for (String num : initialNumbers) {
            totalStones = totalStones.add(countStonesAfterBlinks(num, blinks));
        }

        System.out.println(totalStones);
    }

    private static BigInteger countStonesAfterBlinks(String stoneStr, int steps) {
        if (steps == 0) {
            // No more transformations, just one stone
            return BigInteger.ONE;
        }

        String key = stoneStr + ":" + steps;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        BigInteger result;

        // Apply rules
        BigInteger stoneVal = new BigInteger(stoneStr);
        if (stoneVal.equals(BigInteger.ZERO)) {
            // Rule 1: 0 -> 1
            result = countStonesAfterBlinks("1", steps - 1);
        } else {
            // Check number of digits
            int length = stoneStr.length();
            if (length % 2 == 0) {
                // Even number of digits: split into two
                int mid = length / 2;
                String leftPart = stoneStr.substring(0, mid);
                String rightPart = stoneStr.substring(mid);

                // Convert to BigInteger and then to string again to strip leading zeros
                // (This ensures no leading zeros remain.)
                BigInteger leftVal = new BigInteger(leftPart);
                BigInteger rightVal = new BigInteger(rightPart);

                BigInteger leftCount = countStonesAfterBlinks(leftVal.toString(), steps - 1);
                BigInteger rightCount = countStonesAfterBlinks(rightVal.toString(), steps - 1);
                result = leftCount.add(rightCount);
            } else {
                // Odd number of digits: multiply by 2024
                BigInteger newVal = stoneVal.multiply(MULTIPLIER);
                result = countStonesAfterBlinks(newVal.toString(), steps - 1);
            }
        }

        memo.put(key, result);
        return result;
    }
}
