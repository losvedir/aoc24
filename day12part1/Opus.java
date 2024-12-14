package day12part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

public class Opus {
    public static void main(String[] args) throws IOException {
        char[][] map = readMap("input/day12.txt");
        int totalPrice = calculateTotalPrice(map);
        System.out.println("Total price: " + totalPrice);
    }

    private static char[][] readMap(String filename) throws IOException {
        return Files.lines(Path.of(filename))
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private static int calculateTotalPrice(char[][] map) {
        int totalPrice = 0;
        boolean[][] visited = new boolean[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (!visited[i][j]) {
                    int[] regionInfo = exploreRegion(map, visited, i, j);
                    int area = regionInfo[0];
                    int perimeter = regionInfo[1];
                    totalPrice += area * perimeter;
                }
            }
        }

        return totalPrice;
    }

    private static int[] exploreRegion(char[][] map, boolean[][] visited, int i, int j) {
        int area = 0;
        int perimeter = 0;
        char plant = map[i][j];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] { i, j });
        visited[i][j] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            area++;

            for (int[] neighbor : getNeighbors(map, row, col)) {
                int neighborRow = neighbor[0];
                int neighborCol = neighbor[1];

                if (map[neighborRow][neighborCol] == plant) {
                    if (!visited[neighborRow][neighborCol]) {
                        visited[neighborRow][neighborCol] = true;
                        queue.offer(neighbor);
                    }
                } else {
                    perimeter++;
                }
            }
        }

        return new int[] { area, perimeter };
    }

    private static int[][] getNeighbors(char[][] map, int row, int col) {
        int[][] neighbors = new int[4][2];
        int count = 0;

        if (row > 0)
            neighbors[count++] = new int[] { row - 1, col };
        if (row < map.length - 1)
            neighbors[count++] = new int[] { row + 1, col };
        if (col > 0)
            neighbors[count++] = new int[] { row, col - 1 };
        if (col < map[0].length - 1)
            neighbors[count++] = new int[] { row, col + 1 };

        return java.util.Arrays.copyOf(neighbors, count);
    }
}
