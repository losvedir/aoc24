// got it.

package Day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6Part1Opus {
    private static final int[][] DIRECTIONS = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } }; // Up, Right, Down, Left

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day6.txt"));
        char[][] map = lines.stream().map(String::toCharArray).toArray(char[][]::new);

        int guardRow = -1, guardCol = -1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '^') {
                    guardRow = i;
                    guardCol = j;
                    break;
                }
            }
            if (guardRow != -1)
                break;
        }

        int direction = 0; // Start facing up
        Set<String> visited = new HashSet<>();
        visited.add(guardRow + "," + guardCol);

        while (true) {
            int newRow = guardRow + DIRECTIONS[direction][0];
            int newCol = guardCol + DIRECTIONS[direction][1];

            if (newRow < 0 || newRow >= map.length || newCol < 0 || newCol >= map[0].length) {
                break; // Guard left the mapped area
            }

            if (map[newRow][newCol] == '#') {
                direction = (direction + 1) % 4; // Turn right
            } else {
                guardRow = newRow;
                guardCol = newCol;
                visited.add(guardRow + "," + guardCol);
            }
        }

        System.out.println("Number of distinct positions visited: " + visited.size());
    }
}
