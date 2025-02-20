package day04part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Sonnet {
    private static final String TARGET = "XMAS";
    private static final int[] DX = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] DY = { -1, 0, 1, -1, 1, -1, 0, 1 };

    public static void main(String[] args) {
        try {
            String input = Files.readString(Path.of("input/day4.txt"));
            System.out.println("Found " + solve(input) + " occurrences of XMAS");
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static int solve(String input) {
        List<String> grid = input.lines().toList();
        if (grid.isEmpty())
            return 0;

        int rows = grid.size();
        int cols = grid.get(0).length();
        int count = 0;

        // Check each starting position
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Try all 8 directions from each position
                for (int dir = 0; dir < 8; dir++) {
                    if (checkDirection(grid, row, col, dir)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static boolean checkDirection(List<String> grid, int startRow, int startCol, int direction) {
        if (grid.get(startRow).charAt(startCol) != TARGET.charAt(0)) {
            return false;
        }

        int rows = grid.size();
        int cols = grid.get(0).length();

        // Check if the word would fit in this direction
        int endRow = startRow + DX[direction] * (TARGET.length() - 1);
        int endCol = startCol + DY[direction] * (TARGET.length() - 1);

        if (endRow < 0 || endRow >= rows || endCol < 0 || endCol >= cols) {
            return false;
        }

        // Check each character of the target word
        for (int i = 1; i < TARGET.length(); i++) {
            int currentRow = startRow + DX[direction] * i;
            int currentCol = startCol + DY[direction] * i;

            if (grid.get(currentRow).charAt(currentCol) != TARGET.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
