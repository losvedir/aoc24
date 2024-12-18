package day15part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GeminiPro {

    private static final char WALL = '#';
    private static final char BOX = 'O';
    private static final char ROBOT = '@';
    private static final char EMPTY = '.';

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day15.txt"));
        char[][] grid = parseGrid(lines);
        String moves = lines.get(lines.size() - 1);

        processMoves(grid, moves);

        int gpsSum = calculateGpsSum(grid);
        System.out.println("Sum of GPS coordinates: " + gpsSum);
    }

    private static char[][] parseGrid(List<String> lines) {
        int height = lines.size() - 1;
        int width = lines.get(0).length();
        char[][] grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    private static void processMoves(char[][] grid, String moves) {
        int[] robotPosition = findRobot(grid);
        int row = robotPosition[0];
        int col = robotPosition[1];

        for (char move : moves.toCharArray()) {
            int newRow = row;
            int newCol = col;

            switch (move) {
                case '^':
                    newRow--;
                    break;
                case 'v':
                    newRow++;
                    break;
                case '<':
                    newCol--;
                    break;
                case '>':
                    newCol++;
                    break;
            }

            if (grid[newRow][newCol] == BOX) {
                int nextRow = newRow + (newRow - row);
                int nextCol = newCol + (newCol - col);
                if (isValidPosition(grid, nextRow, nextCol)) {
                    grid[nextRow][nextCol] = BOX;
                    grid[newRow][newCol] = EMPTY;
                } else {
                    continue; // Cannot push the box
                }
            }

            if (isValidPosition(grid, newRow, newCol)) {
                grid[row][col] = EMPTY;
                grid[newRow][newCol] = ROBOT;
                row = newRow;
                col = newCol;
            }
        }
    }

    private static int[] findRobot(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == ROBOT) {
                    return new int[] { i, j };
                }
            }
        }
        return null; // Should not happen
    }

    private static boolean isValidPosition(char[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length && grid[row][col] != WALL;
    }

    private static int calculateGpsSum(char[][] grid) {
        int sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == BOX) {
                    sum += (100 * i) + j;
                }
            }
        }
        return sum;
    }
}
