package day14part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiFlash {

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day14.txt"));
        List<Robot> robots = parseRobots(lines);

        Set<String> seenStates = new HashSet<>();
        int time = 0;

        while (true) {
            int[][] grid = new int[HEIGHT][WIDTH];
            for (Robot robot : robots) {
                int x = (robot.px + robot.vx * time + WIDTH * 10000) % WIDTH; // Add large multiple to avoid negative
                                                                              // modulo
                int y = (robot.py + robot.vy * time + HEIGHT * 10000) % HEIGHT;
                grid[y][x]++;
            }

            String state = gridToString(grid);
            if (seenStates.contains(state)) {
                System.out.println("State repeats at time: " + time);
                break;
            }
            seenStates.add(state);

            time++;
        }
    }

    private static String gridToString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : grid) {
            sb.append(Arrays.toString(row));
        }
        return sb.toString();
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
