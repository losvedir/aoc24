// Chat-GPT 4o

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day4 {
    public static void main(String[] args) throws IOException {
        String[] wordSearch = Files.readAllLines(Path.of("input/day4.txt")).toArray(new String[0]);

        String wordToFind = "XMAS";
        // int occurrences = countOccurrences(wordSearch, wordToFind);
        int occurrences = countOccurrencesPart2(wordSearch, wordToFind);
        System.out.println("The word '" + wordToFind + "' appears " + occurrences + " times.");
    }

    public static int countOccurrences(String[] grid, String word) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length();
        int wordLength = word.length();

        // Directions: {rowDelta, colDelta}
        int[][] directions = {
                { 0, 1 }, // right
                { 1, 0 }, // down
                { 0, -1 }, // left
                { -1, 0 }, // up
                { 1, 1 }, // down-right
                { 1, -1 }, // down-left
                { -1, 1 }, // up-right
                { -1, -1 } // up-left
        };

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Check all directions
                for (int[] dir : directions) {
                    if (matchesWord(grid, word, row, col, dir[0], dir[1])) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static int countOccurrencesPart2(String[] grid, String word) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length();

        // Iterate over each potential center of an X-MAS pattern
        for (int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                // Check for an X-MAS pattern with the current cell as the center
                if (isXMAS(grid, row, col)) {
                    count++;
                }
            }
        }

        return count;
    }

    private static boolean isXMAS(String[] grid, int centerRow, int centerCol) {
        // Define the relative positions of the M, A, and S in the X pattern
        int[][] positions = {
                { -1, -1 }, // top-left M
                { -1, 1 }, // top-right S
                { 1, -1 }, // bottom-left M
                { 1, 1 } // bottom-right S
        };

        char[] pattern = { 'M', 'A', 'S' };

        // Check both forward and reversed MAS in all four arms
        for (boolean reversed : new boolean[] { false, true }) {
            boolean valid = true;
            for (int i = 0; i < positions.length; i++) {
                int row = centerRow + positions[i][0];
                int col = centerCol + positions[i][1];

                // Check bounds
                if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length()) {
                    valid = false;
                    break;
                }

                // Check characters (accounting for reversal)
                char expectedChar = pattern[reversed ? 2 - i % 3 : i % 3];
                if (grid[row].charAt(col) != expectedChar) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesWord(String[] grid, String word, int startRow, int startCol, int rowDelta,
            int colDelta) {
        int rows = grid.length;
        int cols = grid[0].length();
        int wordLength = word.length();

        for (int i = 0; i < wordLength; i++) {
            int newRow = startRow + i * rowDelta;
            int newCol = startCol + i * colDelta;

            // Check boundaries
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                return false;
            }

            // Check character match
            if (grid[newRow].charAt(newCol) != word.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
