package day16part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Opus {
    private static final int FORWARD_COST = 1;
    private static final int ROTATE_COST = 1000;

    private static final int[] DR = { -1, 0, 1, 0 };
    private static final int[] DC = { 0, 1, 0, -1 };

    public static void main(String[] args) throws IOException {
        char[][] maze = Files.lines(Path.of("input/day16.txt"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int startRow = -1, startCol = -1;
        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[0].length; c++) {
                if (maze[r][c] == 'S') {
                    startRow = r;
                    startCol = c;
                    break;
                }
            }
        }

        int lowestScore = findLowestScore(maze, startRow, startCol, 0, 1);
        System.out.println("The lowest score is: " + lowestScore);
    }

    private static int findLowestScore(char[][] maze, int row, int col, int dir, int score) {
        if (maze[row][col] == 'E') {
            return score;
        }

        int lowestScore = Integer.MAX_VALUE;

        // Try moving forward
        int newRow = row + DR[dir];
        int newCol = col + DC[dir];
        if (isValidMove(maze, newRow, newCol)) {
            lowestScore = Math.min(lowestScore, findLowestScore(maze, newRow, newCol, dir, score + FORWARD_COST));
        }

        // Try rotating clockwise and counterclockwise
        int clockwiseDir = (dir + 1) % 4;
        int counterClockwiseDir = (dir + 3) % 4;
        lowestScore = Math.min(lowestScore, findLowestScore(maze, row, col, clockwiseDir, score + ROTATE_COST));
        lowestScore = Math.min(lowestScore, findLowestScore(maze, row, col, counterClockwiseDir, score + ROTATE_COST));

        return lowestScore;
    }

    private static boolean isValidMove(char[][] maze, int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length && maze[row][col] != '#';
    }
}
