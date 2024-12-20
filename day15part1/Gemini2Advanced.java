package day15part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Gemini2Advanced {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day15.txt"));
        List<String> gridLines = new ArrayList<>();
        String moves = "";

        boolean readingGrid = true;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                readingGrid = false;
                continue;
            }
            if (readingGrid) {
                gridLines.add(line);
            } else {
                moves += line;
            }
        }

        char[][] grid = new char[gridLines.size()][gridLines.get(0).length()];
        int robotRow = -1, robotCol = -1;

        for (int i = 0; i < gridLines.size(); i++) {
            for (int j = 0; j < gridLines.get(i).length(); j++) {
                grid[i][j] = gridLines.get(i).charAt(j);
                if (grid[i][j] == '@') {
                    robotRow = i;
                    robotCol = j;
                }
            }
        }

        for (char move : moves.toCharArray()) {
            int newRobotRow = robotRow;
            int newRobotCol = robotCol;
            int boxRow = -1, boxCol = -1;

            switch (move) {
                case '<':
                    newRobotCol--;
                    break;
                case '>':
                    newRobotCol++;
                    break;
                case '^':
                    newRobotRow--;
                    break;
                case 'v':
                    newRobotRow++;
                    break;
            }

            if (newRobotRow < 0 || newRobotRow >= grid.length || newRobotCol < 0 || newRobotCol >= grid[0].length
                    || grid[newRobotRow][newRobotCol] == '#') {
                continue;
            }

            if (grid[newRobotRow][newRobotCol] == 'O') {
                boxRow = newRobotRow;
                boxCol = newRobotCol;
                int newBoxRow = boxRow;
                int newBoxCol = boxCol;

                switch (move) {
                    case '<':
                        newBoxCol--;
                        break;
                    case '>':
                        newBoxCol++;
                        break;
                    case '^':
                        newBoxRow--;
                        break;
                    case 'v':
                        newBoxRow++;
                        break;
                }
                if (newBoxRow < 0 || newBoxRow >= grid.length || newBoxCol < 0 || newBoxCol >= grid[0].length
                        || grid[newBoxRow][newBoxCol] == '#' || grid[newBoxRow][newBoxCol] == 'O') {
                    continue;
                }
                grid[boxRow][boxCol] = '.';
                grid[newBoxRow][newBoxCol] = 'O';
            }

            grid[robotRow][robotCol] = '.';
            robotRow = newRobotRow;
            robotCol = newRobotCol;
            grid[robotRow][robotCol] = '@';

        }

        long gpsSum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    gpsSum += (i * 100) + j;
                }
            }
        }

        System.out.println(gpsSum);
    }
}
