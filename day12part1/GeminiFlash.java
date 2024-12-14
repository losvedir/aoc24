package day12part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class GeminiFlash {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day12.txt"));
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        Set<Region> regions = new HashSet<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length()];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length(); j++) {
                if (!visited[i][j]) {
                    Region region = new Region(grid[i][j]);
                    dfs(grid, visited, i, j, region);
                    regions.add(region);
                }
            }
        }

        int totalPrice = regions.stream().mapToInt(r -> r.area * r.perimeter).sum();
        System.out.println(totalPrice);
    }

    private static void dfs(char[][] grid, boolean[][] visited, int i, int j, Region region) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j] || grid[i][j] != region.type) {
            return;
        }

        visited[i][j] = true;
        region.area++;
        region.perimeter += 4;

        // Adjust perimeter for shared edges
        if (i > 0 && grid[i - 1][j] == region.type) {
            region.perimeter -= 2;
        }
        if (j > 0 && grid[i][j - 1] == region.type) {
            region.perimeter -= 2;
        }

        dfs(grid, visited, i - 1, j, region);
        dfs(grid, visited, i + 1, j, region);
        dfs(grid, visited, i, j - 1, region);
        dfs(grid, visited, i, j + 1, region);
    }

    private static class Region {
        char type;
        int area;
        int perimeter;

        public Region(char type) {
            this.type = type;
            this.area = 0;
            this.perimeter = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Region region = (Region) o;
            return type == region.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
