package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day10Part2o1Pro {
    // Directions for orthogonal movement
    private static final int[][] DIRECTIONS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day10.txt"));

        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] map = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            char[] chars = lines.get(r).toCharArray();
            for (int c = 0; c < cols; c++) {
                map[r][c] = chars[c] - '0';
            }
        }

        // We now need to count the number of distinct hiking trails starting at each
        // trailhead.
        // A hiking trail:
        // - starts at height 0
        // - ends at height 9
        // - increments height by exactly 1 at each step (0->1->2->...->9)
        // - moves only up/down/left/right

        // The rating of a trailhead is the number of distinct hiking trails that start
        // from it.
        // We must find the sum of the ratings of all trailheads.

        // Approach:
        // Use dynamic programming (memoization):
        // dp[r][c]: number of distinct valid hiking trails starting from (r,c).
        // If cell height = 9, dp[r][c] = 1 (a completed trail if you start here).
        // Otherwise, dp[r][c] = sum of dp of all neighbors that have height =
        // currentHeight+1.
        // If no such neighbors, dp[r][c] = 0.

        // Finally, sum dp[r][c] for all cells where map[r][c] = 0.

        long[][] dp = new long[rows][cols];
        // 0 means uninitialized. We can use -1 to indicate not computed.
        // But since we can have 0 as a legitimate dp result, we must use a separate
        // visited array.
        boolean[][] computed = new boolean[rows][cols];

        long sumOfRatings = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == 0) {
                    sumOfRatings += computeDP(map, dp, computed, r, c);
                }
            }
        }

        System.out.println(sumOfRatings);
    }

    private static long computeDP(int[][] map, long[][] dp, boolean[][] computed, int r, int c) {
        if (computed[r][c]) {
            return dp[r][c];
        }
        computed[r][c] = true;

        int height = map[r][c];
        if (height == 9) {
            dp[r][c] = 1;
            return 1;
        }

        long paths = 0;
        int rows = map.length;
        int cols = map[0].length;
        int nextHeight = height + 1;

        for (int[] d : DIRECTIONS) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                continue;
            }
            if (map[nr][nc] == nextHeight) {
                paths += computeDP(map, dp, computed, nr, nc);
            }
        }

        dp[r][c] = paths;
        return paths;
    }
}
