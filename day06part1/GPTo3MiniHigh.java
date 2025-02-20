package day06part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GPTo3MiniHigh {
    public static void main(String[] args) throws IOException {
        // Read the grid from the file
        List<String> lines = Files.readAllLines(Paths.get("input/day6.txt"));
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        // Initialize starting position and direction.
        // Direction indices: 0 = up, 1 = right, 2 = down, 3 = left.
        int startRow = -1, startCol = -1, dir = 0;
        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                if (ch == '^' || ch == '>' || ch == 'v' || ch == '<') {
                    startRow = r;
                    startCol = c;
                    if (ch == '^') {
                        dir = 0;
                    } else if (ch == '>') {
                        dir = 1;
                    } else if (ch == 'v') {
                        dir = 2;
                    } else { // ch == '<'
                        dir = 3;
                    }
                    // Replace the guard symbol with empty space so it is not treated as an
                    // obstacle.
                    grid[r][c] = '.';
                } else {
                    grid[r][c] = ch;
                }
            }
        }

        // Set the current position to the starting position.
        int curR = startRow, curC = startCol;
        Set<String> visited = new HashSet<>();

        // Simulate the guard's movement until leaving the mapped area.
        while (curR >= 0 && curR < rows && curC >= 0 && curC < cols) {
            // Record the current position.
            visited.add(curR + "," + curC);

            // Determine the next cell based on the current direction.
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
            int nextR = curR + dr;
            int nextC = curC + dc;

            // If next position is outside the grid, stop the simulation.
            if (nextR < 0 || nextR >= rows || nextC < 0 || nextC >= cols) {
                break;
            }

            // If there is an obstacle directly ahead, turn right (90 degrees).
            if (grid[nextR][nextC] == '#') {
                dir = (dir + 1) % 4;
            } else {
                // Otherwise, take a step forward.
                curR = nextR;
                curC = nextC;
            }
        }

        // Output the number of distinct positions visited.
        System.out.println(visited.size());
    }
}
