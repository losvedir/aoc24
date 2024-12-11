package Day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Day11Part1o1Pro {

    public static void main(String[] args) throws IOException {
        // Read the input file. Assume there's just one line with the initial stones.
        List<String> stones = new ArrayList<>(List.of(
                Files.readString(Paths.get("input/day11.txt"))
                        .trim()
                        .split("\\s+")));

        // We need to blink 25 times
        int blinks = 25;

        for (int i = 0; i < blinks; i++) {
            stones = blink(stones);
        }

        System.out.println(stones.size());
    }

    private static List<String> blink(List<String> stones) {
        List<String> newStones = new ArrayList<>();

        for (String stone : stones) {
            if (stone.equals("0")) {
                // Rule 1: If 0, becomes 1
                newStones.add("1");
            } else {
                // Check length for even number of digits
                int length = stone.length();
                if (length % 2 == 0) {
                    // Rule 2: Split into two stones
                    int mid = length / 2;
                    String leftHalf = stone.substring(0, mid);
                    String rightHalf = stone.substring(mid);

                    // Remove leading zeros by parsing as int and converting back to string
                    leftHalf = new BigInteger(leftHalf).toString();
                    rightHalf = new BigInteger(rightHalf).toString();

                    newStones.add(leftHalf);
                    newStones.add(rightHalf);
                } else {
                    // Rule 3: Multiply by 2024
                    BigInteger val = new BigInteger(stone);
                    val = val.multiply(BigInteger.valueOf(2024));
                    newStones.add(val.toString());
                }
            }
        }

        return newStones;
    }
}
