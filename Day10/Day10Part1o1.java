package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day10Part1o1 {
    public static void main(String[] args) {
        int[][] map = readMap("input/day10.txt");

        // Dimensions of the map
        int rows = map.length;
        int cols = map[0].length;

        // Find all trailheads (cells with height 0)
        List<int[]> trailheads = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == 0) {
                    trailheads.add(new int[] { r, c });
                }
            }
        }

        // For each trailhead, we do a BFS/DFS to find all reachable '9' cells
        // that can be reached via a strictly ascending path (each step height+1).
        int totalScore = 0;
        for (int[] th : trailheads) {
            Set<String> reachableNines = new HashSet<>();
            boolean[][] visited = new boolean[rows][cols];
            Deque<int[]> queue = new ArrayDeque<>();
            queue.add(th);
            visited[th[0]][th[1]] = true;

            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int cr = current[0], cc = current[1];
                int ch = map[cr][cc];

                // If we've reached height 9, record this location
                if (ch == 9) {
                    reachableNines.add(cr + "," + cc);
                    // We don't need to continue from a 9 cell, as we can't go higher than 9
                    continue;
                }

                // Try all 4 directions
                int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
                for (int[] d : dirs) {
                    int nr = cr + d[0];
                    int nc = cc + d[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                        continue;
                    if (!visited[nr][nc] && map[nr][nc] == ch + 1) {
                        visited[nr][nc] = true;
                        queue.add(new int[] { nr, nc });
                    }
                }
            }

            totalScore += reachableNines.size();
        }

        System.out.println(totalScore);
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
