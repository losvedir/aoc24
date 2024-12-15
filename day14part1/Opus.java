package day14part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Opus {
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int TIME = 100;

    public static void main(String[] args) throws IOException {
        List<Robot> robots = Files.lines(Paths.get("input/day14.txt"))
                .map(Robot::parse)
                .collect(Collectors.toList());

        int[][] grid = new int[HEIGHT][WIDTH];
        for (int t = 0; t < TIME; t++) {
            for (Robot robot : robots) {
                robot.move();
            }
        }

        for (Robot robot : robots) {
            int x = robot.x;
            int y = robot.y;
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                grid[y][x]++;
            }
        }

        int q1 = countRobots(grid, 0, 0, WIDTH / 2, HEIGHT / 2);
        int q2 = countRobots(grid, WIDTH / 2, 0, WIDTH, HEIGHT / 2);
        int q3 = countRobots(grid, 0, HEIGHT / 2, WIDTH / 2, HEIGHT);
        int q4 = countRobots(grid, WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);

        int safetyFactor = q1 * q2 * q3 * q4;
        System.out.println("Safety factor after " + TIME + " seconds: " + safetyFactor);
    }

    private static int countRobots(int[][] grid, int minX, int minY, int maxX, int maxY) {
        int count = 0;
        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                count += grid[y][x];
            }
        }
        return count;
    }

    private static class Robot {
        int x, y, vx, vy;

        Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        void move() {
            x = (x + vx + WIDTH) % WIDTH;
            y = (y + vy + HEIGHT) % HEIGHT;
        }

        static Robot parse(String line) {
            Pattern pattern = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int vx = Integer.parseInt(matcher.group(3));
                int vy = Integer.parseInt(matcher.group(4));
                return new Robot(x, y, vx, vy);
            }
            throw new IllegalArgumentException("Invalid input: " + line);
        }
    }
}
