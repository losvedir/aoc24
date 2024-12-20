package day16part1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Sonnet {
    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';
    private static final int TURN_COST = 1000;
    private static final int MOVE_COST = 1;

    // Represents directions: EAST(0), SOUTH(1), WEST(2), NORTH(3)
    private static final int[][] DIRECTIONS = {
            { 0, 1 }, // EAST
            { 1, 0 }, // SOUTH
            { 0, -1 }, // WEST
            { -1, 0 } // NORTH
    };

    private static class State {
        final int row;
        final int col;
        final int direction; // 0=EAST, 1=SOUTH, 2=WEST, 3=NORTH
        final int cost;

        State(int row, int col, int direction, int cost) {
            this.row = row;
            this.col = col;
            this.direction = direction;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof State))
                return false;
            State state = (State) o;
            return row == state.row &&
                    col == state.col &&
                    direction == state.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, direction);
        }
    }

    public static int solve(String inputPath) {
        try {
            char[][] maze = Files.lines(Path.of(inputPath))
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            // Find start position
            int startRow = -1, startCol = -1;
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if (maze[i][j] == START) {
                        startRow = i;
                        startCol = j;
                        break;
                    }
                }
                if (startRow != -1)
                    break;
            }

            return findShortestPath(maze, startRow, startCol);
        } catch (Exception e) {
            throw new RuntimeException("Error reading input file", e);
        }
    }

    private static int findShortestPath(char[][] maze, int startRow, int startCol) {
        PriorityQueue<State> pq = new PriorityQueue<>(
                Comparator.comparingInt(state -> state.cost));
        Set<State> visited = new HashSet<>();

        // Start facing EAST (direction 0)
        State initial = new State(startRow, startCol, 0, 0);
        pq.offer(initial);

        while (!pq.isEmpty()) {
            State current = pq.poll();

            if (maze[current.row][current.col] == END) {
                return current.cost;
            }

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            // Try moving forward
            int newRow = current.row + DIRECTIONS[current.direction][0];
            int newCol = current.col + DIRECTIONS[current.direction][1];

            if (isValidMove(maze, newRow, newCol)) {
                pq.offer(new State(newRow, newCol, current.direction,
                        current.cost + MOVE_COST));
            }

            // Try turning left (counterclockwise)
            int leftDirection = (current.direction + 3) % 4;
            pq.offer(new State(current.row, current.col, leftDirection,
                    current.cost + TURN_COST));

            // Try turning right (clockwise)
            int rightDirection = (current.direction + 1) % 4;
            pq.offer(new State(current.row, current.col, rightDirection,
                    current.cost + TURN_COST));
        }

        return -1; // No path found
    }

    private static boolean isValidMove(char[][] maze, int row, int col) {
        return row >= 0 && row < maze.length &&
                col >= 0 && col < maze[0].length &&
                maze[row][col] != WALL;
    }

    public static void main(String[] args) {
        int result = solve("input/day16.txt");
        System.out.println("Lowest possible score: " + result);
    }
}
