package day13part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GPT4o {

    static class ClawMachine {
        int ax, ay, bx, by, px, py;

        public ClawMachine(int ax, int ay, int bx, int by, int px, int py) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.px = px;
            this.py = py;
        }

        public int minTokensToWin() {
            int minTokens = Integer.MAX_VALUE;

            for (int aPresses = 0; aPresses <= 100; aPresses++) {
                for (int bPresses = 0; bPresses <= 100; bPresses++) {
                    int totalX = aPresses * ax + bPresses * bx;
                    int totalY = aPresses * ay + bPresses * by;

                    if (totalX == px && totalY == py) {
                        int cost = aPresses * 3 + bPresses;
                        minTokens = Math.min(minTokens, cost);
                    }
                }
            }

            return minTokens == Integer.MAX_VALUE ? -1 : minTokens;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of("input/day13.txt"));
            List<ClawMachine> machines = parseInput(lines);

            int totalTokens = 0;
            int prizesWon = 0;

            for (ClawMachine machine : machines) {
                int tokens = machine.minTokensToWin();
                if (tokens != -1) {
                    totalTokens += tokens;
                    prizesWon++;
                }
            }

            System.out.println("Prizes won: " + prizesWon);
            System.out.println("Total tokens spent: " + totalTokens);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static List<ClawMachine> parseInput(List<String> lines) {
        List<ClawMachine> machines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 4) {
            String[] aParts = lines.get(i).split(" ");
            int ax = Integer.parseInt(aParts[2].replace("X+", "").replace(",", ""));
            int ay = Integer.parseInt(aParts[3].replace("Y+", ""));

            String[] bParts = lines.get(i + 1).split(" ");
            int bx = Integer.parseInt(bParts[2].replace("X+", "").replace(",", ""));
            int by = Integer.parseInt(bParts[3].replace("Y+", ""));

            String[] pParts = lines.get(i + 2).split(" ");
            int px = Integer.parseInt(pParts[1].replace("X=", "").replace(",", ""));
            int py = Integer.parseInt(pParts[2].replace("Y=", ""));

            machines.add(new ClawMachine(ax, ay, bx, by, px, py));
        }

        return machines;
    }
}
