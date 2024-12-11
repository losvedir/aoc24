package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10Part2GeminiFlash {

    private static int rows;
    private static int cols;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day10.txt"));
        rows = lines.size();
        cols = lines.get(0).length();
        int[][] map = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = lines.get(i).charAt(j) - '0';
            }
        }

        int totalRating = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == 0) {
                    totalRating += calculateTrailheadRating(map, r, c);
                }
            }
        }

        System.out.println(totalRating);
    }

    private static int calculateTrailheadRating(int[][] map, int startRow, int startCol) {
        Set<List<int[]>> trails = new HashSet<>();
        List<int[]> currentTrail = new ArrayList<>();
        currentTrail.add(new int[] { startRow, startCol });
        findTrails(map, startRow, startCol, currentTrail, trails);
        return trails.size();
    }

    private static void findTrails(int[][] map, int r, int c, List<int[]> currentTrail, Set<List<int[]>> trails) {
        if (map[r][c] == 9) {
            trails.add(new ArrayList<>(currentTrail)); // Crucial: create a copy
            return;
        }

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
                    map[nr][nc] == map[r][c] + 1 && !containsLocation(currentTrail, nr, nc)) {
                currentTrail.add(new int[] { nr, nc });
                findTrails(map, nr, nc, currentTrail, trails);
                currentTrail.remove(currentTrail.size() - 1); // Backtrack
            }
        }
    }

    private static boolean containsLocation(List<int[]> trail, int r, int c) {
        for (int[] loc : trail) {
            if (loc[0] == r && loc[1] == c) {
                return true;
            }
        }
        return false;
    }
}
