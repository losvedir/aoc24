package day06part1;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Gemini2FlashThinking {

    public static void main(String[] args) {
        try {
            String filePath = "input/day6.txt";
            int distinctPositions = solve(filePath);
            System.out.println(distinctPositions);
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }

    public static int solve(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        if (lines.isEmpty()) {
            return 0; // Handle empty input, though problem statement suggests input will be present
        }

        char[][] grid = new char[lines.size()][lines.get(0).length()];
        int startRow = -1;
        int startCol = -1;
        int direction = -1; // 0: up, 1: right, 2: down, 3: left

        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                char c = lines.get(i).charAt(j);
                grid[i][j] = c;
                if (c == '^' || c == '>' || c == 'v' || c == '<') {
                    startRow = i;
                    startCol = j;
                    if (c == '^')
                        direction = 0;
                    else if (c == '>')
                        direction = 1;
                    else if (c == 'v')
                        direction = 2;
                    else if (c == '<')
                        direction = 3;
                    grid[i][j] = '.'; // Treat starting position as empty space for movement
                }
            }
        }

        if (startRow == -1) {
            return 0; // No starting position found, though problem statement suggests it will be
                      // present
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int currentRow = startRow;
        int currentCol = startCol;
        Set<Point> visitedPositions = new HashSet<>();
        visitedPositions.add(new Point(currentRow, currentCol));

        int[] dr = { -1, 0, 1, 0 }; // row direction changes for up, right, down, left
        int[] dc = { 0, 1, 0, -1 }; // col direction changes for up, right, down, left

        while (true) {
            int nextRow = currentRow + dr[direction];
            int nextCol = currentCol + dc[direction];

            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols || grid[nextRow][nextCol] == '#') {
                direction = (direction + 1) % 4; // Turn right
            } else {
                currentRow = nextRow;
                currentCol = nextCol;
                if (!visitedPositions.add(new Point(currentRow, currentCol))) {
                    // Optional: Detect cycles if needed, but for this problem, just count distinct
                    // positions.
                }
            }
            if (currentRow < 0 || currentRow >= rows || currentCol < 0 || currentCol >= cols) {
                break; // Guard left the area
            }
        }

        return visitedPositions.size();
    }
}
