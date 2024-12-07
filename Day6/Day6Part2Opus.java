// // wrong answer

// the answer is too high, can you double check the logic?

// I see: just being in the same spot, the guard can still leave.

// gives me a new version of one of the methods. still wrong (but answer is 1 less)

package Day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6Part2Opus {
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

        Set<String> obstructionPositions = new HashSet<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == '.' && (row != guardRow || col != guardCol)) {
                    if (isLoopPosition(map, row, col, guardRow, guardCol)) {
                        obstructionPositions.add(row + "," + col);
                    }
                }
            }
        }

        System.out.println("Number of possible obstruction positions: " + obstructionPositions.size());
    }

    // private static boolean isLoopPosition(char[][] map, int row, int col, int
    // startRow, int startCol) {
    // char[][] mapCopy = copyMap(map);
    // mapCopy[row][col] = '#';

    // int guardRow = startRow, guardCol = startCol;
    // int direction = 0; // Start facing up
    // Set<String> visited = new HashSet<>();

    // while (true) {
    // int newRow = guardRow + DIRECTIONS[direction][0];
    // int newCol = guardCol + DIRECTIONS[direction][1];

    // if (newRow < 0 || newRow >= mapCopy.length || newCol < 0 || newCol >=
    // mapCopy[0].length) {
    // return false; // Guard left the mapped area
    // }

    // String position = guardRow + "," + guardCol;
    // if (visited.contains(position)) {
    // return true; // Guard is stuck in a loop
    // }
    // visited.add(position);

    // if (mapCopy[newRow][newCol] == '#') {
    // direction = (direction + 1) % 4; // Turn right
    // } else {
    // guardRow = newRow;
    // guardCol = newCol;
    // }
    // }
    // }

    private static boolean isLoopPosition(char[][] map, int row, int col, int startRow, int startCol) {
        char[][] mapCopy = copyMap(map);
        mapCopy[row][col] = '#';

        int guardRow = startRow, guardCol = startCol;
        int direction = 0; // Start facing up
        Set<String> visited = new HashSet<>();

        while (true) {
            int newRow = guardRow + DIRECTIONS[direction][0];
            int newCol = guardCol + DIRECTIONS[direction][1];

            if (newRow < 0 || newRow >= mapCopy.length || newCol < 0 || newCol >= mapCopy[0].length) {
                return false; // Guard left the mapped area
            }

            String position = guardRow + "," + guardCol;
            if (visited.contains(position)) {
                // Check if the guard is stuck in a loop
                int loopRow = guardRow, loopCol = guardCol;
                int loopDirection = direction;
                Set<String> loopVisited = new HashSet<>();

                while (true) {
                    int nextRow = loopRow + DIRECTIONS[loopDirection][0];
                    int nextCol = loopCol + DIRECTIONS[loopDirection][1];

                    if (nextRow < 0 || nextRow >= mapCopy.length || nextCol < 0 || nextCol >= mapCopy[0].length) {
                        return false; // Guard left the mapped area
                    }

                    String loopPosition = loopRow + "," + loopCol;
                    if (loopVisited.contains(loopPosition)) {
                        return true; // Guard is stuck in a loop
                    }
                    loopVisited.add(loopPosition);

                    if (mapCopy[nextRow][nextCol] == '#') {
                        loopDirection = (loopDirection + 1) % 4; // Turn right
                    } else {
                        loopRow = nextRow;
                        loopCol = nextCol;
                    }
                }
            }
            visited.add(position);

            if (mapCopy[newRow][newCol] == '#') {
                direction = (direction + 1) % 4; // Turn right
            } else {
                guardRow = newRow;
                guardCol = newCol;
            }
        }
    }

    private static char[][] copyMap(char[][] map) {
        char[][] copy = new char[map.length][];
        for (int i = 0; i < map.length; i++) {
            copy[i] = map[i].clone();
        }
        return copy;
    }
}
