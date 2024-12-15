package day14part1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sonnet {
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int SIMULATION_SECONDS = 100;

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

    private static long calculateSafetyFactor(int[][] grid) {
        int midX = WIDTH / 2;
        int midY = HEIGHT / 2;

        // Count robots in each quadrant
        long topLeft = 0, topRight = 0, bottomLeft = 0, bottomRight = 0;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (grid[y][x] == 0)
                    continue;

                // Skip robots on the middle lines
                if (x == midX || y == midY)
                    continue;

                if (x < midX) {
                    if (y < midY)
                        topLeft += grid[y][x];
                    else
                        bottomLeft += grid[y][x];
                } else {
                    if (y < midY)
                        topRight += grid[y][x];
                    else
                        bottomRight += grid[y][x];
                }
            }
        }

        return topLeft * topRight * bottomLeft * bottomRight;
    }

    public static void main(String[] args) {
        try {
            List<Robot> robots = parseInput("input/day14.txt");
            int[][] finalGrid = simulateRobots(robots, SIMULATION_SECONDS);
            long safetyFactor = calculateSafetyFactor(finalGrid);
            System.out.println("Safety factor after " + SIMULATION_SECONDS + " seconds: " + safetyFactor);
        } catch (Exception e) {
            System.err.println("Error processing input: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
