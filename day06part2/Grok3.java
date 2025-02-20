package day06part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grok3 {

    /** Record to represent a position in the grid */
    record Position(int row, int col) {
    }

    /**
     * Record to represent the guard's state (position + direction) for loop
     * detection
     */
    record State(Position pos, Direction dir) {
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
        List<String> map = new ArrayList<>(Files.readAllLines(Path.of("input/day6.txt")));
        int R = map.size(); // Number of rows
        int C = map.get(0).length(); // Number of columns (assumes rectangular grid)

        // Find the guard's starting position and direction
        Position startPos = null;
        Direction startDir = null;
        for (int row = 0; row < R; row++) {
            String line = map.get(row);
            for (int col = 0; col < C; col++) {
                char c = line.charAt(col);
                if (c == '^' || c == '>' || c == 'v' || c == '<') {
                    startPos = new Position(row, col);
                    startDir = Direction.fromChar(c);
                    // Replace starting position with '.' in the map
                    map.set(row, line.substring(0, col) + '.' + line.substring(col + 1));
                    break;
                }
            }
            if (startPos != null)
                break;
        }

        // Find all candidate positions for the new obstruction (empty spaces excluding
        // start)
        List<Position> candidates = new ArrayList<>();
        for (int row = 0; row < R; row++) {
            String line = map.get(row);
            for (int col = 0; col < C; col++) {
                if (line.charAt(col) == '.' && !new Position(row, col).equals(startPos)) {
                    candidates.add(new Position(row, col));
                }
            }
        }

        // Count positions that cause the guard to get stuck in a loop
        int loopCount = 0;
        for (Position candidate : candidates) {
            // Place the new obstruction
            int obsRow = candidate.row();
            int obsCol = candidate.col();
            String origRow = map.get(obsRow);
            map.set(obsRow, origRow.substring(0, obsCol) + '#' + origRow.substring(obsCol + 1));

            // Simulate guard movement to check for a loop
            if (createsLoop(map, startPos, startDir, R, C)) {
                loopCount++;
            }

            // Restore the map by removing the obstruction
            map.set(obsRow, origRow);
        }

        // Output the number of valid positions
        System.out.println(loopCount);
    }

    /** Simulates guard movement and returns true if it enters a loop */
    private static boolean createsLoop(List<String> map, Position start, Direction startDir, int R, int C) {
        Set<State> visited = new HashSet<>();
        Position current = start;
        Direction direction = startDir;

        while (true) {
            State currentState = new State(current, direction);
            if (!visited.add(currentState)) {
                return true; // Loop detected: same position and direction revisited
            }

            // Calculate the next position
            int nextRow = current.row() + direction.dr;
            int nextCol = current.col() + direction.dc;

            // Check if the next position is outside the map
            if (nextRow < 0 || nextRow >= R || nextCol < 0 || nextCol >= C) {
                return false; // Guard exits, no loop
            }

            // Check for an obstacle and move or turn accordingly
            if (map.get(nextRow).charAt(nextCol) == '#') {
                direction = direction.turnRight();
            } else {
                current = new Position(nextRow, nextCol);
            }
        }
    }
}
