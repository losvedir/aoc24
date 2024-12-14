package day12part2;

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
                    totalPrice += region.area * region.sides;
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
        }

        // Calculate sides after exploring the whole region
        region.sides = calculateSides(grid, plantType, visited);

        return region;
    }

    private int calculateSides(char[][] grid, char plantType, boolean[][] visited) {
        int rows = grid.length;
        int cols = grid[0].length;
        int sides = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (visited[i][j] && grid[i][j] == plantType) {
                    if (i == 0 || !visited[i - 1][j] || grid[i - 1][j] != plantType) {
                        sides++;
                    }
                    if (i == rows - 1 || !visited[i + 1][j] || grid[i + 1][j] != plantType) {
                        sides++;
                    }
                    if (j == 0 || !visited[i][j - 1] || grid[i][j - 1] != plantType) {
                        sides++;
                    }
                    if (j == cols - 1 || !visited[i][j + 1] || grid[i][j + 1] != plantType) {
                        sides++;
                    }
                }
            }
        }

        return sides;
    }

    private static class Region {
        int area = 0;
        int sides = 0;
    }
}
