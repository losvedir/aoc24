package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day10Part2o1 {
    public static void main(String[] args) {
        int[][] map = readMap("input/day10.txt");

        int rows = map.length;
        int cols = map[0].length;

        // Find all trailheads (cells with height 0)
        // We'll sum up their ratings in the end.
        // The rating of a trailhead is the number of distinct hiking trails
        // that start at that trailhead and end at any cell of height 9.
        // A "hiking trail" is defined as a path that increments height by exactly 1
        // each step.

        // We will use a DP (memoization) approach to count the number of distinct paths
        // from each cell to a height 9 cell.
        // dp[r][c] = number of distinct paths from cell (r,c) to any height-9 cell
        // following the rules.
        // If map[r][c] = 9, dp[r][c] = 1 (it's a trail endpoint).
        // Otherwise dp[r][c] = sum of dp of neighbors that have height current+1.

        long[][] dp = new long[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                dp[r][c] = -1; // -1 indicates uncomputed
            }
        }

        // Directions for neighbors (up, down, left, right)
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        // Memoized DFS function
        java.util.function.BiFunction<Integer, Integer, Long> dfs = new java.util.function.BiFunction<>() {
            @Override
            public Long apply(Integer r, Integer c) {
                if (dp[r][c] != -1) {
                    return dp[r][c];
                }

                int currentHeight = map[r][c];
                if (currentHeight == 9) {
                    dp[r][c] = 1;
                    return 1L;
                }

                long paths = 0;
                for (int[] d : dirs) {
                    int nr = r + d[0];
                    int nc = c + d[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                        continue;
                    if (map[nr][nc] == currentHeight + 1) {
                        paths += this.apply(nr, nc);
                    }
                }

                dp[r][c] = paths;
                return paths;
            }
        };

        // Now find all trailheads and sum up their dp values
        long totalRating = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == 0) {
                    totalRating += dfs.apply(r, c);
                }
            }
        }

        System.out.println(totalRating);
    }

    private static int[][] readMap(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            int rows = lines.size();
            int cols = lines.get(0).length();
            int[][] map = new int[rows][cols];
            for (int r = 0; r < rows; r++) {
                char[] chars = lines.get(r).toCharArray();
                for (int c = 0; c < cols; c++) {
                    map[r][c] = chars[c] - '0';
                }
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input file", e);
        }
    }
}
