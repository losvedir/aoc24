package Day8;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day8Part1Opus {
    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("input/day8.txt"));
        var map = parseMap(input);
        var antinodeCount = countAntinodes(map);
        System.out.println("Number of unique antinodes: " + antinodeCount);
    }

    private static char[][] parseMap(String input) {
        return input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private static int countAntinodes(char[][] map) {
        var antinodes = new ArrayList<String>();

        for (var row = 0; row < map.length; row++) {
            for (var col = 0; col < map[row].length; col++) {
                var antenna = map[row][col];
                if (antenna != '.') {
                    addAntinodes(map, row, col, antenna, antinodes);
                }
            }
        }

        return antinodes.size();
    }

    private static void addAntinodes(char[][] map, int row, int col, char antenna, List<String> antinodes) {
        addAntinode(row, col, antinodes);

        for (var r = 0; r < map.length; r++) {
            for (var c = 0; c < map[r].length; c++) {
                if ((r != row || c != col) && map[r][c] == antenna) {
                    var distance1 = Math.abs(row - r) + Math.abs(col - c);
                    var distance2 = distance1 * 2;

                    var antinode1Row = row + (r - row) * 2;
                    var antinode1Col = col + (c - col) * 2;
                    if (isValid(map, antinode1Row, antinode1Col)) {
                        addAntinode(antinode1Row, antinode1Col, antinodes);
                    }

                    var antinode2Row = r + (row - r) * 2;
                    var antinode2Col = c + (col - c) * 2;
                    if (isValid(map, antinode2Row, antinode2Col)) {
                        addAntinode(antinode2Row, antinode2Col, antinodes);
                    }
                }
            }
        }
    }

    private static boolean isValid(char[][] map, int row, int col) {
        return row >= 0 && row < map.length && col >= 0 && col < map[row].length;
    }

    private static void addAntinode(int row, int col, List<String> antinodes) {
        antinodes.add(row + "," + col);
    }
}
