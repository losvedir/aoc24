package day14part1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GPT4o {

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int TIME = 100;

    public static void main(String[] args) throws IOException {
        List<Robot> robots = parseInput("input/day14.txt");

        int[][] grid = new int[WIDTH][HEIGHT];

        // Simulate robots' positions after 100 seconds
        for (Robot robot : robots) {
            int finalX = (robot.x + TIME * robot.vx) % WIDTH;
            int finalY = (robot.y + TIME * robot.vy) % HEIGHT;

            // Handle negative modulo
            if (finalX < 0)
                finalX += WIDTH;
            if (finalY < 0)
                finalY += HEIGHT;

            grid[finalX][finalY]++;
        }

        // Count robots in each quadrant
        int midX = WIDTH / 2;
        int midY = HEIGHT / 2;

        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (x == midX || y == midY || grid[x][y] == 0)
                    continue; // Ignore middle line and empty tiles

                if (x > midX && y < midY)
                    q1 += grid[x][y];
                else if (x < midX && y < midY)
                    q2 += grid[x][y];
                else if (x < midX && y > midY)
                    q3 += grid[x][y];
                else if (x > midX && y > midY)
                    q4 += grid[x][y];
            }
        }

        // Compute the safety factor
        int safetyFactor = q1 * q2 * q3 * q4;

        System.out.println("Safety Factor: " + safetyFactor);
    }

    private static List<Robot> parseInput(String filePath) throws IOException {
        List<Robot> robots = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");

                // Extract position
                String[] position = parts[0].substring(2).split(",");
                int x = Integer.parseInt(position[0]);
                int y = Integer.parseInt(position[1]);

                // Extract velocity
                String[] velocity = parts[1].substring(2).split(",");
                int vx = Integer.parseInt(velocity[0]);
                int vy = Integer.parseInt(velocity[1]);

                robots.add(new Robot(x, y, vx, vy));
            }
        }
        return robots;
    }

    private static class Robot {
        int x, y, vx, vy;

        Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }
    }
}
