package Day10;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10Part2Sonnet {
    private static final int[] DX = { 0, 1, 0, -1 }; // right, down, left, up
    private static final int[] DY = { 1, 0, -1, 0 };
    private final int[][] grid;
    private final int rows;
    private final int cols;

    public Day10Part2Sonnet(String input) {
        List<String> lines = input.lines().toList();
        rows = lines.size();
        cols = lines.get(0).length();
        grid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
    }

    public long solve() {
        long totalRating = 0;

        // Find all trailheads (positions with height 0)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    totalRating += calculateTrailheadRating(i, j);
                }
            }
        }

        return totalRating;
    }

    private long calculateTrailheadRating(int startX, int startY) {
        // Use dynamic programming to count paths to each cell
        long[][] pathCount = new long[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        Queue<State> queue = new LinkedList<>();

        // Initialize starting point
        queue.offer(new State(startX, startY, 0));
        pathCount[startX][startY] = 1;
        visited[startX][startY] = true;

        // Process cells in order of increasing height
        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Try all four directions
            for (int dir = 0; dir < 4; dir++) {
                int newX = current.x + DX[dir];
                int newY = current.y + DY[dir];

                if (isValid(newX, newY) &&
                        grid[newX][newY] == current.height + 1) {

                    // Add paths from current cell to the new cell
                    pathCount[newX][newY] += pathCount[current.x][current.y];

                    // Only enqueue if we haven't visited this height before
                    if (!visited[newX][newY]) {
                        queue.offer(new State(newX, newY, current.height + 1));
                        visited[newX][newY] = true;
                    }
                }
            }
        }

        // Sum up paths to all height 9 cells
        long totalPaths = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 9) {
                    totalPaths += pathCount[i][j];
                }
            }
        }

        return totalPaths;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private record State(int x, int y, int height) {
    }

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("input/day10.txt"));
        Day10Part2Sonnet solver = new Day10Part2Sonnet(input);
        System.out.println(solver.solve());
    }
}
