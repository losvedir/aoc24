package day16part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Sonnet {
    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';
    private static final int TURN_COST = 1000;
    private static final int MOVE_COST = 1;

    private static final int[][] DIRECTIONS = {
            { 0, 1 }, // EAST
            { 1, 0 }, // SOUTH
            { 0, -1 }, // WEST
            { -1, 0 } // NORTH
    };

    private static class State {
        final int row;
        final int col;
        final int direction;
        final int cost;
        final Set<Position> path;

        State(int row, int col, int direction, int cost, Set<Position> path) {
            this.row = row;
            this.col = col;
            this.direction = direction;
            this.cost = cost;
            this.path = path;
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

    private static class Position {
        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Position))
                return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
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

            return findOptimalPathTiles(maze, startRow, startCol);
        } catch (Exception e) {
            throw new RuntimeException("Error reading input file", e);
        }
    }

    private static int findOptimalPathTiles(char[][] maze, int startRow, int startCol) {
        PriorityQueue<State> pq = new PriorityQueue<>(
                Comparator.comparingInt(state -> state.cost));

        // Track minimum costs for each position and direction
        Map<State, Integer> minCosts = new HashMap<>();

        // Track all paths that reach the end with minimum cost
        Set<Set<Position>> optimalPaths = new HashSet<>();
        int minEndCost = Integer.MAX_VALUE;

        // Initialize start state with its position in the path
        Set<Position> initialPath = new HashSet<>();
        initialPath.add(new Position(startRow, startCol));
        State initial = new State(startRow, startCol, 0, 0, initialPath);
        pq.offer(initial);

        while (!pq.isEmpty()) {
            State current = pq.poll();

            // Skip if we've seen a better path to this state
            Integer prevCost = minCosts.get(current);
            if (prevCost != null && prevCost < current.cost) {
                continue;
            }

            // If we reached the end
            if (maze[current.row][current.col] == END) {
                if (current.cost < minEndCost) {
                    minEndCost = current.cost;
                    optimalPaths.clear();
                    optimalPaths.add(current.path);
                } else if (current.cost == minEndCost) {
                    optimalPaths.add(current.path);
                }
                continue;
            }

            // Try moving forward
            int newRow = current.row + DIRECTIONS[current.direction][0];
            int newCol = current.col + DIRECTIONS[current.direction][1];

            if (isValidMove(maze, newRow, newCol)) {
                Set<Position> newPath = new HashSet<>(current.path);
                newPath.add(new Position(newRow, newCol));
                State newState = new State(newRow, newCol, current.direction,
                        current.cost + MOVE_COST, newPath);

                if (!minCosts.containsKey(newState) ||
                        minCosts.get(newState) > newState.cost) {
                    minCosts.put(newState, newState.cost);
                    pq.offer(newState);
                }
            }

            // Try turning left and right
            for (int turn : new int[] { 3, 1 }) { // -1 (left) and +1 (right)
                int newDirection = (current.direction + turn) % 4;
                State newState = new State(current.row, current.col, newDirection,
                        current.cost + TURN_COST, current.path);

                if (!minCosts.containsKey(newState) ||
                        minCosts.get(newState) > newState.cost) {
                    minCosts.put(newState, newState.cost);
                    pq.offer(newState);
                }
            }
        }

        // Combine all positions from optimal paths
        Set<Position> allOptimalTiles = new HashSet<>();
        for (Set<Position> path : optimalPaths) {
            allOptimalTiles.addAll(path);
        }

        return allOptimalTiles.size();
    }

    private static boolean isValidMove(char[][] maze, int row, int col) {
        return row >= 0 && row < maze.length &&
                col >= 0 && col < maze[0].length &&
                maze[row][col] != WALL;
    }

    public static void main(String[] args) {
        int result = solve("input/day16.txt");
        System.out.println("Number of tiles in optimal paths: " + result);
    }
}
