package day06part1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sonnet {
    private static record Position(int row, int col) {
    }

    private static record State(Position pos, Direction dir) {
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

    private final char[][] map;
    private final int rows;
    private final int cols;
    private State initialState;

    public Sonnet(List<String> input) {
        rows = input.size();
        cols = input.get(0).length();
        map = new char[rows][cols];

        for (int row = 0; row < rows; row++) {
            String line = input.get(row);
            for (int col = 0; col < cols; col++) {
                char c = line.charAt(col);
                if (c == '^') {
                    initialState = new State(new Position(row, col), Direction.UP);
                    map[row][col] = '.';
                } else {
                    map[row][col] = c;
                }
            }
        }
    }

    private boolean isInBounds(Position pos) {
        return pos.row() >= 0 && pos.row() < rows &&
                pos.col() >= 0 && pos.col() < cols;
    }

    private boolean isBlocked(Position pos) {
        return !isInBounds(pos) || map[pos.row()][pos.col()] == '#';
    }

    private Position move(Position pos, Direction dir) {
        return new Position(pos.row() + dir.dy, pos.col() + dir.dx);
    }

    public int solve() {
        Set<Position> visited = new HashSet<>();
        State current = initialState;

        while (isInBounds(current.pos())) {
            visited.add(current.pos());

            // Check position in front
            Position ahead = move(current.pos(), current.dir());

            if (isBlocked(ahead)) {
                // Turn right if blocked
                current = new State(current.pos(), current.dir().turnRight());
            } else {
                // Move forward if not blocked
                current = new State(ahead, current.dir());
            }
        }

        return visited.size();
    }

    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Path.of("input/day6.txt"));
        Sonnet solution = new Sonnet(input);
        System.out.println(solution.solve());
    }
}
