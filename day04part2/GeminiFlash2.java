package day04part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GeminiFlash2 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day4.txt"));
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                count += checkXMAS(grid, i, j);
            }
        }

        System.out.println(count);
    }

    private static int checkXMAS(char[][] grid, int row, int col) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        // Check for X-MAS pattern
        if (row - 1 >= 0 && row + 1 < rows && col - 1 >= 0 && col + 1 < cols) {
            if (grid[row][col] == 'X') {
                if (checkMAS(grid, row - 1, col - 1, 1, 1) && checkMAS(grid, row + 1, col + 1, -1, -1))
                    count++;
                if (checkMAS(grid, row - 1, col + 1, 1, -1) && checkMAS(grid, row + 1, col - 1, -1, 1))
                    count++;
                if (checkMAS(grid, row - 1, col - 1, 1, 1) && checkMAS(grid, row + 1, col + 1, 1, 1))
                    count++; // both forward
                if (checkMAS(grid, row - 1, col + 1, 1, -1) && checkMAS(grid, row + 1, col - 1, 1, -1))
                    count++; // both forward
                if (checkMAS(grid, row - 1, col - 1, -1, -1) && checkMAS(grid, row + 1, col + 1, -1, -1))
                    count++; // both backward
                if (checkMAS(grid, row - 1, col + 1, -1, 1) && checkMAS(grid, row + 1, col - 1, -1, 1))
                    count++; // both backward

            }
        }

        return count;
    }

    private static boolean checkMAS(char[][] grid, int row, int col, int rowInc, int colInc) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return false;
        }
        if (grid[row][col] != 'M')
            return false;
        row += rowInc;
        col += colInc;
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return false;
        }
        if (grid[row][col] != 'A')
            return false;
        row += rowInc;
        col += colInc;
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return false;
        }
        return grid[row][col] == 'S';
    }
}
