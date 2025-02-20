package day04part1;

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
                count += checkWord(grid, i, j);
            }
        }

        System.out.println(count);
    }

    private static int checkWord(char[][] grid, int row, int col) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        String word = "XMAS";

        // Check horizontal
        if (col + word.length() <= cols) {
            if (checkString(grid, row, col, 0, 1, word))
                count++;
        }
        if (col - word.length() + 1 >= 0) {
            if (checkString(grid, row, col, 0, -1, word))
                count++;
        }

        // Check vertical
        if (row + word.length() <= rows) {
            if (checkString(grid, row, col, 1, 0, word))
                count++;
        }
        if (row - word.length() + 1 >= 0) {
            if (checkString(grid, row, col, -1, 0, word))
                count++;
        }

        // Check diagonal (top-left to bottom-right)
        if (row + word.length() <= rows && col + word.length() <= cols) {
            if (checkString(grid, row, col, 1, 1, word))
                count++;
        }
        if (row - word.length() + 1 >= 0 && col - word.length() + 1 >= 0) {
            if (checkString(grid, row, col, -1, -1, word))
                count++;
        }

        // Check diagonal (top-right to bottom-left)
        if (row + word.length() <= rows && col - word.length() + 1 >= 0) {
            if (checkString(grid, row, col, 1, -1, word))
                count++;
        }
        if (row - word.length() + 1 >= 0 && col + word.length() <= cols) {
            if (checkString(grid, row, col, -1, 1, word))
                count++;
        }

        return count;
    }

    private static boolean checkString(char[][] grid, int row, int col, int rowInc, int colInc, String word) {
        for (int i = 0; i < word.length(); i++) {
            if (grid[row][col] != word.charAt(i)) {
                return false;
            }
            row += rowInc;
            col += colInc;
        }
        return true;
    }
}
