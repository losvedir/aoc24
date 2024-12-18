package day15part2;

import java.nio.file.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GPTo1Pro {
    public static void main(String[] args) throws IOException {
        // Read all lines from input file
        List<String> lines = Files.readAllLines(Paths.get("input/day15.txt"));

        // Separate map and moves as in part 1
        int mapWidth = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
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
                index++;
                break;
            }
            if (line.length() == mapWidth && line.startsWith("#") && line.endsWith("#")) {
                mapLines.add(line);
            } else {
                // If doesn't match map width or after we finish reading map lines
                // we break
                break;
            }
        }

        StringBuilder movesBuilder = new StringBuilder();
        for (; index < lines.size(); index++) {
            String line = lines.get(index).trim();
            if (!line.isEmpty()) {
                movesBuilder.append(line);
            }
        }
        String moves = movesBuilder.toString();

        // Convert the original map to a scaled-up map
        // Each character in the original map becomes 2 chars in the new map
        // '#' -> "##", 'O' -> "[]", '.' -> "..", '@' -> "@."
        int originalRows = mapLines.size();
        int originalCols = mapWidth;

        // The scaled map will have the same number of rows but double the columns
        int scaledCols = originalCols * 2;
        char[][] warehouse = new char[originalRows][scaledCols];

        int robotR = -1;
        int robotC = -1; // Robot column in cell coordinates

        for (int r = 0; r < originalRows; r++) {
            String line = mapLines.get(r);
            int writePos = 0;
            for (int c = 0; c < originalCols; c++) {
                char ch = line.charAt(c);
                String replacement;
                switch (ch) {
                    case '#':
                        replacement = "##";
                        break;
                    case 'O':
                        replacement = "[]";
                        break;
                    case '.':
                        replacement = "..";
                        break;
                    case '@':
                        replacement = "@.";
                        break;
                    default:
                        replacement = ".."; // In case of any unexpected char
                }
                warehouse[r][writePos] = replacement.charAt(0);
                warehouse[r][writePos + 1] = replacement.charAt(1);
                if (ch == '@') {
                    // Robot found
                    robotR = r;
                    robotC = c; // cell coordinate directly from original c
                }
                writePos += 2;
            }
        }

        // Directions: '^' -> (-1,0), 'v' -> (1,0), '<' -> (0,-1), '>' -> (0,1)
        // Robot moves in cell units.
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

            int newR = robotR + dr;
            int newC = robotC + dc;

            // Check boundaries and obstacles
            // Extract the cell type at (newR, newC)
            if (!inBounds(newR, newC, warehouse)) {
                // Out of bounds means can't move
                continue;
            }

            char[] newCell = cellChars(warehouse, newR, newC);
            if (isWall(newCell)) {
                // Wall: can't move
                continue;
            } else if (isBox(newCell)) {
                // There's a box chain. Attempt to push
                List<int[]> boxes = new ArrayList<>();
                int chainR = newR;
                int chainC = newC;
                while (inBounds(chainR, chainC, warehouse) && isBox(cellChars(warehouse, chainR, chainC))) {
                    boxes.add(new int[] { chainR, chainC });
                    chainR += dr;
                    chainC += dc;
                }
                // Now chainR, chainC is cell after last box
                if (!inBounds(chainR, chainC, warehouse)) {
                    // Can't push out of bounds
                    continue;
                }
                char[] afterBoxCell = cellChars(warehouse, chainR, chainC);
                if (isWall(afterBoxCell) || isBox(afterBoxCell)) {
                    // Can't push into wall or another box
                    continue;
                }
                // If it's empty or robot cell (robot cell won't appear here),
                // we can push
                // Move boxes from last to first
                for (int i = boxes.size() - 1; i >= 0; i--) {
                    int br = boxes.get(i)[0];
                    int bc = boxes.get(i)[1];
                    setCell(warehouse, br + dr, bc + dc, cellChars(warehouse, br, bc));
                    setCell(warehouse, br, bc, new char[] { '.', '.' });
                }
                // Move robot
                setCell(warehouse, robotR, robotC, new char[] { '.', '.' });
                robotR = newR;
                robotC = newC;
                setCell(warehouse, robotR, robotC, new char[] { '@', '.' });
            } else if (isEmpty(newCell)) {
                // Just move the robot
                setCell(warehouse, robotR, robotC, new char[] { '.', '.' });
                robotR = newR;
                robotC = newC;
                setCell(warehouse, robotR, robotC, new char[] { '@', '.' });
            }
            // Ignore other unexpected cells, if any
        }

        // After all moves, sum up the GPS coordinates of all boxes
        // GPS = 100 * row + column_of_'['
        // We'll directly scan the warehouse array for '[' chars that indicate boxes.
        int sum = 0;
        for (int r = 0; r < warehouse.length; r++) {
            for (int c = 0; c < warehouse[r].length; c++) {
                if (warehouse[r][c] == '[') {
                    // Found a box
                    // row = r, col = c (this is character-based index)
                    sum += (100 * r + c);
                }
            }
        }

        System.out.println(sum);
    }

    private static boolean inBounds(int r, int c, char[][] warehouse) {
        return r >= 0 && r < warehouse.length && (2 * c + 1) < warehouse[0].length && c >= 0;
    }

    private static char[] cellChars(char[][] warehouse, int r, int c) {
        return new char[] { warehouse[r][2 * c], warehouse[r][2 * c + 1] };
    }

    private static void setCell(char[][] warehouse, int r, int c, char[] cellContent) {
        warehouse[r][2 * c] = cellContent[0];
        warehouse[r][2 * c + 1] = cellContent[1];
    }

    private static boolean isWall(char[] cell) {
        return cell[0] == '#' && cell[1] == '#';
    }

    private static boolean isBox(char[] cell) {
        return cell[0] == '[' && cell[1] == ']';
    }

    private static boolean isEmpty(char[] cell) {
        return cell[0] == '.' && cell[1] == '.';
    }
}
