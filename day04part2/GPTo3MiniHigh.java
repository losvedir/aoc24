package day04part2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

public class GPTo3MiniHigh {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day4.txt"));
            if (lines.isEmpty()) {
                System.out.println(0);
                return;
            }
            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] grid = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                grid[i] = lines.get(i).toCharArray();
            }

            int count = 0;
            // Iterate over each possible 3x3 block in the grid.
            for (int r = 0; r <= rows - 3; r++) {
                for (int c = 0; c <= cols - 3; c++) {
                    // The center of the X must be an 'A'
                    if (grid[r + 1][c + 1] != 'A') {
                        continue;
                    }

                    // Check the main diagonal (top-left to bottom-right).
                    char topLeft = grid[r][c];
                    char bottomRight = grid[r + 2][c + 2];
                    boolean diag1Valid = (topLeft == 'M' && bottomRight == 'S') ||
                            (topLeft == 'S' && bottomRight == 'M');
                    if (!diag1Valid) {
                        continue;
                    }

                    // Check the anti-diagonal (top-right to bottom-left).
                    char topRight = grid[r][c + 2];
                    char bottomLeft = grid[r + 2][c];
                    boolean diag2Valid = (topRight == 'M' && bottomLeft == 'S') ||
                            (topRight == 'S' && bottomLeft == 'M');
                    if (!diag2Valid) {
                        continue;
                    }

                    // Both diagonals are valid.
                    count++;
                }
            }

            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
