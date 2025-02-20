package day04part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GPT4o {
    private static final String TARGET_WORD = "XMAS";
    private static final int[] DX = { 1, 1, 1, 0, 0, -1, -1, -1 }; // Row movement
    private static final int[] DY = { 1, 0, -1, 1, -1, 1, 0, -1 }; // Column movement

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of("input/day4.txt"));
            char[][] grid = parseGrid(lines);
            int count = countOccurrences(grid);
            System.out.println("Total occurrences of XMAS: " + count);
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

    private static int countOccurrences(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int wordLength = TARGET_WORD.length();
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int dir = 0; dir < 8; dir++) {
                    if (matchesWord(grid, r, c, DX[dir], DY[dir], wordLength)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static boolean matchesWord(char[][] grid, int startRow, int startCol, int dRow, int dCol, int wordLength) {
        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < wordLength; i++) {
            int r = startRow + i * dRow;
            int c = startCol + i * dCol;

            if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != TARGET_WORD.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
