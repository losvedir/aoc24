package day12part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeminiPro {

    private static final String INPUT_FILE = "input/day12.txt";

    public static void main(String[] args) throws IOException {
        char[][] grid = Files.lines(Paths.get(INPUT_FILE))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        GeminiPro solution = new GeminiPro();
        int totalPrice = solution.calculateTotalPrice(grid);
        System.out.println(totalPrice);
    }

    public int calculateTotalPrice(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int totalPrice = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    Region region = exploreRegion(grid, visited, i, j);
                    totalPrice += region.area * region.perimeter;
                }
            }
        }

        return totalPrice;
    }

    private Region exploreRegion(char[][] grid, boolean[][] visited, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;
        char plantType = grid[row][col];
        Region region = new Region();
        List<int[]> queue = new ArrayList<>();
        queue.add(new int[] { row, col });
        visited[row][col] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.remove(0);
            int r = curr[0];
            int c = curr[1];
            region.area++;

            // Check adjacent cells
            if (r > 0 && !visited[r - 1][c] && grid[r - 1][c] == plantType) {
                queue.add(new int[] { r - 1, c });
                visited[r - 1][c] = true;
            }
            if (r < rows - 1 && !visited[r + 1][c] && grid[r + 1][c] == plantType) {
                queue.add(new int[] { r + 1, c });
                visited[r + 1][c] = true;
            }
            if (c > 0 && !visited[r][c - 1] && grid[r][c - 1] == plantType) {
                queue.add(new int[] { r, c - 1 });
                visited[r][c - 1] = true;
            }
            if (c < cols - 1 && !visited[r][c + 1] && grid[r][c + 1] == plantType) {
                queue.add(new int[] { r, c + 1 });
                visited[r][c + 1] = true;
            }

            // Calculate perimeter contribution
            region.perimeter += 4;
            if (r > 0 && grid[r - 1][c] == plantType) {
                region.perimeter -= 2;
            }
            if (c > 0 && grid[r][c - 1] == plantType) {
                region.perimeter -= 2;
            }
        }

        return region;
    }

    private static class Region {
        int area = 0;
        int perimeter = 0;
    }
}
