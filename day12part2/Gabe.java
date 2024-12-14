package day12part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.*;

public class Gabe {
    public static void main(String[] args) throws IOException {
        char[][] grid = readInput();
        List<Set<Point>> regions = findRegions(grid);

        long price = 0;

        for (Set<Point> region : regions) {
            price += region.size() * numSides(region);
        }
        // System.out.println(regions);
        System.out.println("price: " + price);
    }

    private static char[][] readInput() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day12.txt"));
        char[][] grid = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

    private static List<Set<Point>> findRegions(char[][] grid) {
        List<Set<Point>> regions = new ArrayList<>();
        Set<Point> handled = new HashSet<>();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                Point start = new Point(x, y);
                if (handled.contains(start)) {
                    continue;
                }

                Set<Point> region = exploreRegion(grid, start, handled);
                if (!region.isEmpty()) {
                    regions.add(region);
                }
            }
        }

        return regions;
    }

    private static Set<Point> exploreRegion(char[][] grid, Point start, Set<Point> handled) {
        Set<Point> region = new HashSet<>();
        char targetChar = grid[start.y][start.x];
        Queue<Point> queue = new LinkedList<>();
        queue.offer(start);
        handled.add(start);
        region.add(start);

        // Directions: right, down, left, up
        int[][] directions = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];
                Point next = new Point(newX, newY);

                if (isValid(grid, newX, newY) &&
                        !handled.contains(next) &&
                        grid[newY][newX] == targetChar) {

                    queue.offer(next);
                    handled.add(next);
                    region.add(next);
                }
            }
        }

        return region;
    }

    private static boolean isValid(char[][] grid, int x, int y) {
        return y >= 0 && y < grid.length && x >= 0 && x < grid[0].length;
    }

    private static int numSides(Set<Point> region) {
        Set<Edge> edges = new HashSet<>();

        for (Point p : region) {
            // west edge
            if (!region.contains(new Point(p.x - 1, p.y))) {
                edges.add(new Edge(p.x, p.y, 'v'));
            }
            // north edge
            if (!region.contains(new Point(p.x, p.y - 1))) {
                edges.add(new Edge(p.x, p.y, 'h'));
            }
            // east edge
            if (!region.contains(new Point(p.x + 1, p.y))) {
                edges.add(new Edge(p.x + 1, p.y, 'v'));
            }
            // south edge
            if (!region.contains(new Point(p.x, p.y + 1))) {
                edges.add(new Edge(p.x, p.y + 1, 'h'));
            }

        }
        // System.out.println("region: " + region);
        // System.out.println("edges: " + edges);

        int sides = 0;

        while (edges.size() > 0) {
            var i = edges.iterator();
            Edge e = i.next();
            i.remove();

            sides += 1;

            // if the edge is horizontal
            if (e.o == 'h') {
                int dx = 0;
                // check for adjacent edges to the right
                while (true) {
                    dx += 1;
                    Edge toCheck = new Edge(e.x + dx, e.y, e.o);
                    if (edges.contains(toCheck)) {
                        edges.remove(toCheck);
                    } else {
                        break;
                    }
                }
                dx = 0;
                // check for adjacent edges to the left
                while (true) {
                    dx -= 1;
                    Edge toCheck = new Edge(e.x + dx, e.y, e.o);
                    if (edges.contains(toCheck)) {
                        edges.remove(toCheck);
                    } else {
                        break;
                    }
                }
            } else {
                // the edge is vertical
                int dy = 0;
                // check for adjacent edges going down
                while (true) {
                    dy += 1;
                    Edge toCheck = new Edge(e.x, e.y + dy, e.o);
                    if (edges.contains(toCheck)) {
                        edges.remove(toCheck);
                    } else {
                        break;
                    }
                }
                dy = 0;
                while (true) {
                    dy -= 1;
                    Edge toCheck = new Edge(e.x, e.y + dy, e.o);
                    if (edges.contains(toCheck)) {
                        edges.remove(toCheck);
                    } else {
                        break;
                    }
                }
            }
        }

        return sides;
    }

    private record Edge(int x, int y, char o) {
    }

    private static class Point {
        final int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Point))
                return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
