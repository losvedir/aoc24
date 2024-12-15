package day14part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sonnet {
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int MAX_TIME = 1000; // Reasonable upper limit for search

    record Position(double x, double y) {
        Position add(Velocity v, double time) {
            return new Position(
                    x + v.dx() * time,
                    y + v.dy() * time);
        }

        Position normalize() {
            return new Position(
                    Math.floorMod((int) Math.floor(x), WIDTH),
                    Math.floorMod((int) Math.floor(y), HEIGHT));
        }
    }

    record Velocity(double dx, double dy) {
    }

    record Robot(Position position, Velocity velocity) {
        Position getPositionAfterTime(double time) {
            return position.add(velocity, time).normalize();
        }
    }

    private static Robot parseRobot(String line) {
        Pattern pattern = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
        Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid robot format: " + line);
        }

        Position position = new Position(
                Double.parseDouble(matcher.group(1)),
                Double.parseDouble(matcher.group(2)));

        Velocity velocity = new Velocity(
                Double.parseDouble(matcher.group(3)),
                Double.parseDouble(matcher.group(4)));

        return new Robot(position, velocity);
    }

    private static List<Robot> parseInput(String filename) throws Exception {
        List<Robot> robots = new ArrayList<>();
        List<String> lines = Files.readAllLines(Path.of(filename));

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                robots.add(parseRobot(line));
            }
        }

        return robots;
    }

    private static int[][] simulateRobots(List<Robot> robots, double time) {
        int[][] grid = new int[HEIGHT][WIDTH];

        for (Robot robot : robots) {
            Position finalPos = robot.getPositionAfterTime(time);
            grid[(int) finalPos.y()][(int) finalPos.x()]++;
        }

        return grid;
    }

    private static boolean isChristmasTreePattern(int[][] grid) {
        // Look for a triangular pattern of robots that could represent a Christmas tree
        // We'll scan the grid for potential tree tops and check for a triangular shape
        // below

        for (int y = 1; y < HEIGHT - 5; y++) { // Leave room for the tree
            for (int x = 1; x < WIDTH - 5; x++) {
                if (grid[y][x] > 0) { // Potential tree top
                    // Check for a triangular pattern below
                    if (isTriangularPattern(grid, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isTriangularPattern(int[][] grid, int topX, int topY) {
        // Define minimum requirements for a Christmas tree pattern:
        // 1. Single point at top
        // 2. Gradually widening pattern below
        // 3. At least 5 rows tall
        // 4. Some form of trunk at the bottom

        if (grid[topY][topX] == 0)
            return false;

        int treeHeight = 5; // Minimum height for a recognizable tree
        int robotCount = 0;

        // Check for widening pattern
        for (int row = 0; row < treeHeight; row++) {
            int y = topY + row;
            if (y >= HEIGHT)
                return false;

            // Expected width at this level (wider as we go down)
            int expectedWidth = 1 + (row * 2);
            int robotsInRow = 0;

            // Check for robots in this row forming roughly triangular shape
            for (int dx = -row; dx <= row; dx++) {
                int x = topX + dx;
                if (x < 0 || x >= WIDTH)
                    continue;
                if (grid[y][x] > 0) {
                    robotsInRow += grid[y][x];
                }
            }

            // For tree shape, we want more robots in lower rows
            if (row > 0 && robotsInRow <= 1)
                return false;
            robotCount += robotsInRow;
        }

        // Check for trunk (1-2 robots in a column below the tree)
        int trunkY = topY + treeHeight;
        if (trunkY < HEIGHT) {
            boolean hasTrunk = false;
            for (int dx = -1; dx <= 1; dx++) {
                int x = topX + dx;
                if (x >= 0 && x < WIDTH && grid[trunkY][x] > 0) {
                    hasTrunk = true;
                    break;
                }
            }
            if (!hasTrunk)
                return false;
        }

        // Need minimum number of robots to form a recognizable tree
        return robotCount >= 8;
    }

    private static int findTreePattern(List<Robot> robots) {
        for (int time = 0; time < MAX_TIME; time++) {
            int[][] grid = simulateRobots(robots, time);
            if (isChristmasTreePattern(grid)) {
                return time;
            }
        }
        return -1; // Pattern not found
    }

    public static void main(String[] args) {
        try {
            List<Robot> robots = parseInput("input/day14.txt");
            int timeToTree = findTreePattern(robots);
            if (timeToTree != -1) {
                System.out.println("Christmas tree pattern found after " + timeToTree + " seconds");
            } else {
                System.out.println("No Christmas tree pattern found within " + MAX_TIME + " seconds");
            }
        } catch (Exception e) {
            System.err.println("Error processing input: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
