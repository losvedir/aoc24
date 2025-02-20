package day04part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Gemini2ProExp {

    private static final String TARGET_WORD = "XMAS";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day4.txt"));
        char[][] grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);

        int count = countOccurrences(grid);
        System.out.println(count);
    }

    private static int countOccurrences(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                count += countFromPosition(grid, r, c);
            }
        }

        return count;
    }

    private static int countFromPosition(char[][] grid, int row, int col) {
        int count = 0;
        int wordLength = TARGET_WORD.length();

        // Check all 8 directions
        int[][] directions = {
                { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, // Horizontal and Vertical
                { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } // Diagonals
        };

        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];

            // Check forward
            if (checkDirection(grid, row, col, dr, dc, TARGET_WORD)) {
                count++;
            }

            // Check backward (by reversing the target word)
            String reversedWord = new StringBuilder(TARGET_WORD).reverse().toString();
            if (checkDirection(grid, row, col, dr, dc, reversedWord)) {
                count++;
            }
        }
        return count;
    }

    private static boolean checkDirection(char[][] grid, int row, int col, int dr, int dc, String word) {
        int wordLength = word.length();

        for (int i = 0; i < wordLength; i++) {
            int newRow = row + i * dr;
            int newCol = col + i * dc;

            if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length ||
                    grid[newRow][newCol] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
