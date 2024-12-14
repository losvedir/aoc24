package day13part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GPT4o {

    static class ClawMachine {
        int ax, ay, bx, by;
        long px, py;

        public ClawMachine(int ax, int ay, int bx, int by, long px, long py) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.px = px;
            this.py = py;
        }

        public long minTokensToWin() {
            long minTokens = Long.MAX_VALUE;

            // Use a breadth-first search to handle the potentially very large numbers
            // efficiently.
            Queue<State> queue = new LinkedList<>();
            Set<State> visited = new HashSet<>();

            queue.add(new State(0, 0, 0));

            while (!queue.isEmpty()) {
                State current = queue.poll();

                if (visited.contains(current))
                    continue;
                visited.add(current);

                long currentX = current.aPresses * ax + current.bPresses * bx;
                long currentY = current.aPresses * ay + current.bPresses * by;

                if (currentX == px && currentY == py) {
                    minTokens = Math.min(minTokens, current.aPresses * 3 + current.bPresses);
                    continue;
                }

                // Add the next states (pressing A or B)
                if (current.aPresses + 1 <= 1000) {
                    queue.add(new State(current.aPresses + 1, current.bPresses, current.tokens + 3));
                }
                if (current.bPresses + 1 <= 1000) {
                    queue.add(new State(current.aPresses, current.bPresses + 1, current.tokens + 1));
                }
            }

            return minTokens == Long.MAX_VALUE ? -1 : minTokens;
        }
    }

    static class State {
        int aPresses;
        int bPresses;
        long tokens;

        public State(int aPresses, int bPresses, long tokens) {
            this.aPresses = aPresses;
            this.bPresses = bPresses;
            this.tokens = tokens;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            State state = (State) o;
            return aPresses == state.aPresses && bPresses == state.bPresses;
        }

        @Override
        public int hashCode() {
            return Objects.hash(aPresses, bPresses);
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of("input/day13.txt"));
            List<ClawMachine> machines = parseInput(lines);

            long totalTokens = 0;
            int prizesWon = 0;

            for (ClawMachine machine : machines) {
                long tokens = machine.minTokensToWin();
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
            long px = Long.parseLong(pParts[1].replace("X=", "").replace(",", "")) + 10_000_000_000_00L;
            long py = Long.parseLong(pParts[2].replace("Y=", "").replace(",", "")) + 10_000_000_000_00L;

            machines.add(new ClawMachine(ax, ay, bx, by, px, py));
        }

        return machines;
    }
}
