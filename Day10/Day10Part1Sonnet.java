package Day10;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10Part1Sonnet {
    private static final int[] DX = { 0, 1, 0, -1 }; // right, down, left, up
    private static final int[] DY = { 1, 0, -1, 0 };
    private final int[][] grid;
    private final int rows;
    private final int cols;

    public Day10Part1Sonnet(String input) {
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

    public int solve() {
        int totalScore = 0;

        // Find all trailheads (positions with height 0)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    totalScore += calculateTrailheadScore(i, j);
                }
            }
        }

        return totalScore;
    }

    private int calculateTrailheadScore(int startX, int startY) {
        Set<Point> reachableNines = new HashSet<>();
        Set<Point> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();

        queue.offer(new State(startX, startY, 0));
        visited.add(new Point(startX, startY));

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // If we've reached a 9, add it to our set of reachable nines
            if (grid[current.x][current.y] == 9) {
                reachableNines.add(new Point(current.x, current.y));
                continue;
            }

            // Try all four directions
            for (int dir = 0; dir < 4; dir++) {
                int newX = current.x + DX[dir];
                int newY = current.y + DY[dir];
                Point newPoint = new Point(newX, newY);

                if (isValid(newX, newY) &&
                        !visited.contains(newPoint) &&
                        grid[newX][newY] == current.height + 1) {

                    queue.offer(new State(newX, newY, current.height + 1));
                    visited.add(newPoint);
                }
            }
        }

        return reachableNines.size();
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private record Point(int x, int y) {
    }

    private record State(int x, int y, int height) {
    }

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("input/day10.txt"));
        Day10Part1Sonnet solver = new Day10Part1Sonnet(input);
        System.out.println(solver.solve());
    }
}
