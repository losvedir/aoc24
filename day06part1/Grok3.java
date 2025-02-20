package day06part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grok3 {

    /** Record to represent a position in the grid */
    record Position(int row, int col) {
    }

    /** Enum to manage guard's direction with movement deltas and right turns */
    enum Direction {
        UP(-1, 0), // Up: decrease row
        RIGHT(0, 1), // Right: increase column
        DOWN(1, 0), // Down: increase row
        LEFT(0, -1); // Left: decrease column

        public final int dr; // Row change
        public final int dc; // Column change

        Direction(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
        }

        /** Returns the direction after a 90-degree right turn */
        public Direction turnRight() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }

        /** Parses direction from the input character */
        public static Direction fromChar(char c) {
            return switch (c) {
                case '^' -> UP;
                case '>' -> RIGHT;
                case 'v' -> DOWN;
                case '<' -> LEFT;
                default -> throw new IllegalArgumentException("Invalid direction: " + c);
            };
        }
    }

    public static void main(String[] args) throws IOException {
        // Read the map from the input file
        List<String> map = Files.readAllLines(Path.of("input/day6.txt"));
        int R = map.size(); // Number of rows
        int C = map.get(0).length(); // Number of columns (assumes rectangular grid)

        // Find the guard's starting position and direction
        int startRow = -1;
        int startCol = -1;
        Direction direction = null;
        for (int row = 0; row < R; row++) {
            String line = map.get(row);
            for (int col = 0; col < C; col++) {
                char c = line.charAt(col);
                if (c == '^' || c == '>' || c == 'v' || c == '<') {
                    startRow = row;
                    startCol = col;
                    direction = Direction.fromChar(c);
                    break;
                }
            }
            if (startRow != -1)
                break; // Exit outer loop once found
        }

        // Replace the starting position with '.' to treat it as an empty space
        String rowStr = map.get(startRow);
        rowStr = rowStr.substring(0, startCol) + '.' + rowStr.substring(startCol + 1);
        map.set(startRow, rowStr);

        // Track distinct positions visited
        Set<Position> visited = new HashSet<>();
        Position current = new Position(startRow, startCol);
        visited.add(current); // Include the starting position

        // Simulate the guard's movement
        while (true) {
            // Calculate the next position
            int nextRow = current.row + direction.dr;
            int nextCol = current.col + direction.dc;

            // Check if the next position is outside the map
            if (nextRow < 0 || nextRow >= R || nextCol < 0 || nextCol >= C) {
                break; // Guard leaves the mapped area
            }

            // Check for an obstacle and move or turn accordingly
            if (map.get(nextRow).charAt(nextCol) == '#') {
                direction = direction.turnRight();
            } else {
                current = new Position(nextRow, nextCol);
                visited.add(current);
            }
        }

        // Output the number of distinct positions visited
        System.out.println(visited.size());
    }
}
