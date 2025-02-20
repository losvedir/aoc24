package day04part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class R1 {

    public static void main(String[] args) {
        try {
            List<String> grid = readInput("input/day4.txt");
            int count = countXMAS(grid);
            System.out.println("Number of X-MAS occurrences: " + count);
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
        if (rows == 0)
            return 0;
        int cols = grid.get(0).length();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean validFirst = checkFirstDiagonal(grid, i, j, rows, cols);
                boolean validSecond = checkSecondDiagonal(grid, i, j, rows, cols);
                if (validFirst && validSecond) {
                    count++;
                }
            }
        }

        return count;
    }

    private static boolean checkFirstDiagonal(List<String> grid, int i, int j, int rows, int cols) {
        if (i - 1 < 0 || j - 1 < 0 || i + 1 >= rows || j + 1 >= cols) {
            return false;
        }
        char c1 = grid.get(i - 1).charAt(j - 1);
        char c2 = grid.get(i).charAt(j);
        char c3 = grid.get(i + 1).charAt(j + 1);
        String s = String.valueOf(new char[] { c1, c2, c3 });
        return s.equals("MAS") || s.equals("SAM");
    }

    private static boolean checkSecondDiagonal(List<String> grid, int i, int j, int rows, int cols) {
        if (i - 1 < 0 || j + 1 >= cols || i + 1 >= rows || j - 1 < 0) {
            return false;
        }
        char c1 = grid.get(i - 1).charAt(j + 1);
        char c2 = grid.get(i).charAt(j);
        char c3 = grid.get(i + 1).charAt(j - 1);
        String s = String.valueOf(new char[] { c1, c2, c3 });
        return s.equals("MAS") || s.equals("SAM");
    }
}
