package Day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Day 8: Resonant Collinearity
 * 
 * This solution:
 * 1. Reads the input file (input/day8.txt) and stores it in a 2D list of chars.
 * 2. Identifies all antennas (characters that are not '.') and their
 * coordinates.
 * 3. Groups antennas by their frequency character.
 * 4. For each pair of antennas with the same frequency, calculates the two
 * antinode positions.
 * - If we have two antennas A(x1,y1) and B(x2,y2), the antinodes are:
 * P1 = (2*x1 - x2, 2*y1 - y2) and P2 = (2*x2 - x1, 2*y2 - y1)
 * This comes from the condition that one antenna is twice as far as the other
 * from the antinode.
 * 5. Filters out antinodes that fall outside the map.
 * 6. Collects all unique antinode positions within the map.
 * 7. Prints the count of unique antinode locations.
 */
public class Day8Part1o1 {
    public static void main(String[] args) throws IOException {
        // Read the input
        List<String> lines = Files.readAllLines(Path.of("input/day8.txt"));

        int height = lines.size();
        int width = lines.isEmpty() ? 0 : lines.get(0).length();

        // Parse the map into a 2D char array
        char[][] map = new char[height][width];
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                map[y][x] = line.charAt(x);
            }
        }

        // Find all antennas and group them by their frequency
        // Frequencies are single characters that are not '.'
        Map<Character, List<int[]>> antennasByFreq = new HashMap<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = map[y][x];
                if (c != '.') {
                    antennasByFreq.computeIfAbsent(c, k -> new ArrayList<>()).add(new int[] { x, y });
                }
            }
        }

        // Set to store unique antinode positions
        Set<String> antinodePositions = new HashSet<>();

        // For each frequency group, consider all pairs of antennas
        for (Map.Entry<Character, List<int[]>> entry : antennasByFreq.entrySet()) {
            List<int[]> antennas = entry.getValue();
            int n = antennas.size();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    int[] A = antennas.get(i);
                    int[] B = antennas.get(j);

                    int x1 = A[0], y1 = A[1];
                    int x2 = B[0], y2 = B[1];

                    // Calculate the two antinode positions:
                    // P1 = 2A - B = (2*x1 - x2, 2*y1 - y2)
                    // P2 = 2B - A = (2*x2 - x1, 2*y2 - y1)
                    int p1x = 2 * x1 - x2;
                    int p1y = 2 * y1 - y2;
                    int p2x = 2 * x2 - x1;
                    int p2y = 2 * y2 - y1;

                    // Check if P1 is inside the map
                    if (p1x >= 0 && p1x < width && p1y >= 0 && p1y < height) {
                        antinodePositions.add(p1x + "," + p1y);
                    }

                    // Check if P2 is inside the map
                    if (p2x >= 0 && p2x < width && p2y >= 0 && p2y < height) {
                        antinodePositions.add(p2x + "," + p2y);
                    }
                }
            }
        }

        // The result is the number of unique antinode positions inside the map
        System.out.println(antinodePositions.size());
    }
}
