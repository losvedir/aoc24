package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10Part24o {

    public static void main(String[] args) {
        String inputFilePath = "input/day10.txt";
        try {
            List<String> lines = Files.readAllLines(Path.of(inputFilePath));
            int[][] map = parseInput(lines);
            int result = calculateTrailheadRatings(map);
            System.out.println("Sum of trailhead ratings: " + result);
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

    private static int calculateTrailheadRatings(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        int totalRating = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 0) {
                    totalRating += countDistinctTrails(map, i, j);
                }
            }
        }

        return totalRating;
    }

    private static int countDistinctTrails(int[][] map, int startRow, int startCol) {
        int rows = map.length;
        int cols = map[0].length;
        Map<String, Integer> memo = new HashMap<>();

        return dfs(map, startRow, startCol, -1, memo);
    }

    private static int dfs(int[][] map, int row, int col, int previousHeight, Map<String, Integer> memo) {
        int rows = map.length;
        int cols = map[0].length;

        if (row < 0 || row >= rows || col < 0 || col >= cols || map[row][col] <= previousHeight) {
            return 0;
        }

        if (map[row][col] == 9) {
            return 1; // Base case: valid trail ends at height 9
        }

        String key = row + "," + col + "," + previousHeight;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int trails = 0;
        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            trails += dfs(map, newRow, newCol, map[row][col], memo);
        }

        memo.put(key, trails);
        return trails;
    }

    private static final int[][] DIRECTIONS = {
            { -1, 0 }, // Up
            { 1, 0 }, // Down
            { 0, -1 }, // Left
            { 0, 1 } // Right
    };
}
