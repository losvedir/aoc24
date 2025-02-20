package day04part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GPT4o {
    private static final String MAS = "MAS";
    private static final int[][] X_SHAPE_DIRECTIONS = {
            { -1, -1, 1, 1 }, // Top-left to Bottom-right diagonal
            { -1, 1, 1, -1 } // Top-right to Bottom-left diagonal
    };

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of("input/day4.txt"));
            char[][] grid = parseGrid(lines);
            int count = countXMasOccurrences(grid);
            System.out.println("Total occurrences of X-MAS: " + count);
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }

    private static char[][] parseGrid(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

    private static int countXMasOccurrences(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'X') { // Check if this position is a valid center for X-MAS
                    for (int[] dirs : X_SHAPE_DIRECTIONS) {
                        if (matchesXMas(grid, r, c, dirs)) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    private static boolean matchesXMas(char[][] grid, int x, int y, int[] dirs) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Extract the direction vectors for two diagonal MAS paths
        int dx1 = dirs[0], dy1 = dirs[1];
        int dx2 = dirs[2], dy2 = dirs[3];

        return matchesMas(grid, x, y, dx1, dy1) && matchesMas(grid, x, y, dx2, dy2);
    }

    private static boolean matchesMas(char[][] grid, int x, int y, int dx, int dy) {
        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < MAS.length(); i++) {
            int r = x + i * dx;
            int c = y + i * dy;

            if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != MAS.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
