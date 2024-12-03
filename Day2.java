import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day2 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("input/day2.txt"));

        // part1(input);
        part2(input);
    }

    static void part1(String input) {
        long safeReports = input.lines()
                .map(line -> splitLine(line))
                .filter(Day2::isSafe)
                .count();

        System.out.println("part1: " + safeReports);
    }

    static void part2(String input) {
        long safeReports = input.lines()
                .map(line -> splitLine(line))
                .filter(Day2::isSafeWithDamping)
                .count();

        System.out.println("part2: " + safeReports);
    }

    static int[] splitLine(String l) {
        return Pattern.compile(" ")
                .splitAsStream(l)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    static boolean isSafe(int[] levels) {
        return isSafelyIncreasing(levels) || isSafelyDecreasing(levels);
    }

    static boolean isSafeWithDamping(int[] levels) {
        return IntStream.range(0, levels.length)
                .mapToObj(i -> IntStream.range(0, levels.length)
                        .filter(j -> j != i)
                        .map(j -> levels[j])
                        .toArray())
                .anyMatch(Day2::isSafe);
    }

    static boolean isSafelyIncreasing(int[] levels) {
        for (int i = 1; i < levels.length; i++) {
            var diff = levels[i] - levels[i - 1];
            if (!(diff >= 1 && diff <= 3)) {
                return false;
            }
        }

        return true;
    }

    static boolean isSafelyDecreasing(int[] levels) {
        for (int i = 1; i < levels.length; i++) {
            var diff = levels[i] - levels[i - 1];
            if (!(diff >= -3 && diff <= -1)) {
                return false;
            }
        }

        return true;
    }
}
