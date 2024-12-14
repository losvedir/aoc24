package day13part1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sonnet {
    private static final Pattern PATTERN = Pattern.compile(
            "Button A: X\\+(\\d+), Y\\+(\\d+)\\s*" +
                    "Button B: X\\+(\\d+), Y\\+(\\d+)\\s*" +
                    "Prize: X=(\\d+), Y=(\\d+)");

    private record ClawMachine(
            int buttonAX, int buttonAY,
            int buttonBX, int buttonBY,
            int prizeX, int prizeY) {
    }

    private record Solution(int pressesA, int pressesB) {
        public int totalTokens() {
            return pressesA * 3 + pressesB;
        }
    }

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("input/day13.txt"));
        System.out.println(solve(input));
    }

    public static int solve(String input) {
        List<ClawMachine> machines = parseInput(input);
        int totalTokens = 0;

        for (ClawMachine machine : machines) {
            Optional<Solution> solution = findMinimalSolution(machine);
            if (solution.isPresent()) {
                totalTokens += solution.get().totalTokens();
            }
        }

        return totalTokens;
    }

    private static List<ClawMachine> parseInput(String input) {
        List<ClawMachine> machines = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(input);

        while (matcher.find()) {
            machines.add(new ClawMachine(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(5)),
                    Integer.parseInt(matcher.group(6))));
        }

        return machines;
    }

    private static Optional<Solution> findMinimalSolution(ClawMachine machine) {
        // Try all combinations of button presses up to 100 times each
        int minTokens = Integer.MAX_VALUE;
        Solution bestSolution = null;

        for (int a = 0; a <= 100; a++) {
            for (int b = 0; b <= 100; b++) {
                if (isValidSolution(machine, a, b)) {
                    int tokens = a * 3 + b;
                    if (tokens < minTokens) {
                        minTokens = tokens;
                        bestSolution = new Solution(a, b);
                    }
                }
            }
        }

        return Optional.ofNullable(bestSolution);
    }

    private static boolean isValidSolution(ClawMachine machine, int pressesA, int pressesB) {
        long totalX = (long) pressesA * machine.buttonAX + (long) pressesB * machine.buttonBX;
        long totalY = (long) pressesA * machine.buttonAY + (long) pressesB * machine.buttonBY;

        return totalX == machine.prizeX && totalY == machine.prizeY;
    }
}
