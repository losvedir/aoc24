package day15part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GPT4o {

    private char[][] warehouse;
    private int robotRow;
    private int robotCol;

    public GPT4o(List<String> map) {
        initializeWarehouse(map);
    }

    private void initializeWarehouse(List<String> map) {
        int rows = map.size();
        int cols = map.get(0).length();
        warehouse = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                warehouse[r][c] = map.get(r).charAt(c);
                if (warehouse[r][c] == '@') {
                    robotRow = r;
                    robotCol = c;
                }
            }
        }
    }

    public void processMoves(String moves) {
        for (char move : moves.toCharArray()) {
            moveRobot(move);
        }
    }

    private void moveRobot(char direction) {
        int newRow = robotRow;
        int newCol = robotCol;

        switch (direction) {
            case '^':
                newRow--;
                break;
            case 'v':
                newRow++;
                break;
            case '<':
                newCol--;
                break;
            case '>':
                newCol++;
                break;
        }

        if (isValidMove(newRow, newCol)) {
            char target = warehouse[newRow][newCol];

            if (target == 'O') {
                int boxNewRow = newRow + (newRow - robotRow);
                int boxNewCol = newCol + (newCol - robotCol);

                if (isValidMove(boxNewRow, boxNewCol) && warehouse[boxNewRow][boxNewCol] == '.') {
                    warehouse[boxNewRow][boxNewCol] = 'O';
                    warehouse[newRow][newCol] = '@';
                    warehouse[robotRow][robotCol] = '.';
                    robotRow = newRow;
                    robotCol = newCol;
                }
            } else if (target == '.') {
                warehouse[newRow][newCol] = '@';
                warehouse[robotRow][robotCol] = '.';
                robotRow = newRow;
                robotCol = newCol;
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < warehouse.length && col >= 0 && col < warehouse[0].length
                && warehouse[row][col] != '#';
    }

    public int calculateGPS() {
        int gpsSum = 0;
        for (int r = 0; r < warehouse.length; r++) {
            for (int c = 0; c < warehouse[0].length; c++) {
                if (warehouse[r][c] == 'O') {
                    gpsSum += 100 * r + c;
                }
            }
        }
        return gpsSum;
    }

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("input/day15.txt"))) {
            List<String> map = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                map.add(line);
            }

            GPT4o simulator = new GPT4o(map);

            StringBuilder moves = new StringBuilder();
            while ((line = br.readLine()) != null) {
                moves.append(line.trim());
            }

            simulator.processMoves(moves.toString());
            System.out.println("Sum of GPS coordinates: " + simulator.calculateGPS());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
