package day04part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class R1 {

    private static final String TARGET = "XMAS";
    private static final int[] dx = { 1, 1, 0, -1, -1, -1, 0, 1 };
    private static final int[] dy = { 0, 1, 1, 1, 0, -1, -1, -1 };

    public static void main(String[] args) {
        try {
            List<String> grid = readInput("input/day4.txt");
            int count = countXMAS(grid);
            System.out.println("Number of XMAS occurrences: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readInput(String filePath) throws IOException {
        List<String> grid = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                grid.add(line);
            }
        }
        return grid;
    }

    private static int countXMAS(List<String> grid) {
        int count = 0;
        int rows = grid.size();
        int cols = grid.get(0).length();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int dir = 0; dir < 8; dir++) {
                    if (searchInDirection(grid, i, j, dir)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static boolean searchInDirection(List<String> grid, int x, int y, int dir) {
        int rows = grid.size();
        int cols = grid.get(0).length();

        for (int i = 0; i < TARGET.length(); i++) {
            int nx = x + i * dx[dir];
            int ny = y + i * dy[dir];

            if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) {
                return false;
            }

            if (grid.get(nx).charAt(ny) != TARGET.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
