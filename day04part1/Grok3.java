package day04part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Grok3 {
    private static final String TARGET = "XMAS";
    private static final String INPUT_FILE = "input/day4.txt";

    public static void main(String[] args) {
        try {
            int count = countXMASOccurrences();
            System.out.println("Total XMAS occurrences: " + count);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static int countXMASOccurrences() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
        char[][] grid = createGrid(lines);
        return countAllDirections(grid);
    }

    private static char[][] createGrid(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    private static int countAllDirections(char[][] grid) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        // All possible directions: horizontal, vertical, and 4 diagonals
        int[][] directions = {
                { 0, 1 }, // right
                { 0, -1 }, // left
                { 1, 0 }, // down
                { -1, 0 }, // up
                { 1, 1 }, // down-right
                { -1, -1 }, // up-left
                { 1, -1 }, // down-left
                { -1, 1 } // up-right
        };

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int[] dir : directions) {
                    count += checkDirection(grid, i, j, dir[0], dir[1]);
                }
            }
        }
        return count;
    }

    private static int checkDirection(char[][] grid, int row, int col, int dRow, int dCol) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Check if we can fit XMAS in this direction from starting position
        for (int k = 0; k < TARGET.length(); k++) {
            int newRow = row + k * dRow;
            int newCol = col + k * dCol;

            // Check bounds and character match
            if (!isValidPosition(newRow, newCol, rows, cols) ||
                    grid[newRow][newCol] != TARGET.charAt(k)) {
                return 0;
            }
        }
        return 1;
    }

    private static boolean isValidPosition(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}
