// works

package Day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Day6Part1o1 {
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

        // Simulation
        int x = startX;
        int y = startY;
        Direction dir = startDir;
        Set<Position> visited = new HashSet<>();
        visited.add(new Position(y, x));

        while (true) {
            // Check what is in front
            int nx = x + dir.dx;
            int ny = y + dir.dy;

            if (!inBounds(ny, nx, rows, cols)) {
                // The guard will step out of the area
                break;
            }

            if (map[ny][nx] == '#') {
                // Turn right
                dir = dir.turnRight();
            } else {
                // Move forward
                x = nx;
                y = ny;
                visited.add(new Position(y, x));
            }
        }

        System.out.println(visited.size());
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
}
