package Day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Day 8, Part Two: Resonant Harmonics
 *
 * In this updated model:
 * Any grid position is an antinode if it lies exactly in line with at least two
 * antennas of the same frequency.
 * This includes the positions of the antennas themselves, as long as there is
 * more than one antenna of that frequency
 * that aligns with it.
 *
 * Steps to solve:
 * 1. Read and parse the input map.
 * 2. Identify antennas and group them by frequency.
 * 3. For each frequency group with at least two antennas:
 * - Consider every pair of antennas (A, B).
 * - Determine the line passing through A and B.
 * - For that line, find all integer grid points within the map that lie on this
 * line.
 * * Special cases:
 * - Vertical line: x is constant, iterate over y.
 * - Horizontal line: y is constant, iterate over x.
 * - Otherwise, use the line equation and iterate over the smaller dimension to
 * find integer solutions.
 * - Add all those points to a global set of antinode positions.
 * 4. The result is the size of the global set of antinodes.
 *
 * Important details:
 * - Lines are infinite; we consider all points in the grid that lie on that
 * line.
 * - A point is considered an antinode if it aligns with at least one line
 * defined by two antennas of the same frequency.
 * - Antennas themselves count if they align with at least one other antenna of
 * the same frequency.
 */
public class Day8Part2o1 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day8.txt"));
        int height = lines.size();
        int width = lines.isEmpty() ? 0 : lines.get(0).length();

        char[][] map = new char[height][width];
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                map[y][x] = line.charAt(x);
            }
        }

        // Group antennas by frequency
        Map<Character, List<int[]>> antennasByFreq = new HashMap<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = map[y][x];
                if (c != '.') {
                    antennasByFreq.computeIfAbsent(c, k -> new ArrayList<>()).add(new int[] { x, y });
                }
            }
        }

        Set<String> antinodes = new HashSet<>();

        // Helper function to add a point
        // We store points as "x,y" in the set
        BiConsumer<Integer, Integer> addPoint = (Integer px, Integer py) -> {
            if (px >= 0 && px < width && py >= 0 && py < height) {
                antinodes.add(px + "," + py);
            }
        };

        // Process each frequency group
        for (Map.Entry<Character, List<int[]>> entry : antennasByFreq.entrySet()) {
            List<int[]> antennas = entry.getValue();
            if (antennas.size() < 2) {
                // If there's only one antenna of this frequency, it can't form a line with
                // another, so skip
                continue;
            }

            // Consider every pair of antennas to define a line
            for (int i = 0; i < antennas.size(); i++) {
                for (int j = i + 1; j < antennas.size(); j++) {
                    int[] A = antennas.get(i);
                    int[] B = antennas.get(j);
                    int x1 = A[0], y1 = A[1];
                    int x2 = B[0], y2 = B[1];

                    int dx = x2 - x1;
                    int dy = y2 - y1;

                    // Normalize direction vector to handle step calculation
                    // gcd ensures direction is in lowest terms
                    int g = gcd(Math.abs(dx), Math.abs(dy));
                    if (g == 0) {
                        // Same point (shouldn't happen unless input is weird)
                        // If it does, it's just one antenna repeated.
                        continue;
                    }
                    dx /= g;
                    dy /= g;

                    // Special cases: vertical or horizontal lines
                    if (dx == 0) {
                        // Vertical line: all points with x = x1
                        int fixedX = x1;
                        if (fixedX >= 0 && fixedX < width) {
                            for (int yy = 0; yy < height; yy++) {
                                // All points with this x are on line
                                addPoint.accept(fixedX, yy);
                            }
                        }
                    } else if (dy == 0) {
                        // Horizontal line: all points with y = y1
                        int fixedY = y1;
                        if (fixedY >= 0 && fixedY < height) {
                            for (int xx = 0; xx < width; xx++) {
                                addPoint.accept(xx, fixedY);
                            }
                        }
                    } else {
                        // General line case
                        // Equation of the line through (x1,y1) and direction (dx,dy):
                        // (y - y1)*dx = (x - x1)*dy
                        // Let's rewrite in form suitable for checking integer solutions.
                        // y = y1 + ((x - x1)*dy)/dx must be integer
                        // For each candidate x, check if (x - x1)*dy is divisible by dx.
                        //
                        // To reduce complexity, choose to iterate over the dimension where we can
                        // easily check divisibility:
                        // If |dx| >= |dy|, we might iterate over x, else iterate over y.

                        if (Math.abs(dx) >= Math.abs(dy)) {
                            // Iterate over x
                            for (int xx = 0; xx < width; xx++) {
                                int numerator = (xx - x1) * dy;
                                if (numerator % dx == 0) {
                                    int yy = y1 + numerator / dx;
                                    addPoint.accept(xx, yy);
                                }
                            }
                        } else {
                            // Iterate over y
                            // rearranging the equation for x:
                            // (y - y1)*dx = (x - x1)*dy
                            // x - x1 = ((y - y1)*dx)/dy
                            // x = x1 + ((y - y1)*dx)/dy
                            for (int yy = 0; yy < height; yy++) {
                                int numerator = (yy - y1) * dx;
                                if (numerator % dy == 0) {
                                    int xx = x1 + numerator / dy;
                                    addPoint.accept(xx, yy);
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println(antinodes.size());
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }
}
