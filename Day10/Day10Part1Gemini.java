package Day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10Part1Gemini {

    private static final String INPUT_FILE = "input/day10.txt";

    public static void main(String[] args) throws IOException {
        int[][] heightMap = readInput(INPUT_FILE);
        int sum = 0;
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                if (heightMap[i][j] == 0) {
                    sum += calculateScore(heightMap, i, j);
                }
            }
        }
        System.out.println("Sum of the scores of all trailheads: " + sum);
    }

    private static int calculateScore(int[][] heightMap, int row, int col) {
        int score = 0;
        List<int[]> queue = new ArrayList<>();
        queue.add(new int[] { row, col });
        while (!queue.isEmpty()) {
            int[] current = queue.remove(0);
            int currentRow = current[0];
            int currentCol = current[1];
            if (heightMap[currentRow][currentCol] == 9) {
                score++;
            } else {
                // Explore adjacent positions with height + 1
                addAdjacentPositions(heightMap, queue, currentRow, currentCol);
            }
        }
        return score;
    }

    private static void addAdjacentPositions(int[][] heightMap, List<int[]> queue, int row, int col) {
        int currentHeight = heightMap[row][col];
        // Up
        if (row > 0 && heightMap[row - 1][col] == currentHeight + 1) {
            queue.add(new int[] { row - 1, col });
        }
        // Down
        if (row < heightMap.length - 1 && heightMap[row + 1][col] == currentHeight + 1) {
            queue.add(new int[] { row + 1, col });
        }
        // Left
        if (col > 0 && heightMap[row][col - 1] == currentHeight + 1) {
            queue.add(new int[] { row, col - 1 });
        }
        // Right
        if (col < heightMap[0].length - 1 && heightMap[row][col + 1] == currentHeight + 1) {
            queue.add(new int[] { row, col + 1 });
        }
    }

    private static int[][] readInput(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            int[][] heightMap = new int[lines.size()][lines.get(0).length()];
            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < lines.get(i).length(); j++) {
                    heightMap[i][j] = lines.get(i).charAt(j) - '0';
                }
            }
            return heightMap;
        }
    }
}
