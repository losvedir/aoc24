package day13part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GPTo1 {
    record Machine(int aX, int aY, int bX, int bY, int prizeX, int prizeY) {
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("input/day13.txt"));
        var machines = parseMachines(lines);

        int totalCost = 0;
        int solvableCount = 0;

        for (var m : machines) {
            int minCost = solveMachine(m);
            if (minCost >= 0) {
                solvableCount++;
                totalCost += minCost;
            }
        }

        // Print the minimum tokens required to solve all solvable machines.
        // The number of solvable prizes is solvableCount (if needed).
        System.out.println(totalCost);
    }

    private static int solveMachine(Machine machine) {
        // Brute force search:
        // A_count and B_count in [0..100]
        int aX = machine.aX();
        int aY = machine.aY();
        int bX = machine.bX();
        int bY = machine.bY();
        int pX = machine.prizeX();
        int pY = machine.prizeY();

        int minCost = Integer.MAX_VALUE;
        for (int A = 0; A <= 100; A++) {
            for (int B = 0; B <= 100; B++) {
                if (A * aX + B * bX == pX && A * aY + B * bY == pY) {
                    int cost = A * 3 + B;
                    if (cost < minCost) {
                        minCost = cost;
                    }
                }
            }
        }

        return (minCost == Integer.MAX_VALUE) ? -1 : minCost;
    }

    private static ArrayList<Machine> parseMachines(java.util.List<String> lines) {
        // The input format (example):
        // Button A: X+94, Y+34
        // Button B: X+22, Y+67
        // Prize: X=8400, Y=5400
        //
        // Followed by a blank line, then next machine.

        var machines = new ArrayList<Machine>();
        var buffer = new ArrayList<String>();
        for (var line : lines) {
            if (line.isBlank()) {
                if (!buffer.isEmpty()) {
                    machines.add(parseOneMachine(buffer));
                    buffer.clear();
                }
            } else {
                buffer.add(line);
            }
        }
        // Add last machine if there's no trailing blank line
        if (!buffer.isEmpty()) {
            machines.add(parseOneMachine(buffer));
        }

        return machines;
    }

    private static Machine parseOneMachine(ArrayList<String> lines) {
        // We expect three lines:
        // 1: Button A: X+XX, Y+YY
        // 2: Button B: X+XX, Y+YY
        // 3: Prize: X=XXXX, Y=YYYY

        // Parse A line
        var aLine = lines.get(0).strip();
        // Example: "Button A: X+94, Y+34"
        // Split by colon first:
        var afterAColon = aLine.split(":", 2)[1].trim();
        // afterAColon = "X+94, Y+34"
        var partsA = afterAColon.split(",");
        // partsA[0] = "X+94"
        // partsA[1] = " Y+34"
        int aX = parseCoordinate(partsA[0].trim());
        int aY = parseCoordinate(partsA[1].trim());

        // Parse B line
        var bLine = lines.get(1).strip();
        var afterBColon = bLine.split(":", 2)[1].trim();
        var partsB = afterBColon.split(",");
        int bX = parseCoordinate(partsB[0].trim());
        int bY = parseCoordinate(partsB[1].trim());

        // Parse Prize line
        var pLine = lines.get(2).strip();
        // "Prize: X=8400, Y=5400"
        var afterPColon = pLine.split(":", 2)[1].trim();
        var partsP = afterPColon.split(",");
        int pX = parsePrizeCoordinate(partsP[0].trim());
        int pY = parsePrizeCoordinate(partsP[1].trim());

        return new Machine(aX, aY, bX, bY, pX, pY);
    }

    private static int parseCoordinate(String s) {
        // s like "X+94" or "Y+34" or "X-10" etc.
        // Remove 'X' or 'Y': we get "+94" or "-10" etc.
        var valStr = s.substring(1);
        return Integer.parseInt(valStr);
    }

    private static int parsePrizeCoordinate(String s) {
        // s like "X=8400" or "Y=5400"
        // remove 'X=' or 'Y='
        var valStr = s.substring(2);
        return Integer.parseInt(valStr);
    }
}
