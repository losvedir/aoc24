package day14part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GPTo1Pro {
    // Dimensions as given in the problem statement
    private static final int WIDTH = 101; // x from 0 to 100
    private static final int HEIGHT = 103; // y from 0 to 102
    private static final int TIME = 100;

    public static void main(String[] args) throws IOException {
        // Read the input lines
        List<String> lines = Files.readAllLines(Path.of("input/day14.txt"));

        // Parse the robot positions and velocities
        // Each line format: p=X,Y v=VX,VY
        record Robot(int x, int y, int vx, int vy) {
        }

        var robots = lines.stream()
                .map(line -> {
                    // Example line: p=0,4 v=3,-3
                    // Split on space first
                    var parts = line.split("\\s+");
                    // parts[0]: p=0,4
                    // parts[1]: v=3,-3
                    var posPart = parts[0].substring(2); // remove "p="
                    var velPart = parts[1].substring(2); // remove "v="

                    var posCoords = posPart.split(",");
                    var velCoords = velPart.split(",");

                    int x = Integer.parseInt(posCoords[0]);
                    int y = Integer.parseInt(posCoords[1]);
                    int vx = Integer.parseInt(velCoords[0]);
                    int vy = Integer.parseInt(velCoords[1]);

                    return new Robot(x, y, vx, vy);
                })
                .toList();

        // Simulate after 100 seconds
        // New position after t=100 seconds:
        // x(t) = (x_initial + vx * 100) mod WIDTH
        // y(t) = (y_initial + vy * 100) mod HEIGHT
        // Use proper modulo to handle negative velocities
        var finalPositions = robots.stream().map(r -> {
            int finalX = ((r.x() + r.vx() * TIME) % WIDTH + WIDTH) % WIDTH;
            int finalY = ((r.y() + r.vy() * TIME) % HEIGHT + HEIGHT) % HEIGHT;
            return new int[] { finalX, finalY };
        }).toList();

        // Determine quadrants
        // Middle lines: x=50 is vertical center line (since width=101, 0..100),
        // y=51 is horizontal center line (since height=103, 0..102).
        int midX = WIDTH / 2; // = 50
        int midY = HEIGHT / 2; // = 51

        int q1Count = 0; // Quadrant 1: x > midX and y < midY
        int q2Count = 0; // Quadrant 2: x < midX and y < midY
        int q3Count = 0; // Quadrant 3: x < midX and y > midY
        int q4Count = 0; // Quadrant 4: x > midX and y > midY

        for (var pos : finalPositions) {
            int x = pos[0];
            int y = pos[1];

            // Ignore robots on the center lines
            if (x == midX || y == midY) {
                continue;
            }

            if (x > midX && y < midY)
                q1Count++;
            else if (x < midX && y < midY)
                q2Count++;
            else if (x < midX && y > midY)
                q3Count++;
            else if (x > midX && y > midY)
                q4Count++;
        }

        long safetyFactor = (long) q1Count * q2Count * q3Count * q4Count;

        System.out.println(safetyFactor);
    }
}
