package day15part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Gabe {
    public static void main(String[] args) throws IOException {
        var parts = Files.readString(Path.of("input/day15.txt")).split("\\n\\n");
        char[][] originalMap = parseMap(parts[0]);
        char[][] map = doubleMap(originalMap);
        char[] instructions = parts[1].replace(("\\n"), "").toCharArray();

        for (char instruction : instructions) {
            // for (int i = 0; i < 10; i++) {
            // var instruction = instructions[i];

            // System.out.println("=== " + i + " ===");
            // printNearBy(map);
            // System.out.println("instruction: " + instruction + "\n");
            if (instruction == '<') {
                moveLeft(map);
            } else if (instruction == '^') {
                moveUp(map);
            } else if (instruction == '>') {
                moveRight(map);
            } else if (instruction == 'v') {
                moveDown(map);
            }
        }

        long gpsSum = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == '[') {
                    gpsSum += 100 * y + x;
                }
            }
        }
        System.out.println("gpsSum: " + gpsSum);
    }

    private static void moveLeft(char[][] map) {
        var pos = findRobot(map);
        var x = pos[0];
        var y = pos[1];

        if (map[y][x - 1] == '.') {
            map[y][x - 1] = '@';
            map[y][x] = '.';
        } else if (map[y][x - 1] == '#') {
            // no-op
        } else if (map[y][x - 1] == ']') {
            if (canPushLeft(map, x - 1, y)) {
                doPushLeft(map, x - 1, y);
                map[y][x - 1] = '@';
                map[y][x] = '.';
            }
        }
    }

    private static boolean canPushLeft(char[][] map, int x, int y) {
        return map[y][x] == '.' || (map[y][x] == ']' && canPushLeft(map, x - 2, y));
    }

    private static void doPushLeft(char[][] map, int x, int y) {
        if (map[y][x] == '.') {
            return;
        }
        if (map[y][x - 2] == '#') {
            throw new RuntimeException("Shouldn't be pushing left");
        }
        if (map[y][x - 2] == ']') {
            doPushLeft(map, x - 2, y);
        }
        map[y][x - 2] = '[';
        map[y][x - 1] = ']';
        map[y][x] = '.';
    }

    private static void moveRight(char[][] map) {
        var pos = findRobot(map);
        var x = pos[0];
        var y = pos[1];

        if (map[y][x + 1] == '.') {
            map[y][x + 1] = '@';
            map[y][x] = '.';
        } else if (map[y][x + 1] == '#') {
            // no-op
        } else if (map[y][x + 1] == '[') {
            if (canPushRight(map, x + 1, y)) {
                doPushRight(map, x + 1, y);
                map[y][x + 1] = '@';
                map[y][x] = '.';
            }
        }
    }

    private static boolean canPushRight(char[][] map, int x, int y) {
        return map[y][x] == '.' || (map[y][x] == '[' && canPushRight(map, x + 2, y));
    }

    private static void doPushRight(char[][] map, int x, int y) {
        if (map[y][x] == '.') {
            return;
        }
        if (map[y][x + 2] == '#') {
            throw new RuntimeException("Shouldn't be pushing right");
        }
        if (map[y][x + 2] == '[') {
            doPushRight(map, x + 2, y);
        }
        map[y][x + 2] = ']';
        map[y][x + 1] = '[';
        map[y][x] = '.';
    }

    private static void moveUp(char[][] map) {
        var pos = findRobot(map);
        var x = pos[0];
        var y = pos[1];

        if (map[y - 1][x] == '.') {
            map[y - 1][x] = '@';
            map[y][x] = '.';
        } else if (map[y - 1][x] == '#') {
            // no-op
        } else if (map[y - 1][x] == '[' || map[y - 1][x] == ']') {
            if (canPushUp(map, x, y - 1)) {
                doPushUp(map, x, y - 1);
                map[y - 1][x] = '@';
                map[y][x] = '.';
            }
        }
    }

    private static boolean canPushUp(char[][] map, int x, int y) {
        return (map[y][x] == '.') ||
                (map[y][x] == '[' && canPushUp(map, x, y - 1) && canPushUp(map, x + 1, y - 1)) ||
                (map[y][x] == ']' && canPushUp(map, x, y - 1) && canPushUp(map, x - 1, y - 1));
    }

    private static void doPushUp(char[][] map, int x, int y) {
        if (map[y][x] == '[') {
            doPushUp(map, x, y - 1);
            doPushUp(map, x + 1, y - 1);
            map[y - 1][x] = '[';
            map[y - 1][x + 1] = ']';
            map[y][x] = '.';
            map[y][x + 1] = '.';
        } else if (map[y][x] == ']') {
            doPushUp(map, x, y - 1);
            doPushUp(map, x - 1, y - 1);
            map[y - 1][x] = ']';
            map[y - 1][x - 1] = '[';
            map[y][x] = '.';
            map[y][x - 1] = '.';
        }
    }

    private static void moveDown(char[][] map) {
        var pos = findRobot(map);
        var x = pos[0];
        var y = pos[1];

        if (map[y + 1][x] == '.') {
            map[y + 1][x] = '@';
            map[y][x] = '.';
        } else if (map[y + 1][x] == '#') {
            // no-op
        } else if (map[y + 1][x] == '[' || map[y + 1][x] == ']') {
            if (canPushDown(map, x, y + 1)) {
                doPushDown(map, x, y + 1);
                map[y + 1][x] = '@';
                map[y][x] = '.';
            }
        }
    }

    private static boolean canPushDown(char[][] map, int x, int y) {
        return (map[y][x] == '.') ||
                (map[y][x] == '[' && canPushDown(map, x, y + 1) && canPushDown(map, x + 1, y + 1)) ||
                (map[y][x] == ']' && canPushDown(map, x, y + 1) && canPushDown(map, x - 1, y + 1));
    }

    private static void doPushDown(char[][] map, int x, int y) {
        if (map[y][x] == '[') {
            doPushDown(map, x, y + 1);
            doPushDown(map, x + 1, y + 1);
            map[y + 1][x] = '[';
            map[y + 1][x + 1] = ']';
            map[y][x] = '.';
            map[y][x + 1] = '.';
        } else if (map[y][x] == ']') {
            doPushDown(map, x, y + 1);
            doPushDown(map, x - 1, y + 1);
            map[y + 1][x] = ']';
            map[y + 1][x - 1] = '[';
            map[y][x] = '.';
            map[y][x - 1] = '.';
        }
    }

    private static int[] findRobot(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == '@') {
                    return new int[] { x, y };
                }
            }
        }
        return new int[] { 0, 0 };
    }

    private static char[][] parseMap(String m) {
        var mapLines = m.split("\\n");
        int rows = mapLines.length;
        int cols = mapLines[0].length();
        var map = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            map[r] = mapLines[r].toCharArray();
        }

        return map;
    }

    private static char[][] doubleMap(char[][] m) {
        var map = new char[m.length * 2][m[0].length * 2];
        for (int y = 0; y < m.length; y++) {
            for (int x = 0; x < m[0].length; x++) {
                if (m[y][x] == '#') {
                    map[y][x * 2] = '#';
                    map[y][x * 2 + 1] = '#';
                } else if (m[y][x] == 'O') {
                    map[y][x * 2] = '[';
                    map[y][x * 2 + 1] = ']';
                } else if (m[y][x] == '@') {
                    map[y][x * 2] = '@';
                    map[y][x * 2 + 1] = '.';
                } else if (m[y][x] == '.') {
                    map[y][x * 2] = '.';
                    map[y][x * 2 + 1] = '.';
                }
            }
        }
        return map;
    }

    private static void print(char[][] map) {
        for (char[] line : map) {
            for (char c : line) {
                System.out.print(c);
            }
            System.out.print("\n");
        }
    }

    private static void printNearBy(char[][] map) {
        var pos = findRobot(map);
        var x = pos[0];
        var y = pos[1];

        for (int r = y - 10; r < y + 10; r++) {
            for (int c = x - 15; c < x + 15; c++) {
                if ((r >= 0 && r < map.length) && (c >= 0 && c < map[0].length)) {
                    System.out.print(map[r][c]);
                } else {
                    System.out.print(' ');
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
}
