package day13part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GPTo1Pro {

    private static class Machine {
        int Ax, Ay;
        int Bx, By;
        int X, Y;
    }

    public static void main(String[] args) {
        ArrayList<Machine> machines = new ArrayList<>();

        // Parse the input
        try (BufferedReader br = new BufferedReader(new FileReader("input/day13.txt"))) {
            String line;
            ArrayList<String> block = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    if (!block.isEmpty()) {
                        machines.add(parseMachine(block));
                        block.clear();
                    }
                } else {
                    block.add(line);
                }
            }
            // Handle last block if no trailing blank line
            if (!block.isEmpty()) {
                machines.add(parseMachine(block));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // For each machine, determine if there's a solution and track minimal cost
        int countSolvable = 0;
        int totalCost = 0;
        for (Machine m : machines) {
            int bestCost = Integer.MAX_VALUE;
            // According to the problem, we consider up to 100 presses each
            // We'll brute-force all combinations of A and B in [0..100]
            for (int A = 0; A <= 100; A++) {
                for (int B = 0; B <= 100; B++) {
                    long xPos = (long) A * m.Ax + (long) B * m.Bx;
                    long yPos = (long) A * m.Ay + (long) B * m.By;
                    if (xPos == m.X && yPos == m.Y) {
                        int cost = 3 * A + B;
                        if (cost < bestCost) {
                            bestCost = cost;
                        }
                    }
                }
            }
            if (bestCost != Integer.MAX_VALUE) {
                countSolvable++;
                totalCost += bestCost;
            }
        }

        // Print results: first maximize number of prizes, then minimize cost
        // The problem states: "What is the fewest tokens you would have to spend
        // to win all possible prizes?"
        // "All possible prizes" here means all that are solvable.
        System.out.println("Max prizes: " + countSolvable);
        System.out.println("Min total cost: " + totalCost);
    }

    private static Machine parseMachine(ArrayList<String> lines) {
        // Expected lines:
        // Button A: X+Ax, Y+Ay
        // Button B: X+Bx, Y+By
        // Prize: X=X, Y=Y
        Machine m = new Machine();

        // Parse line for Button A
        // Example: "Button A: X+94, Y+34"
        // We can split by ':' and then by ',' or just use a regex
        String lineA = lines.get(0).replace("Button A:", "").trim();
        // lineA: X+94, Y+34
        // Split by comma
        String[] partsA = lineA.split(",");
        // partsA[0] = "X+94"
        // partsA[1] = " Y+34"
        m.Ax = parseAxisValue(partsA[0]);
        m.Ay = parseAxisValue(partsA[1]);

        // Parse line for Button B
        String lineB = lines.get(1).replace("Button B:", "").trim();
        String[] partsB = lineB.split(",");
        m.Bx = parseAxisValue(partsB[0]);
        m.By = parseAxisValue(partsB[1]);

        // Parse prize line
        String lineP = lines.get(2).replace("Prize:", "").trim();
        String[] partsP = lineP.split(",");
        m.X = parseAxisValue(partsP[0]);
        m.Y = parseAxisValue(partsP[1]);

        return m;
    }

    private static int parseAxisValue(String s) {
        s = s.trim();
        // format: X+94 or X=8400
        // Could be "X+something", "X-something", or "X=something"
        // Remove leading 'X' or 'Y'
        if (s.startsWith("X"))
            s = s.substring(1);
        else if (s.startsWith("Y"))
            s = s.substring(1);

        // Now s might start with '+' or '-' or '='
        s = s.replace("=", "");
        return Integer.parseInt(s);
    }
}
