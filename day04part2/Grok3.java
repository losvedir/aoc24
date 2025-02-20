package day04part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Grok3 {
    private static final String INPUT_FILE = "input/day4.txt";

    public static void main(String[] args) {
        try {
            int count = countXMASOccurrences();
            System.out.println("Total X-MAS occurrences: " + count);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static int countXMASOccurrences() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
        char[][] grid = createGrid(lines);
        return countXMAS(grid);
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

    private static int countXMAS(char[][] grid) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        // Iterate over potential center cells (not on borders)
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                // Check if the center is 'A'
                if (grid[i][j] == 'A') {
                    // Extract the two diagonals
                    String diag1 = "" + grid[i - 1][j - 1] + grid[i][j] + grid[i + 1][j + 1];
                    String diag2 = "" + grid[i - 1][j + 1] + grid[i][j] + grid[i + 1][j - 1];

                    // Check if both diagonals are "MAS" or "SAM"
                    if ((diag1.equals("MAS") || diag1.equals("SAM")) &&
                            (diag2.equals("MAS") || diag2.equals("SAM"))) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
