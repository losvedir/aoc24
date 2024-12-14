package day12part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GPTo1Pro {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("input/day12.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            var line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                grid[r][c] = line.charAt(c);
            }
        }

        boolean[][] visited = new boolean[rows][cols];
        long totalPrice = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!visited[r][c]) {
                    totalPrice += processRegion(grid, visited, r, c);
                }
            }
        }

        System.out.println(totalPrice);
    }

    private record Cell(int r, int c) {
    }

    private static long processRegion(char[][] grid, boolean[][] visited, int startR, int startC) {
        var rows = grid.length;
        var cols = grid[0].length;
        var regionChar = grid[startR][startC];
        var stack = new ArrayDeque<Cell>();
        stack.push(new Cell(startR, startC));

        var regionCells = new ArrayList<Cell>();
        visited[startR][startC] = true;

        while (!stack.isEmpty()) {
            var cell = stack.pop();
            regionCells.add(cell);
            for (var dir : DIRECTIONS) {
                int nr = cell.r() + dir[0];
                int nc = cell.c() + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                    continue;
                if (!visited[nr][nc] && grid[nr][nc] == regionChar) {
                    visited[nr][nc] = true;
                    stack.push(new Cell(nr, nc));
                }
            }
        }

        int area = regionCells.size();
        int perimeter = 0;

        // Compute perimeter
        // For each cell in the region, check its 4 neighbors.
        // If neighbor is out of bounds or different char, that side contributes to
        // perimeter.
        for (var cell : regionCells) {
            for (var dir : DIRECTIONS) {
                int nr = cell.r() + dir[0];
                int nc = cell.c() + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != regionChar) {
                    perimeter++;
                }
            }
        }

        return (long) area * perimeter;
    }

    private static final int[][] DIRECTIONS = {
            { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }
    };
}
