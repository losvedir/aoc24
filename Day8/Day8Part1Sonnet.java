// package Day8;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;
// import java.util.stream.IntStream;

// public class Day8Part1Sonnet {
//     record Point(int x, int y) {
//     }

//     record Antenna(Point position, char frequency) {
//     }

//     public static void main(String[] args) {
//         try {
//             List<String> lines = Files.readAllLines(Path.of("input/day8.txt"));
//             System.out.println("Solution: " + solve(lines));
//         } catch (Exception e) {
//             System.err.println("Error reading input file: " + e.getMessage());
//         }
//     }

//     public static int solve(List<String> input) {
//         // Parse input into list of antennas
//         List<Antenna> antennas = parseAntennas(input);

//         // Find all antinodes
//         Set<Point> antinodes = findAntinodes(antennas, input.size(), input.get(0).length());

//         return antinodes.size();
//     }

//     private static List<Antenna> parseAntennas(List<String> input) {
//         List<Antenna> antennas = new ArrayList<>();

//         for (int y = 0; y < input.size(); y++) {
//             String line = input.get(y);
//             for (int x = 0; x < line.length(); x++) {
//                 char c = line.charAt(x);
//                 if (c != '.') {
//                     antennas.add(new Antenna(new Point(x, y), c));
//                 }
//             }
//         }

//         return antennas;
//     }

//     private static Set<Point> findAntinodes(List<Antenna> antennas, int height, int width) {
//         Set<Point> antinodes = new HashSet<>();

//         // For each pair of antennas with the same frequency
//         for (int i = 0; i < antennas.size(); i++) {
//             for (int j = i + 1; j < antennas.size(); j++) {
//                 Antenna a1 = antennas.get(i);
//                 Antenna a2 = antennas.get(j);

//                 // Skip if frequencies don't match
//                 if (a1.frequency() != a2.frequency()) {
//                     continue;
//                 }

//                 // Calculate distance between antennas
//                 int dx = a2.position().x() - a1.position().x();
//                 int dy = a2.position().y() - a1.position().y();

//                 // Calculate potential antinode positions (one on each side)
//                 // Antinode is at distance d from one antenna and 2d from the other
//                 addAntinodesForPair(a1.position(), a2.position(), dx, dy, height, width, antinodes);
//             }
//         }

//         return antinodes;
//     }

//     private static void addAntinodesForPair(Point p1, Point p2, int dx, int dy, int height, int width,
//             Set<Point> antinodes) {
//         // Calculate perpendicular vector (-dy, dx) for antinode positions
//         // Scale it to maintain integer coordinates
//         int px = -dy;
//         int py = dx;

//         // Calculate the midpoint between the antennas
//         double midX = (p1.x() + p2.x()) / 2.0;
//         double midY = (p1.y() + p2.y()) / 2.0;

//         // Distance between antennas
//         double distance = Math.sqrt(dx * dx + dy * dy);

//         // Calculate scaling factor for perpendicular vector to get correct distance
//         double scale = distance / (2 * Math.sqrt(px * px + py * py));

//         // Calculate antinode positions on both sides
//         for (int sign : new int[] { -1, 1 }) {
//             int antinodeX = (int) Math.round(midX + sign * px * scale);
//             int antinodeY = (int) Math.round(midY + sign * py * scale);

//             // Check if antinode is within bounds
//             if (antinodeX >= 0 && antinodeX < width && antinodeY >= 0 && antinodeY < height) {
//                 antinodes.add(new Point(antinodeX, antinodeY));
//             }
//         }
//     }
// }

package Day8;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8Part1Sonnet {
    record Point(int x, int y) {
    }

    record Antenna(Point position, char frequency) {
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of("input/day8.txt"));
            System.out.println("Solution: " + solve(lines));
        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static int solve(List<String> input) {
        List<Antenna> antennas = parseAntennas(input);
        Set<Point> antinodes = findAntinodes(antennas, input.size(), input.get(0).length());
        return antinodes.size();
    }

    private static List<Antenna> parseAntennas(List<String> input) {
        List<Antenna> antennas = new ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c != '.') {
                    antennas.add(new Antenna(new Point(x, y), c));
                }
            }
        }
        return antennas;
    }

    private static Set<Point> findAntinodes(List<Antenna> antennas, int height, int width) {
        Set<Point> antinodes = new HashSet<>();

        // For each pair of antennas with the same frequency
        for (int i = 0; i < antennas.size(); i++) {
            for (int j = i + 1; j < antennas.size(); j++) {
                Antenna a1 = antennas.get(i);
                Antenna a2 = antennas.get(j);

                // Skip if frequencies don't match
                if (a1.frequency() != a2.frequency()) {
                    continue;
                }

                // Find antinodes where one antenna is twice as far as the other
                addAntinodesForPair(a1.position(), a2.position(), height, width, antinodes);
            }
        }

        return antinodes;
    }

    private static void addAntinodesForPair(Point p1, Point p2, int height, int width, Set<Point> antinodes) {
        int dx = p2.x() - p1.x();
        int dy = p2.y() - p1.y();

        // Try both directions: p1->p2 and p2->p1
        addAntinodesInDirection(p1, p2, dx, dy, height, width, antinodes);
        addAntinodesInDirection(p2, p1, -dx, -dy, height, width, antinodes);
    }

    private static void addAntinodesInDirection(Point start, Point end, int dx, int dy, int height, int width,
            Set<Point> antinodes) {
        // Calculate two potential antinode positions:
        // 1. Beyond the second antenna (at 2x distance from first antenna)
        // 2. Between the antennas (at 1/2 distance from first antenna)

        // Normalize the direction vector
        int gcd = gcd(Math.abs(dx), Math.abs(dy));
        if (gcd > 0) {
            dx /= gcd;
            dy /= gcd;
        }

        // Calculate the total number of steps between antennas
        int steps = Math.max(Math.abs(end.x() - start.x()) / Math.abs(dx),
                Math.abs(end.y() - start.y()) / Math.abs(dy));

        // Add antinode beyond the second antenna (2x distance)
        int beyondX = start.x() + dx * (steps * 2);
        int beyondY = start.y() + dy * (steps * 2);
        if (beyondX >= 0 && beyondX < width && beyondY >= 0 && beyondY < height) {
            antinodes.add(new Point(beyondX, beyondY));
        }

        // Add antinode between antennas (1/2 distance)
        int betweenX = start.x() + dx * (steps / 2);
        int betweenY = start.y() + dy * (steps / 2);
        if (betweenX >= 0 && betweenX < width && betweenY >= 0 && betweenY < height) {
            antinodes.add(new Point(betweenX, betweenY));
        }
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
