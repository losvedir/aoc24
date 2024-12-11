package Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day10Part1GeminiFlash {

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

        int totalScore = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == 0) {
                    totalScore += calculateTrailheadScore(map, r, c);
                }
            }
        }

        System.out.println(totalScore);
    }

    private static int calculateTrailheadScore(int[][] map, int startRow, int startCol) {
        int score = 0;
        boolean[][] visited = new boolean[rows][cols];
        List<int[]> queue = new ArrayList<>();
        queue.add(new int[] { startRow, startCol });
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            List<int[]> nextQueue = new ArrayList<>();
            for (int[] current : queue) {
                int r = current[0];
                int c = current[1];

                if (map[r][c] == 9) {
                    score++;
                }

                int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
                for (int[] dir : directions) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];

                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
                            !visited[nr][nc] && map[nr][nc] == map[r][c] + 1) {
                        nextQueue.add(new int[] { nr, nc });
                        visited[nr][nc] = true;
                    }
                }
            }
            queue = nextQueue;
        }
        return score;
    }
}
