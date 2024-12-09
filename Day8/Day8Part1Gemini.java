package Day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8Part1Gemini {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day8.txt"));
        char[][] grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);
        int height = grid.length;
        int width = grid[0].length;

        Set<Point> antinodes = new HashSet<>();

        // Iterate over each antenna
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char frequency = grid[y][x];

                // Check for antinodes in all four directions
                antinodes.addAll(findAntinodes(grid, x, y, frequency, 1, 0)); // Right
                antinodes.addAll(findAntinodes(grid, x, y, frequency, -1, 0)); // Left
                antinodes.addAll(findAntinodes(grid, x, y, frequency, 0, 1)); // Down
                antinodes.addAll(findAntinodes(grid, x, y, frequency, 0, -1)); // Up
            }
        }

        System.out.println("The number of unique antinodes is: " + antinodes.size());
    }

    // Helper function to find antinodes in a specific direction
    private static Set<Point> findAntinodes(char[][] grid, int x, int y, char frequency, int dx, int dy) {
        Set<Point> antinodes = new HashSet<>();
        int distance = 1;

        while (true) {
            int x1 = x + dx * distance;
            int x2 = x + dx * distance * 2;
            int y1 = y + dy * distance;
            int y2 = y + dy * distance * 2;

            // Check if any point is out of bounds
            if (x1 < 0 || x1 >= grid[0].length || x2 < 0 || x2 >= grid[0].length ||
                    y1 < 0 || y1 >= grid.length || y2 < 0 || y2 >= grid.length) {
                break;
            }

            // Check if the antennas have the same frequency
            if (grid[y1][x1] == frequency && grid[y2][x2] == frequency) {
                antinodes.add(new Point(x + dx * distance / 2, y + dy * distance / 2));
            }

            distance++;
        }

        return antinodes;
    }

    // Helper record to represent a point in the grid
    private record Point(int x, int y) {
    }
}
