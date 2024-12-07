// // infinite list

// package Day6;

// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

// public class Day6Part1Sonnet {
//     private static final char OBSTACLE = '#';
//     private static final char UP = '^';
//     private static final char RIGHT = '>';
//     private static final char DOWN = 'v';
//     private static final char LEFT = '<';

//     private record Position(int row, int col) {
//     }

//     private record State(Position pos, char direction) {
//     }

//     public static void main(String[] args) throws Exception {
//         String input = Files.readString(Paths.get("input/day6.txt"));
//         System.out.println("Result: " + solve(input));
//     }

//     public static int solve(String input) {
//         char[][] grid = input.lines()
//                 .map(String::toCharArray)
//                 .toArray(char[][]::new);

//         State initialState = findGuard(grid);
//         return simulateGuardPath(grid, initialState);
//     }

//     private static State findGuard(char[][] grid) {
//         for (int row = 0; row < grid.length; row++) {
//             for (int col = 0; col < grid[row].length; col++) {
//                 char cell = grid[row][col];
//                 if (cell == UP || cell == RIGHT || cell == DOWN || cell == LEFT) {
//                     return new State(new Position(row, col), cell);
//                 }
//             }
//         }
//         throw new IllegalStateException("No guard found in input");
//     }

//     private static int simulateGuardPath(char[][] grid, State initial) {
//         Set<Position> visited = new HashSet<>();
//         State current = initial;
//         visited.add(current.pos());

//         while (isInBounds(grid, current.pos())) {
//             current = moveGuard(grid, current);
//             visited.add(current.pos());
//         }

//         return visited.size();
//     }

//     private static State moveGuard(char[][] grid, State current) {
//         Position nextPos = getNextPosition(current);

//         // If next position is blocked or out of bounds, turn right
//         if (!isInBounds(grid, nextPos) || grid[nextPos.row()][nextPos.col()] == OBSTACLE) {
//             return new State(current.pos(), turnRight(current.direction()));
//         }

//         // Otherwise, move forward
//         return new State(nextPos, current.direction());
//     }

//     private static Position getNextPosition(State state) {
//         return switch (state.direction()) {
//             case UP -> new Position(state.pos().row() - 1, state.pos().col());
//             case RIGHT -> new Position(state.pos().row(), state.pos().col() + 1);
//             case DOWN -> new Position(state.pos().row() + 1, state.pos().col());
//             case LEFT -> new Position(state.pos().row(), state.pos().col() - 1);
//             default -> throw new IllegalStateException("Invalid direction: " + state.direction());
//         };
//     }

//     private static char turnRight(char direction) {
//         return switch (direction) {
//             case UP -> RIGHT;
//             case RIGHT -> DOWN;
//             case DOWN -> LEFT;
//             case LEFT -> UP;
//             default -> throw new IllegalStateException("Invalid direction: " + direction);
//         };
//     }

//     private static boolean isInBounds(char[][] grid, Position pos) {
//         return pos.row() >= 0 && pos.row() < grid.length &&
//                 pos.col() >= 0 && pos.col() < grid[0].length;
//     }
// }

// tell claude: this has an infinite loop, can you double check the logic?

// it says "i see the problem. when we hit an obstalce..."
// but the new code has an infinite loop still.

package Day6;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6Part1Sonnet {
    private static final char OBSTACLE = '#';
    private static final char UP = '^';
    private static final char RIGHT = '>';
    private static final char DOWN = 'v';
    private static final char LEFT = '<';

    private record Position(int row, int col) {
    }

    private record State(Position pos, char direction) {
    }

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Paths.get("input/day6.txt"));
        System.out.println("Result: " + solve(input));
    }

    public static int solve(String input) {
        char[][] grid = input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        State initialState = findGuard(grid);
        return simulateGuardPath(grid, initialState);
    }

    private static State findGuard(char[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                char cell = grid[row][col];
                if (cell == UP || cell == RIGHT || cell == DOWN || cell == LEFT) {
                    return new State(new Position(row, col), cell);
                }
            }
        }
        throw new IllegalStateException("No guard found in input");
    }

    private static int simulateGuardPath(char[][] grid, State initial) {
        Set<Position> visited = new HashSet<>();
        State current = initial;
        visited.add(current.pos());

        while (isInBounds(grid, current.pos())) {
            Position nextPos = getNextPosition(current);

            // If next position is blocked or out of bounds
            if (!isInBounds(grid, nextPos) || grid[nextPos.row()][nextPos.col()] == OBSTACLE) {
                // Turn right and try to move in the new direction
                char newDirection = turnRight(current.direction());
                Position newNextPos = getNextPosition(new State(current.pos(), newDirection));

                // If the new direction is also blocked, we'll just turn right again in the next
                // iteration
                if (isInBounds(grid, newNextPos) && grid[newNextPos.row()][newNextPos.col()] != OBSTACLE) {
                    current = new State(newNextPos, newDirection);
                } else {
                    current = new State(current.pos(), newDirection);
                }
            } else {
                // Move forward
                current = new State(nextPos, current.direction());
            }

            visited.add(current.pos());
        }

        return visited.size() - 1; // Subtract 1 because we shouldn't count the position where guard exits the map
    }

    private static Position getNextPosition(State state) {
        return switch (state.direction()) {
            case UP -> new Position(state.pos().row() - 1, state.pos().col());
            case RIGHT -> new Position(state.pos().row(), state.pos().col() + 1);
            case DOWN -> new Position(state.pos().row() + 1, state.pos().col());
            case LEFT -> new Position(state.pos().row(), state.pos().col() - 1);
            default -> throw new IllegalStateException("Invalid direction: " + state.direction());
        };
    }

    private static char turnRight(char direction) {
        return switch (direction) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
            default -> throw new IllegalStateException("Invalid direction: " + direction);
        };
    }

    private static boolean isInBounds(char[][] grid, Position pos) {
        return pos.row() >= 0 && pos.row() < grid.length &&
                pos.col() >= 0 && pos.col() < grid[0].length;
    }
}
