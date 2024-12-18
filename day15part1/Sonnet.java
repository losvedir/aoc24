package day15part1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Sonnet {
    private static final char WALL = '#';
    private static final char BOX = 'O';
    private static final char ROBOT = '@';
    private static final char EMPTY = '.';

    static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Position move(char direction) {
            return switch (direction) {
                case '^' -> new Position(row - 1, col);
                case 'v' -> new Position(row + 1, col);
                case '<' -> new Position(row, col - 1);
                case '>' -> new Position(row, col + 1);
                default -> throw new IllegalArgumentException("Invalid direction: " + direction);
            };
        }
    }

    static class Warehouse {
        private char[][] grid;
        private Position robotPos;

        Warehouse(List<String> mapLines) {
            grid = new char[mapLines.size()][mapLines.get(0).length()];
            for (int i = 0; i < mapLines.size(); i++) {
                String line = mapLines.get(i);
                for (int j = 0; j < line.length(); j++) {
                    grid[i][j] = line.charAt(j);
                    if (grid[i][j] == ROBOT) {
                        robotPos = new Position(i, j);
                    }
                }
            }
        }

        boolean isValidMove(Position pos) {
            return pos.row >= 0 && pos.row < grid.length &&
                    pos.col >= 0 && pos.col < grid[0].length &&
                    grid[pos.row][pos.col] != WALL;
        }

        void move(char direction) {
            Position newRobotPos = robotPos.move(direction);

            if (!isValidMove(newRobotPos)) {
                return;
            }

            if (grid[newRobotPos.row][newRobotPos.col] == BOX) {
                Position newBoxPos = newRobotPos.move(direction);
                if (!isValidMove(newBoxPos) || grid[newBoxPos.row][newBoxPos.col] == BOX) {
                    return;
                }
                // Move box
                grid[newBoxPos.row][newBoxPos.col] = BOX;
                grid[newRobotPos.row][newRobotPos.col] = ROBOT;
                grid[robotPos.row][robotPos.col] = EMPTY;
                robotPos = newRobotPos;
            } else {
                // Move robot
                grid[newRobotPos.row][newRobotPos.col] = ROBOT;
                grid[robotPos.row][robotPos.col] = EMPTY;
                robotPos = newRobotPos;
            }
        }

        int calculateGPSSum() {
            int sum = 0;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (grid[row][col] == BOX) {
                        sum += (row * 100 + col);
                    }
                }
            }
            return sum;
        }
    }

    public static void main(String[] args) throws Exception {
        // Read input file
        List<String> input = Files.readAllLines(Paths.get("input/day15.txt"));

        // Split input into map and moves
        List<String> mapLines = new ArrayList<>();
        String moves = "";
        boolean parsingMap = true;

        for (String line : input) {
            if (line.isEmpty()) {
                parsingMap = false;
                continue;
            }
            if (parsingMap) {
                mapLines.add(line);
            } else {
                moves += line;
            }
        }

        // Create warehouse and simulate moves
        Warehouse warehouse = new Warehouse(mapLines);
        for (char move : moves.toCharArray()) {
            warehouse.move(move);
        }

        // Calculate and print result
        System.out.println(warehouse.calculateGPSSum());
    }
}
