package day13part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiPro {

    private static final int MAX_PRESSES = 100;

    record ClawMachine(int buttonAX, int buttonAY, int buttonBX, int buttonBY, int prizeX, int prizeY) {
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day13.txt"));
        List<ClawMachine> machines = parseInput(lines);

        int minTokens = Integer.MAX_VALUE;
        for (ClawMachine machine : machines) {
            minTokens = Math.min(minTokens, minTokensToWin(machine));
        }

        System.out.println(minTokens);
    }

    private static List<ClawMachine> parseInput(List<String> lines) {
        List<ClawMachine> machines = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "Button A: X\\+(\\d+), Y\\+(\\d+)\nButton B: X\\+(\\d+), Y\\+(\\d+)\nPrize: X=(\\d+), Y=(\\d+)");

        for (int i = 0; i < lines.size(); i += 4) {
            Matcher matcher = pattern.matcher(lines.get(i) + "\n" + lines.get(i + 1) + "\n" + lines.get(i + 2));
            if (matcher.find()) {
                machines.add(new ClawMachine(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)),
                        Integer.parseInt(matcher.group(5)),
                        Integer.parseInt(matcher.group(6))));
            }
        }
        return machines;
    }

    private static int minTokensToWin(ClawMachine machine) {
        int minTokens = Integer.MAX_VALUE;
        for (int a = 0; a <= MAX_PRESSES; a++) {
            for (int b = 0; b <= MAX_PRESSES; b++) {
                if (a * machine.buttonAX + b * machine.buttonBX == machine.prizeX &&
                        a * machine.buttonAY + b * machine.buttonBY == machine.prizeY) {
                    minTokens = Math.min(minTokens, a * 3 + b);
                }
            }
        }
        return minTokens;
    }
}
