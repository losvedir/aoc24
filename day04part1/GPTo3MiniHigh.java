package day04part1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

public class GPTo3MiniHigh {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day4.txt"));
            if (lines.isEmpty()) {
                System.out.println(0);
                return;
            }
            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] grid = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                grid[i] = lines.get(i).toCharArray();
            }

            String word = "XMAS";
            int count = 0;
            // Define 8 possible directions:
            // (-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1)
            int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
            int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    for (int d = 0; d < 8; d++) {
                        int j;
                        for (j = 0; j < word.length(); j++) {
                            int nr = r + dx[d] * j;
                            int nc = c + dy[d] * j;
                            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != word.charAt(j)) {
                                break;
                            }
                        }
                        if (j == word.length()) {
                            count++;
                        }
                    }
                }
            }

            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
