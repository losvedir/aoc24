package day14part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiFlash {

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int TIME = 100;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day14.txt"));
        List<Robot> robots = parseRobots(lines);

        int[][] grid = new int[HEIGHT][WIDTH];

        for (Robot robot : robots) {
            int x = robot.px;
            int y = robot.py;
            for (int t = 0; t <= TIME; ++t) {
                if (t == TIME) {
                    grid[y][x]++;
                }
                x = (x + robot.vx + WIDTH) % WIDTH;
                y = (y + robot.vy + HEIGHT) % HEIGHT;
            }
        }

        int midX = WIDTH / 2;
        int midY = HEIGHT / 2;

        int[] quadrants = new int[4];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (x == midX || y == midY)
                    continue;
                int quadrant = (y < midY ? 0 : 2) + (x < midX ? 0 : 1);
                quadrants[quadrant] += grid[y][x];
            }
        }

        int safetyFactor = Arrays.stream(quadrants).reduce(1, (a, b) -> a * b);
        System.out.println(safetyFactor);
    }

    private static List<Robot> parseRobots(List<String> lines) {
        Pattern pattern = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
        return lines.stream().map(line -> {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid input line: " + line);
            }
            int px = Integer.parseInt(matcher.group(1));
            int py = Integer.parseInt(matcher.group(2));
            int vx = Integer.parseInt(matcher.group(3));
            int vy = Integer.parseInt(matcher.group(4));
            return new Robot(px, py, vx, vy);
        }).toList();
    }

    private record Robot(int px, int py, int vx, int vy) {
    }
}
