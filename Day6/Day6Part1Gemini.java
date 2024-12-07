// infinite loop

package Day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6Part1Gemini {

    private static final String INPUT_FILE = "input/day6.txt";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
        char[][] grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);

        int guardRow = 0, guardCol = 0, guardDir = 0; // 0: up, 1: right, 2: down, 3: left
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '^') {
                    guardRow = i;
                    guardCol = j;
                    break;
                }
            }
        }

        Set<String> visited = new HashSet<>();
        visited.add(guardRow + "," + guardCol);

        while (true) {
            // Determine next position
            int nextRow = guardRow, nextCol = guardCol;
            switch (guardDir) {
                case 0:
                    nextRow--;
                    break; // Up
                case 1:
                    nextCol++;
                    break; // Right
                case 2:
                    nextRow++;
                    break; // Down
                case 3:
                    nextCol--;
                    break; // Left
            }

            // Check if next position is valid and not an obstacle
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length
                    && grid[nextRow][nextCol] != '#') {
                guardRow = nextRow;
                guardCol = nextCol;
                visited.add(guardRow + "," + guardCol);
            } else {
                guardDir = (guardDir + 1) % 4; // Turn right
            }

            // Check if guard has left the mapped area
            if (guardRow < 0 || guardRow >= grid.length || guardCol < 0 || guardCol >= grid[0].length) {
                break;
            }
        }

        System.out.println(visited.size());
    }
}
