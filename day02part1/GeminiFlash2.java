package day02part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeminiFlash2 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day2.txt"));
        long safeReports = lines.stream()
                .map(GeminiFlash2::parseLevels)
                .filter(GeminiFlash2::isSafe)
                .count();

        System.out.println("Number of safe reports: " + safeReports);
    }

    private static List<Integer> parseLevels(String line) {
        return Arrays.stream(line.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static boolean isSafe(List<Integer> levels) {
        if (levels.size() < 2) {
            return true; // A single level is always safe
        }

        boolean increasing = levels.get(1) > levels.get(0);
        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false;
            }
            if ((diff > 0 && !increasing) || (diff < 0 && increasing)) {
                return false;
            }
        }
        return true;
    }
}
