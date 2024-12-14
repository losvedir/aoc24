package day12part2;

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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Point))
                return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static class Edge {
        final Point p1, p2;

        Edge(Point p1, Point p2) {
            // Ensure consistent ordering for edge comparison
            if (p1.row < p2.row || (p1.row == p2.row && p1.col < p2.col)) {
                this.p1 = p1;
                this.p2 = p2;
            } else {
                this.p1 = p2;
                this.p2 = p1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Edge))
                return false;
            Edge edge = (Edge) o;
            return p1.equals(edge.p1) && p2.equals(edge.p2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p1, p2);
        }
    }

    private static class Region {
        char type;
        int area = 0;
        Set<Edge> edges = new HashSet<>();

        Region(char type) {
            this.type = type;
        }

        long getPrice() {
            return (long) area * edges.size();
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
        Point start = new Point(startRow, startCol);
        queue.offer(start);
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            region.area++;

            // Check all four sides for edge calculation
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (!isValid(newRow, newCol) || grid[newRow][newCol] != type) {
                    // This is a boundary edge
                    Point edgePoint = new Point(
                            current.row + dir[0] / 2,
                            current.col + dir[1] / 2);
                    region.edges.add(new Edge(current, edgePoint));
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
