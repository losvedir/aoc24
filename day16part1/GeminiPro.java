package day16part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GeminiPro {

    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // E, S, W, N
    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day16.txt"));
        char[][] grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);

        int lowestScore = findLowestScore(grid);
        System.out.println("Lowest score: " + lowestScore);
    }

    private static int findLowestScore(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[] start = findStart(grid);

        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.score));
        queue.offer(new State(start[0], start[1], 0, 0)); // Start facing East

        Set<State> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            State curr = queue.poll();

            if (grid[curr.row][curr.col] == END) {
                return curr.score;
            }

            if (!visited.add(curr)) {
                continue;
            }

            // Explore moving forward
            int nextRow = curr.row + DIRECTIONS[curr.dir][0];
            int nextCol = curr.col + DIRECTIONS[curr.dir][1];
            if (isValid(grid, nextRow, nextCol)) {
                queue.offer(new State(nextRow, nextCol, curr.dir, curr.score + 1));
            }

            // Explore turning clockwise and counterclockwise
            queue.offer(new State(curr.row, curr.col, (curr.dir + 1) % 4, curr.score + 1000));
            queue.offer(new State(curr.row, curr.col, (curr.dir + 3) % 4, curr.score + 1000));
        }

        return -1; // Should not reach here if there's a valid path
    }

    private static boolean isValid(char[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length && grid[row][col] != WALL;
    }

    private static int[] findStart(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == START) {
                    return new int[] { i, j };
                }
            }
        }
        throw new IllegalArgumentException("Start position not found");
    }

    private record State(int row, int col, int dir, int score) {
    }
}
