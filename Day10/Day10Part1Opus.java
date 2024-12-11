package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day10Part1Opus {
    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("input/day10.txt")));
        int[][] map = parseMap(input);
        int sum = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0) {
                    sum += countReachableNines(map, i, j);
                }
            }
        }
        System.out.println("Sum of the scores of all trailheads: " + sum);
    }

    private static int[][] parseMap(String input) {
        String[] lines = input.split("\\n");
        int[][] map = new int[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                map[i][j] = Character.getNumericValue(lines[i].charAt(j));
            }
        }
        return map;
    }

    private static int countReachableNines(int[][] map, int i, int j) {
        int count = 0;
        boolean[][] visited = new boolean[map.length][map[0].length];
        dfs(map, i, j, visited, 0, count);
        return count;
    }

    private static void dfs(int[][] map, int i, int j, boolean[][] visited, int height, int count) {
        if (i < 0 || i >= map.length || j < 0 || j >= map[0].length || visited[i][j] || map[i][j] != height) {
            return;
        }
        visited[i][j] = true;
        if (map[i][j] == 9) {
            count++;
            return;
        }
        dfs(map, i - 1, j, visited, height + 1, count);
        dfs(map, i + 1, j, visited, height + 1, count);
        dfs(map, i, j - 1, visited, height + 1, count);
        dfs(map, i, j + 1, visited, height + 1, count);
    }
}
