package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day10Part1o1Pro {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day10.txt"));

        // Parse the input into a 2D grid of integers representing heights
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] map = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            char[] chars = lines.get(r).toCharArray();
            for (int c = 0; c < cols; c++) {
                map[r][c] = chars[c] - '0';
            }
        }

        // We need to find all trailheads (cells with height 0)
        // For each trailhead, find how many distinct '9' cells are reachable via a path
        // that increments height by exactly 1 each step (0->1->2->...->9).

        // Directions for orthogonal movement
        int[][] directions = {
                { -1, 0 },
                { 1, 0 },
                { 0, -1 },
                { 0, 1 }
        };

        // A function to find reachable 9s from a given start (trailhead) cell
        // We can use BFS or DFS. BFS is straightforward here.
        // We track visited states to avoid re-exploration.
        // Since heights must strictly increase by 1 each step, no cycles can form.

        int sumOfScores = 0;

        for (int startR = 0; startR < rows; startR++) {
            for (int startC = 0; startC < cols; startC++) {
                if (map[startR][startC] == 0) {
                    // This is a trailhead
                    Set<String> visited = new HashSet<>();
                    Set<String> reachableNines = new HashSet<>();
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[] { startR, startC, 0 }); // r, c, currentHeight
                    visited.add(startR + "," + startC);

                    while (!queue.isEmpty()) {
                        int[] cur = queue.poll();
                        int cr = cur[0];
                        int cc = cur[1];
                        int ch = cur[2];

                        // If we have reached height 9, record this cell
                        if (ch == 9) {
                            reachableNines.add(cr + "," + cc);
                            // Even if we found a 9, we don't continue from here because
                            // there's no height 10. So no need to enqueue neighbors from here.
                            continue;
                        }

                        // Otherwise, try to move to cells of height ch+1
                        int nextHeight = ch + 1;
                        for (int[] d : directions) {
                            int nr = cr + d[0];
                            int nc = cc + d[1];
                            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                                continue;
                            if (map[nr][nc] == nextHeight) {
                                String key = nr + "," + nc;
                                if (!visited.contains(key)) {
                                    visited.add(key);
                                    queue.offer(new int[] { nr, nc, nextHeight });
                                }
                            }
                        }
                    }

                    // The score for this trailhead is the number of distinct reachable 9 cells
                    sumOfScores += reachableNines.size();
                }
            }
        }

        System.out.println(sumOfScores);
    }
}
