package Day11;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day11Part1o1 {
    public static void main(String[] args) throws IOException {
        // Read all lines from input file (assuming one line with space-separated
        // integers)
        List<String> lines = Files.readAllLines(Paths.get("input/day11.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        // Parse the initial stones
        String[] initialNumbers = lines.get(0).trim().split("\\s+");
        List<BigInteger> stones = new ArrayList<>(initialNumbers.length);
        for (String numStr : initialNumbers) {
            stones.add(new BigInteger(numStr));
        }

        // We need to blink 25 times
        for (int blink = 0; blink < 25; blink++) {
            stones = blink(stones);
        }

        // After 25 blinks, print the number of stones
        System.out.println(stones.size());
    }

    private static List<BigInteger> blink(List<BigInteger> stones) {
        // Each stone transforms simultaneously according to the rules
        // Rules:
        // 1) If number == 0, becomes [1].
        // 2) Else if even number of digits, split into two stones (left half and right
        // half)
        // (no leading zeros to remain, so parse halves as integers again)
        // 3) Else multiply by 2024
        List<BigInteger> newStones = new ArrayList<>(stones.size() * 2); // can grow
        for (BigInteger stone : stones) {
            if (stone.equals(BigInteger.ZERO)) {
                // rule 1
                newStones.add(BigInteger.ONE);
            } else {
                String stoneStr = stone.toString();
                int length = stoneStr.length();
                if (length % 2 == 0) {
                    // rule 2: split evenly
                    int mid = length / 2;
                    String leftPart = stoneStr.substring(0, mid);
                    String rightPart = stoneStr.substring(mid);

                    // Convert to BigIntegers to strip any leading zeros
                    BigInteger leftVal = new BigInteger(leftPart);
                    BigInteger rightVal = new BigInteger(rightPart);
                    newStones.add(leftVal);
                    newStones.add(rightVal);
                } else {
                    // rule 3: multiply by 2024
                    newStones.add(stone.multiply(BigInteger.valueOf(2024)));
                }
            }
        }
        return newStones;
    }
}
