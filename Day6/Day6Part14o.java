// infinite loop

package Day6;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Day6Part14o {
    private static final char GUARD = '^';
    private static final char OBSTACLE = '#';
    private static final char EMPTY = '.';

    // Directions in the order of up, right, down, left
    private static final int[][] DIRECTIONS = {
            { -1, 0 }, // Up
            { 0, 1 }, // Right
            { 1, 0 }, // Down
            { 0, -1 } // Left
    };

    private static final Map<Character, Integer> DIRECTION_MAP = Map.of(
            '^', 0, // Up
            '>', 1, // Right
            'v', 2, // Down
            '<', 3 // Left
    );

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day6.txt"));
            System.out.println("Distinct positions visited: " + calculateVisitedPositions(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateVisitedPositions(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();

        boolean[][] visited = new boolean[rows][cols];

        // Find the guard's starting position and direction
        int startX = -1, startY = -1, direction = -1;
        outerLoop: for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char ch = lines.get(i).charAt(j);
                if (DIRECTION_MAP.containsKey(ch)) {
                    startX = i;
                    startY = j;
                    direction = DIRECTION_MAP.get(ch);
                    break outerLoop;
                }
            }
        }

        if (startX == -1 || startY == -1) {
            throw new IllegalStateException("Guard not found on the map.");
        }

        int x = startX;
        int y = startY;
        visited[x][y] = true;

        while (isInBounds(x, y, rows, cols)) {
            int nextX = x + DIRECTIONS[direction][0];
            int nextY = y + DIRECTIONS[direction][1];

            if (isInBounds(nextX, nextY, rows, cols) && lines.get(nextX).charAt(nextY) != OBSTACLE) {
                x = nextX;
                y = nextY;
                visited[x][y] = true;
            } else {
                // Turn right 90 degrees
                direction = (direction + 1) % 4;
            }
        }

        return countVisitedPositions(visited);
    }

    private static boolean isInBounds(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private static int countVisitedPositions(boolean[][] visited) {
        int count = 0;
        for (boolean[] row : visited) {
            for (boolean cell : row) {
                if (cell)
                    count++;
            }
        }
        return count;
    }
}
