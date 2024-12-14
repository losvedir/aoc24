package day13part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Opus {
    private static class Machine {
        int aX, aY;
        int bX, bY;
        int prizeX, prizeY;

        public Machine(int aX, int aY, int bX, int bY, int prizeX, int prizeY) {
            this.aX = aX;
            this.aY = aY;
            this.bX = bX;
            this.bY = bY;
            this.prizeX = prizeX;
            this.prizeY = prizeY;
        }

        public int calculateMinTokens() {
            for (int a = 0; a <= 100; a++) {
                for (int b = 0; b <= 100; b++) {
                    if (a * aX + b * bX == prizeX && a * aY + b * bY == prizeY) {
                        return a * 3 + b;
                    }
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day13.txt"));
        List<Machine> machines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 4) {
            int aX = Integer.parseInt(lines.get(i).split("X\\+|,")[1]);
            int aY = Integer.parseInt(lines.get(i).split("Y\\+")[1]);
            int bX = Integer.parseInt(lines.get(i + 1).split("X\\+|,")[1]);
            int bY = Integer.parseInt(lines.get(i + 1).split("Y\\+")[1]);
            int prizeX = Integer.parseInt(lines.get(i + 2).split("X=|,")[1]);
            int prizeY = Integer.parseInt(lines.get(i + 2).split("Y=")[1]);

            machines.add(new Machine(aX, aY, bX, bY, prizeX, prizeY));
        }

        int totalTokens = 0;
        int prizesWon = 0;

        for (Machine machine : machines) {
            int minTokens = machine.calculateMinTokens();
            if (minTokens != -1) {
                totalTokens += minTokens;
                prizesWon++;
            }
        }

        System.out.println("Minimum tokens needed to win " + prizesWon + " prizes: " + totalTokens);
    }
}
