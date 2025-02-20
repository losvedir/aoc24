package day04part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Sonnet {
    private static final String TARGET = "MAS";
    // Diagonal directions for the two arms of the X
    private static final int[][] DIRECTIONS = {
            { -1, -1, 1, 1 }, // row changes for NW and SE arms
            { -1, 1, -1, 1 } // col changes for NW and SE arms
    };

    public static void main(String[] args) {
        try {
            String input = Files.readString(Path.of("input/day4.txt"));
            System.out.println("Found " + solve(input) + " X-MAS patterns");
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

        // Check each possible center point of the X
        for (int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                // The center must be 'A'
                if (grid.get(row).charAt(col) != 'A')
                    continue;

                count += countValidXPatterns(grid, row, col);
            }
        }

        return count;
    }

    private static int countValidXPatterns(List<String> grid, int centerRow, int centerCol) {
        int count = 0;
        int rows = grid.size();
        int cols = grid.get(0).length();

        // Check both possible orientations of "MAS" on each diagonal
        for (boolean nwMasForward : new boolean[] { true, false }) {
            for (boolean seMasForward : new boolean[] { true, false }) {
                // Check NW arm
                int r = centerRow + DIRECTIONS[0][nwMasForward ? 0 : 1];
                int c = centerCol + DIRECTIONS[1][nwMasForward ? 0 : 1];
                if (r < 0 || r >= rows || c < 0 || c >= cols ||
                        grid.get(r).charAt(c) != (nwMasForward ? 'M' : 'S')) {
                    continue;
                }

                r = centerRow + DIRECTIONS[0][nwMasForward ? 1 : 0];
                c = centerCol + DIRECTIONS[1][nwMasForward ? 1 : 0];
                if (r < 0 || r >= rows || c < 0 || c >= cols ||
                        grid.get(r).charAt(c) != (nwMasForward ? 'S' : 'M')) {
                    continue;
                }

                // Check SE arm
                r = centerRow + DIRECTIONS[0][seMasForward ? 2 : 3];
                c = centerCol + DIRECTIONS[1][seMasForward ? 2 : 3];
                if (r < 0 || r >= rows || c < 0 || c >= cols ||
                        grid.get(r).charAt(c) != (seMasForward ? 'M' : 'S')) {
                    continue;
                }

                r = centerRow + DIRECTIONS[0][seMasForward ? 3 : 2];
                c = centerCol + DIRECTIONS[1][seMasForward ? 3 : 2];
                if (r < 0 || r >= rows || c < 0 || c >= cols ||
                        grid.get(r).charAt(c) != (seMasForward ? 'S' : 'M')) {
                    continue;
                }

                count++;
            }
        }

        return count;
    }
}
