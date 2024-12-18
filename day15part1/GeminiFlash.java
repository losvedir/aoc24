package day15part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class GeminiFlash {

    private static final Map<Character, Point> MOVE_MAP = Map.of(
            '^', new Point(-1, 0),
            '>', new Point(0, 1),
            'v', new Point(1, 0),
            '<', new Point(0, -1));

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input/day15.txt"));

        // Read warehouse layout
        String line;
        char[][] warehouse = new char[0][];
        Point robotPosition = null;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            warehouse = addRow(warehouse, line.toCharArray());
            robotPosition = findRobot(warehouse);
        }

        // Read move sequence
        StringBuilder moveSequenceBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            moveSequenceBuilder.append(line.trim());
        }
        String moveSequence = moveSequenceBuilder.toString();
        reader.close();

        // Simulate robot movement
        Map<Point, Character> boxPositions = getBoxPositions(warehouse);
        moveRobot(warehouse, boxPositions, robotPosition, moveSequence);

        // Calculate and print the sum of box GPS coordinates
        int gpsSum = 0;
        for (Point boxPoint : boxPositions.keySet()) {
            gpsSum += getGpsCoordinate(boxPoint.getRow(), boxPoint.getCol());
        }
        System.out.println("Sum of all boxes' GPS coordinates: " + gpsSum);
    }

    private static void moveRobot(char[][] warehouse, Map<Point, Character> boxPositions, Point robotPosition,
            String moveSequence) {
        for (char move : moveSequence.toCharArray()) {
            Point newRobotPosition = robotPosition.add(MOVE_MAP.get(move));

            // Check if robot can move and push box if possible
            if (isValidMove(warehouse, newRobotPosition) && canPushBox(warehouse, boxPositions, robotPosition, move)) {
                warehouse[robotPosition.getRow()][robotPosition.getCol()] = '.';
                robotPosition = newRobotPosition;
                warehouse[robotPosition.getRow()][robotPosition.getCol()] = '@';
                moveBox(warehouse, boxPositions, robotPosition, move);
            }
        }
    }

    private static void moveBox(char[][] warehouse, Map<Point, Character> boxPositions, Point robotPosition,
            char move) {
        Point boxPosition = robotPosition.add(MOVE_MAP.get(move));
        warehouse[boxPositions.get(robotPosition)][robotPosition.getCol()] = '.';
        boxPositions.put(boxPosition, 'O');
        warehouse[boxPosition.getRow()][boxPosition.getCol()] = 'O';
    }

    private static boolean canPushBox(char[][] warehouse, Map<Point, Character> boxPositions, Point robotPosition,
            char move) {
        Point boxPosition = robotPosition.add(MOVE_MAP.get(move));
        return isValidMove(warehouse, boxPosition) && boxPositions.containsKey(boxPosition);
    }

    private static boolean isValidMove(char[][] warehouse, Point point) {
        return point.getRow() >= 0 && point.getRow() < warehouse.length &&
                point.getCol() >= 0 && point.getCol() < warehouse[0].length &&
                warehouse[point.getRow()][point.getCol()] != '#';
    }

    private static Map<Point, Character> getBoxPositions(char[][] warehouse) {
        Map<Point, Character> boxPositions = new HashMap<>();
        for (int row = 0; row < warehouse.length; row++) {
            for (int col = 0; col < warehouse[row].length; col++) {
                if (warehouse[row][col] == 'O') {
                    boxPositions.put(new Point(row, col), 'O');
                }
            }
        }
        return boxPositions;
    }

private static Point findRobot(char
