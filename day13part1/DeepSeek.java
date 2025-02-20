package day13part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepSeek {

    private static class Machine {
        final int ax, ay;
        final int bx, by;
        final int px, py;

        public Machine(int ax, int ay, int bx, int by, int px, int py) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.px = px;
            this.py = py;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = readLines();
        List<Machine> machines = parseMachines(lines);
        int totalCost = 0;

        for (Machine m : machines) {
            int minCost = findMinCost(m);
            if (minCost != -1) {
                totalCost += minCost;
            }
        }

        System.out.println(totalCost);
    }

    private static List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input/day13.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }

    private static List<Machine> parseMachines(List<String> lines) {
        List<Machine> machines = new ArrayList<>();
        int i = 0;
        while (i < lines.size()) {
            while (i < lines.size() && lines.get(i).isEmpty()) {
                i++;
            }
            if (i >= lines.size()) {
                break;
            }
            String lineA = lines.get(i++);
            String lineB = lines.get(i++);
            String linePrize = lines.get(i++);
            machines.add(parseMachine(lineA, lineB, linePrize));
        }
        return machines;
    }

    private static Machine parseMachine(String lineA, String lineB, String linePrize) {
        Pattern aPattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
        Matcher aMatcher = aPattern.matcher(lineA);
        if (!aMatcher.matches()) {
            throw new IllegalArgumentException("Invalid lineA: " + lineA);
        }
        int ax = Integer.parseInt(aMatcher.group(1));
        int ay = Integer.parseInt(aMatcher.group(2));

        Pattern bPattern = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
        Matcher bMatcher = bPattern.matcher(lineB);
        if (!bMatcher.matches()) {
            throw new IllegalArgumentException("Invalid lineB: " + lineB);
        }
        int bx = Integer.parseInt(bMatcher.group(1));
        int by = Integer.parseInt(bMatcher.group(2));

        Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");
        Matcher prizeMatcher = prizePattern.matcher(linePrize);
        if (!prizeMatcher.matches()) {
            throw new IllegalArgumentException("Invalid linePrize: " + linePrize);
        }
        int px = Integer.parseInt(prizeMatcher.group(1));
        int py = Integer.parseInt(prizeMatcher.group(2));

        return new Machine(ax, ay, bx, by, px, py);
    }

    private static int findMinCost(Machine m) {
        int minCost = Integer.MAX_VALUE;
        boolean found = false;

        for (int a = 0; a <= 100; a++) {
            for (int b = 0; b <= 100; b++) {
                if (a * m.ax + b * m.bx == m.px && a * m.ay + b * m.by == m.py) {
                    found = true;
                    int cost = 3 * a + b;
                    if (cost < minCost) {
                        minCost = cost;
                    }
                }
            }
        }

        return found ? minCost : -1;
    }
}
