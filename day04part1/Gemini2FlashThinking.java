package day04part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Gemini2FlashThinking {

    public static void main(String[] args) {
        Gemini2FlashThinking solver = new Gemini2FlashThinking();
        int count = solver.countXMASOccurrences("input/day4.txt");
        System.out.println(count);
    }

    public int countXMASOccurrences(String filename) {
        char[][] grid = readGrid(filename);
        if (grid == null) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        char[] target = { 'X', 'M', 'A', 'S' };
        int[][] directions = {
                { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, // Horizontal, Vertical
                { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } // Diagonal
        };

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int[] dir : directions) {
                    if (isXMAS(grid, r, c, dir, target)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean isXMAS(char[][] grid, int startRow, int startCol, int[] direction, char[] target) {
        int rows = grid.length;
        int cols = grid[0].length;

        if (grid[startRow][startCol] != target[0]) {
            return false;
        }

        for (int i = 1; i < target.length; i++) {
            int currentRow = startRow + i * direction[0];
            int currentCol = startCol + i * direction[1];

            if (currentRow < 0 || currentRow >= rows || currentCol < 0 || currentCol >= cols) {
                return false;
            }
            if (grid[currentRow][currentCol] != target[i]) {
                return false;
            }
        }
        return true;
    }

    private char[][] readGrid(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
            return null;
        }

        if (lines.isEmpty()) {
            return null;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }
}
