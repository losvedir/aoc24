package day12part1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Sonnet {
    private static class Point {
        final int row, col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static class Region {
        char type;
        int area = 0;
        int perimeter = 0;

        Region(char type) {
            this.type = type;
        }

        long getPrice() {
            return (long) area * perimeter;
        }
    }

    private final char[][] grid;
    private final boolean[][] visited;
    private final int rows;
    private final int cols;

    // Four directions: up, right, down, left
    private static final int[][] DIRECTIONS = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

    public Sonnet(String input) {
        String[] lines = input.trim().split("\n");
        rows = lines.length;
        cols = lines[0].length();
        grid = new char[rows][cols];
        visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines[i].toCharArray();
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private Region floodFill(int startRow, int startCol) {
        char type = grid[startRow][startCol];
        Region region = new Region(type);
        Queue<Point> queue = new LinkedList<>();
        queue.offer(new Point(startRow, startCol));
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            region.area++;

            // Check all four sides for perimeter calculation
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (!isValid(newRow, newCol) || grid[newRow][newCol] != type) {
                    // This side contributes to perimeter
                    region.perimeter++;
                } else if (!visited[newRow][newCol]) {
                    // Valid neighbor of same type that hasn't been visited
                    queue.offer(new Point(newRow, newCol));
                    visited[newRow][newCol] = true;
                }
            }
        }

        return region;
    }

    public long solve() {
        List<Region> regions = new ArrayList<>();

        // Find all regions using flood fill
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    regions.add(floodFill(i, j));
                }
            }
        }

        // Calculate total price
        return regions.stream()
                .mapToLong(Region::getPrice)
                .sum();
    }

    public static void main(String[] args) {
        try {
            String input = Files.readString(Paths.get("input/day12.txt"));
            Sonnet solver = new Sonnet(input);
            System.out.println(solver.solve());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
