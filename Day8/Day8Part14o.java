package Day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day8Part14o {

    public static void main(String[] args) {
        String filename = "input/day8.txt";
        try {
            List<String> lines = Files.readAllLines(Path.of(filename));
            System.out.println("Unique Antinode Count: " + countAntinodes(lines));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static int countAntinodes(List<String> grid) {
        List<Position> antennas = new ArrayList<>();
        // Collect antenna positions with their frequencies
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.get(row).length(); col++) {
                char ch = grid.get(row).charAt(col);
                if (Character.isLetterOrDigit(ch)) {
                    antennas.add(new Position(row, col, ch));
                }
            }
        }

        Set<Position> antinodes = new HashSet<>();

        // Compare all pairs of antennas with the same frequency
        for (int i = 0; i < antennas.size(); i++) {
            for (int j = i + 1; j < antennas.size(); j++) {
                Position p1 = antennas.get(i);
                Position p2 = antennas.get(j);

                if (p1.frequency == p2.frequency) {
                    // Calculate potential antinode positions
                    Position antinode1 = p1.getAntinode(p2);
                    Position antinode2 = p2.getAntinode(p1);

                    // Add antinodes if they are within the grid bounds
                    if (antinodesWithinBounds(antinode1, grid)) {
                        antinodes.add(antinode1);
                    }
                    if (antinodesWithinBounds(antinode2, grid)) {
                        antinodes.add(antinode2);
                    }
                }
            }
        }

        return antinodes.size();
    }

    private static boolean antinodesWithinBounds(Position pos, List<String> grid) {
        return pos.row >= 0 && pos.row < grid.size() && pos.col >= 0 && pos.col < grid.get(pos.row).length();
    }

    // Position class representing an antenna or antinode
    static class Position {
        int row;
        int col;
        char frequency;

        Position(int row, int col, char frequency) {
            this.row = row;
            this.col = col;
            this.frequency = frequency;
        }

        // Calculate the antinode position for another antenna
        Position getAntinode(Position other) {
            return new Position(
                    this.row + 2 * (this.row - other.row),
                    this.col + 2 * (this.col - other.col),
                    this.frequency);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "Position{" + "row=" + row + ", col=" + col + ", frequency=" + frequency + '}';
        }
    }
}
