package day14part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiPro {

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int SIMULATION_TIME = 100;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day14.txt"));
        List<Robot> robots = lines.stream()
                .map(GeminiPro::parseRobot)
                .toList();

        simulate(robots, SIMULATION_TIME);

        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        for (Robot robot : robots) {
            if (robot.x() < WIDTH / 2 && robot.y() < HEIGHT / 2) {
                q1++;
            } else if (robot.x() > WIDTH / 2 && robot.y() < HEIGHT / 2) {
                q2++;
            } else if (robot.x() < WIDTH / 2 && robot.y() > HEIGHT / 2) {
                q3++;
            } else if (robot.x() > WIDTH / 2 && robot.y() > HEIGHT / 2) {
                q4++;
            }
        }

        long safetyFactor = (long) q1 * q2 * q3 * q4;
        System.out.println("Safety factor: " + safetyFactor);
    }

    private static Robot parseRobot(String line) {
        Pattern pattern = Pattern.compile("p=(\\d+),(\\d+) v=(\\d+),(\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int vx = Integer.parseInt(matcher.group(3));
            int vy = Integer.parseInt(matcher.group(4));
            return new Robot(x, y, vx, vy);
        } else {
            throw new IllegalArgumentException("Invalid input line: " + line);
        }
    }

    private static void simulate(List<Robot> robots, int time) {
        for (int i = 0; i < time; i++) {
            for (Robot robot : robots) {
                robot.move();
            }
        }
    }

    private record Robot(int x, int y, int vx, int vy) {
        public void move() {
            x = (x + vx) % WIDTH;
            y = (y + vy) % HEIGHT;
        }
    }
}
