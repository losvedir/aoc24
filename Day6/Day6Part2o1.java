// works

package Day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day6Part2o1 {
    public static void main(String[] args) throws IOException {
        // Read the input lines from the file
        var lines = Files.readAllLines(Path.of("input/day6.txt"));

        int rows = lines.size();
        int cols = lines.get(0).length();

        // Parse the map
        char[][] map = new char[rows][cols];
        int startX = -1, startY = -1; // Guard start position
        Direction startDir = Direction.UP; // Guard start direction, will override when found
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                map[r][c] = ch;
                if (ch == '^' || ch == '>' || ch == 'v' || ch == '<') {
                    startX = c;
                    startY = r;
                    switch (ch) {
                        case '^' -> startDir = Direction.UP;
                        case '>' -> startDir = Direction.RIGHT;
                        case 'v' -> startDir = Direction.DOWN;
                        case '<' -> startDir = Direction.LEFT;
                    }
                }
            }
        }

        // We'll consider placing a new obstruction '#' in every '.' cell except the
        // starting cell.
        // For each candidate position, we test if it causes the guard to enter a loop.
        int loopCount = 0;

        // The guard's starting cell is at (startY, startX)
        // We cannot place the new obstruction there.
        Position startPos = new Position(startY, startX);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == startY && c == startX)
                    continue; // Can't place at guard start
                if (map[r][c] == '.') {
                    // Try placing obstruction here
                    map[r][c] = '#';
                    if (causesLoop(map, startPos, startDir, rows, cols)) {
                        loopCount++;
                    }
                    // Restore
                    map[r][c] = '.';
                }
            }
        }

        System.out.println(loopCount);
    }

    private static boolean causesLoop(char[][] map, Position startPos, Direction startDir, int rows, int cols) {
        int x = startPos.c;
        int y = startPos.r;
        Direction dir = startDir;

        // To detect loops, we track states: (y, x, dir)
        Set<State> visitedStates = new HashSet<>();
        visitedStates.add(new State(y, x, dir));

        while (true) {
            int nx = x + dir.dx;
            int ny = y + dir.dy;

            if (!inBounds(ny, nx, rows, cols)) {
                // The guard will step out of the area - no loop
                return false;
            }

            if (map[ny][nx] == '#') {
                // Turn right
                dir = dir.turnRight();
            } else {
                // Move forward
                x = nx;
                y = ny;
            }

            State s = new State(y, x, dir);
            if (visitedStates.contains(s)) {
                // We found a previously visited state - it's a loop
                return true;
            }
            visitedStates.add(s);
        }
    }

    private static boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    private enum Direction {
        UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

        final int dx;
        final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Direction turnRight() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }
    }

    private record Position(int r, int c) {
    }

    private record State(int r, int c, Direction dir) {
    }
}
