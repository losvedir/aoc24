package day06part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GPTo3MiniHigh {
    public static void main(String[] args) throws IOException {
        // Read grid from file "input/day6.txt"
        List<String> lines = Files.readAllLines(Paths.get("input/day6.txt"));
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        int startRow = -1, startCol = -1, startDir = 0;
        // Build grid and locate the guard's starting position and initial direction.
        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                if (ch == '^' || ch == '>' || ch == 'v' || ch == '<') {
                    startRow = r;
                    startCol = c;
                    if (ch == '^') {
                        startDir = 0;
                    } else if (ch == '>') {
                        startDir = 1;
                    } else if (ch == 'v') {
                        startDir = 2;
                    } else { // ch == '<'
                        startDir = 3;
                    }
                    // Replace the guard symbol with an empty cell.
                    grid[r][c] = '.';
                } else {
                    grid[r][c] = ch;
                }
            }
        }

        int validObstructionCount = 0;
        // Try placing a new obstruction in every empty cell (except the starting
        // position).
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Skip the guard's starting position.
                if (r == startRow && c == startCol)
                    continue;
                // Only consider originally empty positions.
                if (grid[r][c] != '.')
                    continue;

                // Create a fresh copy of the grid.
                char[][] gridCopy = new char[rows][cols];
                for (int i = 0; i < rows; i++) {
                    gridCopy[i] = grid[i].clone();
                }
                // Place the new obstruction.
                gridCopy[r][c] = '#';

                // Simulate the guard's movement on the modified grid.
                if (simulateGuard(gridCopy, startRow, startCol, startDir)) {
                    validObstructionCount++;
                }
            }
        }

        // Output the count of valid obstruction positions.
        System.out.println(validObstructionCount);
    }

    /**
     * Simulates the guard's patrol on the provided grid.
     * 
     * @param grid     the grid with the added obstruction
     * @param startRow the starting row of the guard
     * @param startCol the starting column of the guard
     * @param startDir the starting direction (0: up, 1: right, 2: down, 3: left)
     * @return true if the guard eventually enters a loop; false if the guard leaves
     *         the grid
     */
    private static boolean simulateGuard(char[][] grid, int startRow, int startCol, int startDir) {
        int rows = grid.length;
        int cols = grid[0].length;
        int r = startRow, c = startCol, dir = startDir;
        Set<String> seenStates = new HashSet<>();

        // Continue simulation until the guard leaves the grid or a loop is detected.
        while (true) {
            // Create a unique state identifier.
            String state = r + "," + c + "," + dir;
            if (seenStates.contains(state)) {
                // Loop detected.
                return true;
            }
            seenStates.add(state);

            // Determine the next cell based on current direction.
            int dr = 0, dc = 0;
            switch (dir) {
                case 0:
                    dr = -1;
                    dc = 0;
                    break; // up
                case 1:
                    dr = 0;
                    dc = 1;
                    break; // right
                case 2:
                    dr = 1;
                    dc = 0;
                    break; // down
                case 3:
                    dr = 0;
                    dc = -1;
                    break; // left
            }
            int nextR = r + dr;
            int nextC = c + dc;

            // If the next position is outside the grid, the guard leaves.
            if (nextR < 0 || nextR >= rows || nextC < 0 || nextC >= cols) {
                return false;
            }

            // If there is an obstruction directly ahead, turn right.
            if (grid[nextR][nextC] == '#') {
                dir = (dir + 1) % 4;
            } else {
                // Otherwise, take a step forward.
                r = nextR;
                c = nextC;
            }
        }
    }
}
