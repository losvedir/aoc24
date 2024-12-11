package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10Part14o {

    public static void main(String[] args) {
        String inputFilePath = "input/day10.txt";
        try {
            List<String> lines = Files.readAllLines(Path.of(inputFilePath));
            int[][] map = parseInput(lines);
            int result = calculateTrailheadScores(map);
            System.out.println("Sum of trailhead scores: " + result);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static int[][] parseInput(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] map = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = lines.get(i).charAt(j) - '0';
            }
        }

        return map;
    }

    private static int calculateTrailheadScores(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        int totalScore = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 0) {
                    totalScore += bfs(map, i, j);
                }
            }
        }

        return totalScore;
    }

    private static int bfs(int[][] map, int startRow, int startCol) {
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startRow, startCol });
        visited[startRow][startCol] = true;

        int reachableNines = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            if (map[row][col] == 9) {
                reachableNines++;
                continue;
            }

            for (int[] direction : DIRECTIONS) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (isValid(map, visited, row, col, newRow, newCol)) {
                    queue.add(new int[] { newRow, newCol });
                    visited[newRow][newCol] = true;
                }
            }
        }

        return reachableNines;
    }

    private static boolean isValid(int[][] map, boolean[][] visited, int currentRow, int currentCol, int newRow,
            int newCol) {
        int rows = map.length;
        int cols = map[0].length;

        return newRow >= 0 && newRow < rows &&
                newCol >= 0 && newCol < cols &&
                !visited[newRow][newCol] &&
                map[newRow][newCol] == map[currentRow][currentCol] + 1;
    }

    private static final int[][] DIRECTIONS = {
            { -1, 0 }, // Up
            { 1, 0 }, // Down
            { 0, -1 }, // Left
            { 0, 1 } // Right
    };
}
