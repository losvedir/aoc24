package Day11;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Day11Part2o1Pro {

    // Memoization cache: Map from (stone_value_as_String, blinks_remaining) to
    // count of stones
    // We use String as key because BigInteger hashing might be slower and also to
    // ensure uniqueness.
    // Storing as String also helps avoid repeated toString() calls in subsequent
    // recursions.
    private static final Map<String, Map<Integer, Long>> memo = new HashMap<>();
    private static final BigInteger YEAR = BigInteger.valueOf(2024);
    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;

    public static void main(String[] args) throws IOException {
        // Read the input file. Assume there's just one line with the initial stones.
        String line = Files.readString(Paths.get("input/day11.txt")).trim();
        String[] initialStones = line.split("\\s+");

        int blinks = 75;
        long totalCount = 0;
        for (String stoneStr : initialStones) {
            BigInteger stoneVal = new BigInteger(stoneStr);
            totalCount += countStonesAfterBlinks(stoneVal, blinks);
        }

        System.out.println(totalCount);
    }

    private static long countStonesAfterBlinks(BigInteger stone, int blinks) {
        if (blinks == 0) {
            return 1; // No more blinks means this stone itself is just one stone.
        }

        // Convert to string once here
        String stoneStr = stone.toString();

        // Check memo
        Map<Integer, Long> subMap = memo.get(stoneStr);
        if (subMap != null && subMap.containsKey(blinks)) {
            return subMap.get(blinks);
        }

        long result;
        // Apply rules:
        if (stone.equals(ZERO)) {
            // Rule 1: If 0, becomes a stone with number 1
            result = countStonesAfterBlinks(ONE, blinks - 1);
        } else {
            int length = stoneStr.length();
            if (length % 2 == 0) {
                // Even length: split into two stones
                int mid = length / 2;
                // left half
                BigInteger leftHalf = new BigInteger(stoneStr.substring(0, mid));
                // right half
                BigInteger rightHalf = new BigInteger(stoneStr.substring(mid));

                long leftCount = countStonesAfterBlinks(leftHalf, blinks - 1);
                long rightCount = countStonesAfterBlinks(rightHalf, blinks - 1);
                result = leftCount + rightCount;
            } else {
                // Odd length: multiply by 2024
                BigInteger multiplied = stone.multiply(YEAR);
                result = countStonesAfterBlinks(multiplied, blinks - 1);
            }
        }

        // Store in memo
        if (subMap == null) {
            subMap = new HashMap<>();
            memo.put(stoneStr, subMap);
        }
        subMap.put(blinks, result);
        return result;
    }
}
