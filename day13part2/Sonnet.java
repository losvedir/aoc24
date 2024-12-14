package day13part2;

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

    private static final long OFFSET = 10_000_000_000_000L;

    private record ClawMachine(
            long buttonAX, long buttonAY,
            long buttonBX, long buttonBY,
            long prizeX, long prizeY) {
    }

    private record Solution(long pressesA, long pressesB) {
        public long totalTokens() {
            return pressesA * 3 + pressesB;
        }
    }

    // Extended Euclidean Algorithm result
    private record EEAResult(long gcd, long x, long y) {
    }

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("input/day13.txt"));
        System.out.println(solve(input));
    }

    public static long solve(String input) {
        List<ClawMachine> machines = parseInput(input);
        long totalTokens = 0;

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
                    Long.parseLong(matcher.group(5)) + OFFSET,
                    Long.parseLong(matcher.group(6)) + OFFSET));
        }

        return machines;
    }

    private static Optional<Solution> findMinimalSolution(ClawMachine m) {
        // Solve system of linear equations:
        // buttonAX * a + buttonBX * b = prizeX
        // buttonAY * a + buttonBY * b = prizeY

        // First equation: a * buttonAX + b * buttonBX = prizeX
        // Second equation: a * buttonAY + b * buttonBY = prizeY

        // Using elimination method to solve for one variable
        // Multiply first equation by buttonAY and second by buttonAX:
        // buttonAY * (a * buttonAX + b * buttonBX) = buttonAY * prizeX
        // buttonAX * (a * buttonAY + b * buttonBY) = buttonAX * prizeY

        // Subtracting these equations eliminates 'a':
        // b * (buttonBX * buttonAY - buttonBY * buttonAX) = buttonAY * prizeX -
        // buttonAX * prizeY

        long coefficient = m.buttonBX * m.buttonAY - m.buttonBY * m.buttonAX;
        if (coefficient == 0) {
            return Optional.empty(); // No solution exists
        }

        long rhs = m.buttonAY * m.prizeX - m.buttonAX * m.prizeY;

        // Find one solution using extended Euclidean algorithm
        EEAResult result = extendedEuclidean(coefficient, rhs);
        if (rhs % result.gcd != 0) {
            return Optional.empty(); // No integer solution exists
        }

        // Find the particular solution for b
        long b = (result.x * (rhs / result.gcd));

        // Once we have b, we can substitute back to find a
        long a = (m.prizeX - m.buttonBX * b) / m.buttonAX;

        // Check if solution is valid (positive integers)
        if (a < 0 || b < 0) {
            return Optional.empty();
        }

        // Verify solution
        if (m.buttonAX * a + m.buttonBX * b != m.prizeX ||
                m.buttonAY * a + m.buttonBY * b != m.prizeY) {
            return Optional.empty();
        }

        return Optional.of(new Solution(a, b));
    }

    private static EEAResult extendedEuclidean(long a, long b) {
        if (b == 0) {
            return new EEAResult(a, 1, 0);
        }

        EEAResult result = extendedEuclidean(b, a % b);
        return new EEAResult(
                result.gcd,
                result.y,
                result.x - (a / b) * result.y);
    }
}
