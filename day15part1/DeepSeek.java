package day15part1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeepSeek {
    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }

    private char[][] grid;
    private int robotX, robotY;
    private Set<Point> boxes = new HashSet<>();
    private int width, height;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day15.txt"), StandardCharsets.UTF_8);
        DeepSeek solver = new DeepSeek();
        solver.parseInput(lines);
        int sum = solver.solve();
        System.out.println(sum);
    }

    private void parseInput(List<String> lines) {
        List<String> gridLines = new ArrayList<>();
        List<String> moveLines = new ArrayList<>();
        boolean inGrid = true;

        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            if (inGrid) {
                if (isMoveLine(line)) {
                    inGrid = false;
                    moveLines.add(line);
                } else {
                    gridLines.add(line);
                }
            } else {
                moveLines.add(line);
            }
        }

        // Parse grid
        height = gridLines.size();
        width = gridLines.isEmpty() ? 0 : gridLines.get(0).length();
        grid = new char[height][width];

        for (int y = 0; y < height; y++) {
            String line = gridLines.get(y);
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                if (c == '@') {
                    robotX = x;
                    robotY = y;
                    grid[y][x] = '.';
                } else if (c == 'O') {
                    boxes.add(new Point(x, y));
                    grid[y][x] = '.';
                } else {
                    grid[y][x] = c;
                }
            }
        }

        // Parse moves
        StringBuilder movesBuilder = new StringBuilder();
        for (String line : moveLines) {
            for (char c : line.toCharArray()) {
                if (c == '^' || c == 'v' || c == '<' || c == '>') {
                    movesBuilder.append(c);
                }
            }
        }
        String moves = movesBuilder.toString();

        // Process all moves
        for (char move : moves.toCharArray()) {
            processMove(move);
        }
    }

    private boolean isMoveLine(String line) {
        for (char c : line.toCharArray()) {
            if (c != '^' && c != 'v' && c != '<' && c != '>' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    private void processMove(char direction) {
        int dx = 0, dy = 0;
        switch (direction) {
            case '^':
                dy = -1;
                break;
            case 'v':
                dy = 1;
                break;
            case '<':
                dx = -1;
                break;
            case '>':
                dx = 1;
                break;
            default:
                return;
        }

        int newX = robotX + dx;
        int newY = robotY + dy;

        if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
            return;
        }
        if (grid[newY][newX] == '#') {
            return;
        }

        Point newPos = new Point(newX, newY);
        if (boxes.contains(newPos)) {
            int boxNewX = newX + dx;
            int boxNewY = newY + dy;

            if (boxNewX < 0 || boxNewX >= width || boxNewY < 0 || boxNewY >= height) {
                return;
            }
            if (grid[boxNewY][boxNewX] == '#' || boxes.contains(new Point(boxNewX, boxNewY))) {
                return;
            }

            boxes.remove(newPos);
            boxes.add(new Point(boxNewX, boxNewY));
            robotX = newX;
            robotY = newY;
        } else {
            robotX = newX;
            robotY = newY;
        }
    }

    private int solve() {
        int sum = 0;
        for (Point box : boxes) {
            sum += 100 * box.y + box.x;
        }
        return sum;
    }
}
