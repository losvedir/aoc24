package Day7;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day7Part2Opus {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of("input/day7.txt"));

        long total = 0;
        for (String line : lines) {
            String[] parts = line.split(":");
            long target = Long.parseLong(parts[0]);
            String[] numbers = parts[1].trim().split(" ");

            if (canReachTarget(numbers, target, 0, 0, '+')) {
                total += target;
            }
        }

        System.out.println("Total calibration result: " + total);
    }

    private static boolean canReachTarget(String[] numbers, long target, int index, long current, char prevOp) {
        if (index == numbers.length) {
            return current == target;
        }

        long num = Long.parseLong(numbers[index]);

        if (prevOp == '+') {
            current += num;
        } else if (prevOp == '*') {
            current *= num;
        } else if (prevOp == '|') {
            current = Long.parseLong(current + "" + num);
        }

        if (index == numbers.length - 1) {
            return current == target;
        }

        return canReachTarget(numbers, target, index + 1, current, '+') ||
                canReachTarget(numbers, target, index + 1, current, '*') ||
                canReachTarget(numbers, target, index + 1, current, '|');
    }
}
