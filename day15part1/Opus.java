package day15part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Opus {
    private static final char EMPTY = '.';
    private static final char WALL = '#';
    private static final char BOX = 'O';
    private static final char ROBOT = '@';

    private char[][] warehouse;
    private int robotRow;
    private int robotCol;

    public Opus(List<String> input) {
        parseWarehouse(input);
    }

    private void parseWarehouse(List<String> input) {
        warehouse = new char[input.size()][input.get(0).length()];
        for (int row = 0; row < input.size(); row++) {
            String line = input.get(row);
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                warehouse[row][col] = c;
                if (c == ROBOT) {
                    robotRow = row;
                    robotCol = col;
                }
            }
        }
    }

    public void move(char direction) {
        int newRow = robotRow;
        int newCol = robotCol;

        switch (direction) {
            case '^' -> newRow--;
            case 'v' -> newRow++;
            case '<' -> newCol--;
            case '>' -> newCol++;
        }

        if (isValidMove(newRow, newCol)) {
            if (warehouse[newRow][newCol] == BOX) {
                int nextRow = newRow + (newRow - robotRow);
                int nextCol = newCol + (newCol - robotCol);
                if (isValidMove(nextRow, nextCol)) {
                    warehouse[nextRow][nextCol] = BOX;
                    warehouse[newRow][newCol] = EMPTY;
                    warehouse[robotRow][robotCol] = EMPTY;
                    robotRow = newRow;
                    robotCol = newCol;
                }
            } else {
                warehouse[newRow][newCol] = ROBOT;
                warehouse[robotRow][robotCol] = EMPTY;
                robotRow = newRow;
                robotCol = newCol;
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < warehouse.length && col >= 0 && col < warehouse[0].length
                && warehouse[row][col] != WALL;
    }

    public int calculateGpsCoordinatesSum() {
        int sum = 0;
        for (int row = 0; row < warehouse.length; row++) {
            for (int col = 0; col < warehouse[0].length; col++) {
                if (warehouse[row][col] == BOX) {
                    sum += 100 * row + col;
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) throws IOException {
        List<String> warehouseMap = Files.readAllLines(Paths.get("input/day15.txt"));
        String moves = warehouseMap.get(warehouseMap.size() - 1);
        warehouseMap.remove(warehouseMap.size() - 1);
        warehouseMap.remove(warehouseMap.size() - 1);

        Opus opus = new Opus(warehouseMap);
        for (char move : moves.toCharArray()) {
            opus.move(move);
        }

        System.out.println("Sum of GPS coordinates: " + opus.calculateGpsCoordinatesSum());
    }
}
