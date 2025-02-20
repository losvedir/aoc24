package day13part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepSeek {

    private static class Machine {
        final long ax, ay;
        final long bx, by;
        final long px, py;

        public Machine(long ax, long ay, long bx, long by, long px, long py) {
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
        long totalCost = 0;

        for (Machine m : machines) {
            long minCost = findMinCost(m);
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
        long ax = Long.parseLong(aMatcher.group(1));
        long ay = Long.parseLong(aMatcher.group(2));

        Pattern bPattern = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
        Matcher bMatcher = bPattern.matcher(lineB);
        if (!bMatcher.matches()) {
            throw new IllegalArgumentException("Invalid lineB: " + lineB);
        }
        long bx = Long.parseLong(bMatcher.group(1));
        long by = Long.parseLong(bMatcher.group(2));

        Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");
        Matcher prizeMatcher = prizePattern.matcher(linePrize);
        if (!prizeMatcher.matches()) {
            throw new IllegalArgumentException("Invalid linePrize: " + linePrize);
        }
        long px = Long.parseLong(prizeMatcher.group(1)) + 10_000_000_000_000L;
        long py = Long.parseLong(prizeMatcher.group(2)) + 10_000_000_000_000L;

        return new Machine(ax, ay, bx, by, px, py);
    }

    private static class Triplet {
        long x, y, gcd;

        public Triplet(long x, long y, long gcd) {
            this.x = x;
            this.y = y;
            this.gcd = gcd;
        }
    }

    private static Triplet extendedGCD(long a, long b) {
        if (b == 0) {
            return new Triplet(1, 0, a);
        } else {
            Triplet t = extendedGCD(b, a % b);
            return new Triplet(t.y, t.x - (a / b) * t.y, t.gcd);
        }
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long findMinCost(Machine m) {
        long ax = m.ax;
        long ay = m.ay;
        long bx = m.bx;
        long by = m.by;
        long px = m.px;
        long py = m.py;

        long D = ax * by - ay * bx;

        if (D != 0) {
            long numeratorA = px * by - py * bx;
            long numeratorB = ax * py - ay * px;

            if (numeratorA % D != 0 || numeratorB % D != 0) {
                return -1;
            }

            long a = numeratorA / D;
            long bVal = numeratorB / D;

            if (a >= 0 && bVal >= 0) {
                return 3 * a + bVal;
            } else {
                return -1;
            }
        } else {
            if (px * ay != py * ax || px * by != py * bx) {
                return -1;
            }

            long d = gcd(ax, bx);

            if (px % d != 0) {
                return -1;
            }

            long axPrime = ax / d;
            long bxPrime = bx / d;
            long pxPrime = px / d;

            Triplet t = extendedGCD(axPrime, bxPrime);
            long x = t.x;
            long y = t.y;

            long a0 = x * pxPrime;
            long b0 = y * pxPrime;

            long t_min = (long) Math.ceil((-a0) / (double) bxPrime);
            long t_max = (long) Math.floor(b0 / (double) axPrime);

            if (t_min > t_max) {
                return -1;
            }

            long costCoefficient = 3 * bxPrime - axPrime;

            long t_opt;
            if (costCoefficient > 0) {
                t_opt = t_min;
            } else if (costCoefficient < 0) {
                t_opt = t_max;
            } else {
                t_opt = t_min;
            }

            long a = a0 + bxPrime * t_opt;
            long b = b0 - axPrime * t_opt;

            if (a < 0 || b < 0) {
                return -1;
            }

            return 3 * a + b;
        }
    }
}
