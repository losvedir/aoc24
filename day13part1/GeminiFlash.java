package day13part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiFlash {

    private static final Pattern BUTTON_PATTERN = Pattern.compile("Button ([AB]): X\\+([0-9]+), Y\\+([0-9]+)");
    private static final Pattern PRIZE_PATTERN = Pattern.compile("Prize: X=([0-9]+), Y=([0-9]+)");

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day13.txt"));

        int totalTokens = 0;
        int winnablePrizes = 0;

        for (int i = 0; i < lines.size(); i += 3) {
            Matcher buttonAMatcher = BUTTON_PATTERN.matcher(lines.get(i));
            Matcher buttonBMatcher = BUTTON_PATTERN.matcher(lines.get(i + 1));
            Matcher prizeMatcher = PRIZE_PATTERN.matcher(lines.get(i + 2));

            if (buttonAMatcher.matches() && buttonBMatcher.matches() && prizeMatcher.matches()) {
                int aX = Integer.parseInt(buttonAMatcher.group(2));
                int aY = Integer.parseInt(buttonAMatcher.group(3));
                int bX = Integer.parseInt(buttonBMatcher.group(2));
                int bY = Integer.parseInt(buttonBMatcher.group(3));
                int targetX = Integer.parseInt(prizeMatcher.group(1));
                int targetY = Integer.parseInt(prizeMatcher.group(2));

                int minTokens = findMinTokens(aX, aY, bX, bY, targetX, targetY);
                if (minTokens != -1) {
                    totalTokens += minTokens;
                    winnablePrizes++;
                }
            }
        }

        System.out.println("Winnable Prizes: " + winnablePrizes);
        System.out.println("Total Tokens: " + totalTokens);
    }

    private static int findMinTokens(int aX, int aY, int bX, int bY, int targetX, int targetY) {
        int minTokens = -1;
        for (int aCount = 0; aCount <= 100; aCount++) {
            for (int bCount = 0; bCount <= 100; bCount++) {
                if (aCount * aX + bCount * bX == targetX && aCount * aY + bCount * bY == targetY) {
                    int tokens = aCount * 3 + bCount;
                    if (minTokens == -1 || tokens < minTokens) {
                        minTokens = tokens;
                    }
                }
            }
        }
        return minTokens;
    }
}
