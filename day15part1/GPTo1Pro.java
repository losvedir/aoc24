package day15part1;

import java.nio.file.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GPTo1Pro {
    public static void main(String[] args) throws IOException {
        // Read all lines from input file
        List<String> lines = Files.readAllLines(Paths.get("input/day15.txt"));

        // Separate map lines from move lines
        // The map is typically rectangular and fully enclosed by '#'.
        // We'll assume that once we encounter a line that doesn't match map width or
        // after we've read the map, all subsequent lines are moves.

        // First, find the map dimensions. The map must at least have one line starting
        // with '#' and ending with '#'.
        int mapWidth = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                // Once a blank line is encountered, map reading might end
                break;
            }
            if (line.contains("#")) {
                mapWidth = line.length();
                break;
            }
        }

        List<String> mapLines = new ArrayList<>();
        int index = 0;

        for (; index < lines.size(); index++) {
            String line = lines.get(index);
            if (line.trim().isEmpty()) {
                // End of map
                index++;
                break;
            }
            if (line.length() == mapWidth && line.startsWith("#") && line.endsWith("#")) {
                mapLines.add(line);
            } else {
                // If line doesn't match map width, we assume map ended
                break;
            }
        }

        // Now all remaining lines should be moves (concatenate them)
        StringBuilder movesBuilder = new StringBuilder();
        for (; index < lines.size(); index++) {
            String line = lines.get(index).trim();
            // ignore empty lines
            if (!line.isEmpty()) {
                movesBuilder.append(line);
            }
        }
        String moves = movesBuilder.toString();

        // Convert map to a mutable structure
        // We'll store it in a 2D array of chars
        int rows = mapLines.size();
        int cols = mapWidth;
        char[][] warehouse = new char[rows][cols];

        int robotR = -1;
        int robotC = -1;

        for (int r = 0; r < rows; r++) {
            String line = mapLines.get(r);
            for (int c = 0; c < cols; c++) {
                warehouse[r][c] = line.charAt(c);
                if (warehouse[r][c] == '@') {
                    robotR = r;
                    robotC = c;
                }
            }
        }

        // Directions: '^' -> (-1,0), 'v' -> (1,0), '<' -> (0,-1), '>' -> (0,1)
        for (char move : moves.toCharArray()) {
            int dr = 0, dc = 0;
            switch (move) {
                case '^':
                    dr = -1;
                    dc = 0;
                    break;
                case 'v':
                    dr = 1;
                    dc = 0;
                    break;
                case '<':
                    dr = 0;
                    dc = -1;
                    break;
                case '>':
                    dr = 0;
                    dc = 1;
                    break;
            }

            // Attempt to move robot
            int newR = robotR + dr;
            int newC = robotC + dc;

            // Check what is at the new position
            if (warehouse[newR][newC] == '#') {
                // Wall: can't move
                continue;
            } else if (warehouse[newR][newC] == 'O') {
                // There's a box; we need to push
                // Find chain of boxes in that direction
                List<int[]> boxes = new ArrayList<>();
                int curR = newR;
                int curC = newC;
                while (warehouse[curR][curC] == 'O') {
                    boxes.add(new int[] { curR, curC });
                    curR += dr;
                    curC += dc;
                }
                // curR, curC is now the cell after the last box in that chain
                // Check if we can move all boxes one step forward
                if (warehouse[curR][curC] == '#' || warehouse[curR][curC] == '@') {
                    // Can't push into wall or robot (robot won't be there at the end, but let's be
                    // consistent)
                    // Actually the robot won't be in front, but just in case check for walls or O
                    // (if chain continues)
                    if (warehouse[curR][curC] != '.' && warehouse[curR][curC] != '@') {
                        // If it's not '.', we can't move
                        continue;
                    }
                    // If it were '@', that would be strange since there's only one robot. But let's
                    // just continue logic
                }
                if (warehouse[curR][curC] == 'O') {
                    // More boxes ahead that we didn't account for (though we accounted by loop)
                    // Actually we ended the loop when we saw a non 'O', so we can't have O here
                    // again
                    continue;
                }
                if (warehouse[curR][curC] == '#') {
                    // Can't push into wall
                    continue;
                }

                // If it's '.' or '@', we can move (actually '@' won't appear ahead because
                // there's only one robot)
                // Move all boxes forward
                // Move from the last box in the chain to the first, to avoid overwriting
                // Start from the end of boxes to move them
                for (int i = boxes.size() - 1; i >= 0; i--) {
                    int br = boxes.get(i)[0];
                    int bc = boxes.get(i)[1];
                    warehouse[br + dr][bc + dc] = 'O';
                    warehouse[br][bc] = '.';
                }
                // Now move the robot
                warehouse[robotR][robotC] = '.';
                robotR = robotR + dr;
                robotC = robotC + dc;
                warehouse[robotR][robotC] = '@';

            } else if (warehouse[newR][newC] == '.') {
                // Just move the robot
                warehouse[robotR][robotC] = '.';
                robotR = newR;
                robotC = newC;
                warehouse[robotR][robotC] = '@';
            }
            // If there's something else unexpected, we ignore or do nothing
        }

        // After all moves, sum up the GPS coordinates of all boxes
        // GPS = 100 * row + col
        int sum = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (warehouse[r][c] == 'O') {
                    sum += (100 * r + c);
                }
            }
        }

        System.out.println(sum);
    }
}
