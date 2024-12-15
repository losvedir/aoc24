package day14part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SonnetHelped {
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int MAX_TIME = 100000; // Reasonable upper limit for search

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
        int midY = HEIGHT / 2;
        int midX = WIDTH / 2;

        // Count robots in top and bottom halves
        int topHalfCount = 0;
        int bottomHalfCount = 0;

        // Check for symmetry about y-axis and count robots in each half
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (grid[y][x] == 0)
                    continue;

                // Mirror position about y-axis
                int mirrorX = WIDTH - 1 - x;

                // Check symmetry (allowing for some tolerance)
                if (Math.abs(grid[y][x] - grid[y][mirrorX]) > 1) {
                    return false;
                }

                // Count robots in top and bottom halves
                if (y < midY) {
                    topHalfCount += grid[y][x];
                } else {
                    bottomHalfCount += grid[y][x];
                }
            }
        }

        // For Christmas tree pattern:
        // 1. Must have robots present (avoid empty grids)
        // 2. Bottom half should have more robots than top half
        return (topHalfCount + bottomHalfCount > 0) &&
                (bottomHalfCount > topHalfCount);
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
