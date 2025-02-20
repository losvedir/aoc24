package day04part2;

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
        char[] masForward = { 'M', 'A', 'S' };
        char[] masBackward = { 'S', 'A', 'M' };

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'A') {
                    if (isXMAS(grid, r, c, masForward, masForward))
                        count++;
                    if (isXMAS(grid, r, c, masForward, masBackward))
                        count++;
                    if (isXMAS(grid, r, c, masBackward, masForward))
                        count++;
                    if (isXMAS(grid, r, c, masBackward, masBackward))
                        count++;
                }
            }
        }
        return count;
    }

    private boolean isXMAS(char[][] grid, int centerRow, int centerCol, char[] verticalMAS, char[] horizontalMAS) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Check Vertical MAS
        int upRow = centerRow - 1;
        int downRow = centerRow + 1;
        if (upRow < 0 || upRow >= rows || downRow < 0 || downRow >= rows)
            return false;
        if (grid[upRow][centerCol] != verticalMAS[0])
            return false;
        if (grid[downRow][centerCol] != verticalMAS[2])
            return false;

        // Check Horizontal MAS
        int leftCol = centerCol - 1;
        int rightCol = centerCol + 1;
        if (leftCol < 0 || leftCol >= cols || rightCol < 0 || rightCol >= cols)
            return false;
        if (grid[centerRow][leftCol] != horizontalMAS[0])
            return false;
        if (grid[centerRow][rightCol] != horizontalMAS[2])
            return false;

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
