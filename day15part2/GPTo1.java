package day15part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GPTo1 {
    public static void main(String[] args) {
        try {
            // Read the input file
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("input/day15.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }

            // Separate map lines from moves lines
            int lineIndex = 0;
            ArrayList<String> mapLinesOriginal = new ArrayList<>();
            for (; lineIndex < lines.size(); lineIndex++) {
                String l = lines.get(lineIndex);
                // If we detect a move character in the line, stop
                if (l.matches(".*[\\^v<>].*")) {
                    break;
                }
                mapLinesOriginal.add(l);
            }

            // The rest are moves
            StringBuilder movesBuilder = new StringBuilder();
            for (; lineIndex < lines.size(); lineIndex++) {
                movesBuilder.append(lines.get(lineIndex));
            }
            String moves = movesBuilder.toString().replaceAll("\\s+", "");

            // Convert original map to new map:
            // Each character in original:
            // '#' -> "##"
            // 'O' -> "[]"
            // '.' -> ".."
            // '@' -> "@."
            int originalRows = mapLinesOriginal.size();
            int originalCols = mapLinesOriginal.get(0).length();
            char[][] newMap = new char[originalRows][originalCols * 2];

            for (int r = 0; r < originalRows; r++) {
                char[] rowChars = mapLinesOriginal.get(r).toCharArray();
                int nc = 0;
                for (int c = 0; c < originalCols; c++) {
                    char ch = rowChars[c];
                    switch (ch) {
                        case '#':
                            newMap[r][nc] = '#';
                            newMap[r][nc + 1] = '#';
                            break;
                        case 'O':
                            newMap[r][nc] = '[';
                            newMap[r][nc + 1] = ']';
                            break;
                        case '.':
                            newMap[r][nc] = '.';
                            newMap[r][nc + 1] = '.';
                            break;
                        case '@':
                            newMap[r][nc] = '@';
                            newMap[r][nc + 1] = '.';
                            break;
                        default:
                            // Unexpected character
                            newMap[r][nc] = ch;
                            newMap[r][nc + 1] = '.';
                            break;
                    }
                    nc += 2;
                }
            }

            // Find robot position (the cell with '@')
            int robotRow = -1;
            int robotCol = -1;
            for (int r = 0; r < newMap.length; r++) {
                for (int c = 0; c < newMap[r].length; c++) {
                    if (newMap[r][c] == '@') {
                        robotRow = r;
                        robotCol = c;
                        break;
                    }
                }
                if (robotRow != -1)
                    break;
            }

            // Direction mappings
            // '^' (up): row-1, col no change
            // 'v' (down): row+1, col no change
            // '<' (left): col-2
            // '>' (right): col+2
            java.util.Map<Character, int[]> dirMap = new java.util.HashMap<>();
            dirMap.put('^', new int[] { -1, 0 });
            dirMap.put('v', new int[] { 1, 0 });
            dirMap.put('<', new int[] { 0, -2 });
            dirMap.put('>', new int[] { 0, 2 });

            // A helper to check if cell is inside map and not out of range
            java.util.function.BiPredicate<Integer, Integer> inBounds = (rr, cc) -> rr >= 0 && rr < newMap.length
                    && cc >= 0 && cc < newMap[rr].length;

            // A helper to determine if a cell is floor: floor cells are '.' and not part of
            // a box.
            // Actually, floor can be '.' or might appear next to '@'. For pushing logic, we
            // just need to distinguish:
            // '#' is wall
            // '[' and ']' form boxes
            // '@' is robot
            // '.' is empty floor
            // Since '@' always followed by '.' in its cell pair, treat '@' cell as robot
            // and '.' after it as floor.

            // Check if cell is a box cell: '[' or ']'
            java.util.function.BiPredicate<Integer, Integer> isBoxCell = (rr, cc) -> inBounds.test(rr, cc)
                    && (newMap[rr][cc] == '[' || newMap[rr][cc] == ']');

            // Check if cell is wall
            java.util.function.BiPredicate<Integer, Integer> isWall = (rr, cc) -> inBounds.test(rr, cc)
                    && newMap[rr][cc] == '#';

            // Check if cell is floor (or empty): '.' is empty floor. '@' is robot.
            java.util.function.BiPredicate<Integer, Integer> isEmptyCell = (rr, cc) -> inBounds.test(rr, cc)
                    && newMap[rr][cc] == '.';

            // Find a box starting from a cell that we know is part of a box:
            // Return the top-left coordinate of the box (where '[' is located).
            java.util.function.BiFunction<Integer, Integer, int[]> getBoxCoordinates = (rr, cc) -> {
                if (newMap[rr][cc] == '[') {
                    return new int[] { rr, cc };
                } else {
                    // If it's ']', the '[' must be at cc-1
                    return new int[] { rr, cc - 1 };
                }
            };

            // To push boxes, we must identify a chain of boxes in the direction of
            // movement.
            // Steps to identify the chain:
            // 1. Identify the direction (dr,dc)
            // 2. Starting from the cell in front of the robot, identify the box and add it
            // to the chain.
            // For horizontal push (dc != 0):
            // Move further in the same direction by increments of 2 columns to find
            // consecutive boxes.
            // For vertical push (dr != 0):
            // Use the same (startCols) of the first box and move vertically to find boxes
            // aligned in the same columns.
            // After identifying the chain, check if pushing is possible:
            // That means checking the next cell(s) after the last box in the chain are
            // empty.
            // For horizontal push, we must ensure that the two cells that will be occupied
            // by the last box after pushing
            // are currently empty.
            // For vertical push, similarly check the two cells in the next row after the
            // last box.

            // Pushing the chain:
            // Move all boxes one step in the direction. For horizontal: 2 columns; for
            // vertical: 1 row.

            for (char m : moves.toCharArray()) {
                if (!dirMap.containsKey(m))
                    continue; // skip unexpected chars
                int dr = dirMap.get(m)[0];
                int dc = dirMap.get(m)[1];

                int nr = robotRow + dr;
                int nc = robotCol + dc;

                // Check boundaries
                if (!inBounds.test(nr, nc)) {
                    // Can't move out of bounds
                    continue;
                }

                if (isWall.test(nr, nc)) {
                    // Robot blocked by wall
                    continue;
                }

                if (isEmptyCell.test(nr, nc)) {
                    // Move robot to empty cell
                    newMap[robotRow][robotCol] = '.'; // old robot pos becomes '.'
                    // Remember robot occupies '@' and next cell is '.' originally. We only stored
                    // '@' at robotCol.
                    // We should mark old robot '@' cell as '.' now.
                    newMap[nr][nc] = '@';
                    robotRow = nr;
                    robotCol = nc;
                    continue;
                }

                if (newMap[nr][nc] == '@') {
                    // Another robot cell? Should not happen, only one robot.
                    continue;
                }

                if (isBoxCell.test(nr, nc)) {
                    // Need to push box(es)
                    // Identify the chain of boxes
                    int[] boxStart = getBoxCoordinates.apply(nr, nc);
                    int boxR = boxStart[0];
                    int boxC = boxStart[1];

                    // Determine how to find the chain:
                    java.util.List<int[]> boxChain = new ArrayList<>();
                    boxChain.add(new int[] { boxR, boxC });

                    if (dr == 0 && dc != 0) {
                        // Horizontal push
                        // Keep going in direction (dc of ±2) to find consecutive boxes directly next to
                        // each other
                        int scanR = boxR;
                        int scanC = boxC + dc;
                        while (inBounds.test(scanR, scanC) && inBounds.test(scanR, scanC + 1)
                                && newMap[scanR][scanC] == '[' && newMap[scanR][scanC + 1] == ']') {
                            boxChain.add(new int[] { scanR, scanC });
                            scanC += dc;
                        }
                    } else if (dr != 0 && dc == 0) {
                        // Vertical push
                        // The box is at (boxR, boxC) and (boxR, boxC+1)
                        // Keep going down/up along the same columns to find more boxes aligned
                        int baseC = boxC;
                        int scanR = boxR + dr;
                        while (inBounds.test(scanR, baseC) && inBounds.test(scanR, baseC + 1)
                                && newMap[scanR][baseC] == '[' && newMap[scanR][baseC + 1] == ']') {
                            boxChain.add(new int[] { scanR, baseC });
                            scanR += dr;
                        }
                    }

                    // Now boxChain contains all boxes in the chain.
                    // Check if we can push them (is the next spot free?)
                    // Next spot means after the last box in chain in the direction of movement
                    int[] lastBox = boxChain.get(boxChain.size() - 1);
                    int checkR = lastBox[0] + dr;
                    int checkC = lastBox[1] + dc;
                    // After pushing, each box moves one step in direction. That means for
                    // horizontal push we move by 2 columns (dc)
                    // Actually, dc is already ±2 for horizontal and ±0 for vertical. For vertical,
                    // dr is ±1.
                    // For horizontal push, we must check two cells (checkC and checkC+1) are empty.
                    // For vertical push, we must also check (checkR, checkC+1).

                    boolean canPush = true;
                    if (!inBounds.test(checkR, checkC) || !inBounds.test(checkR, checkC + 1)) {
                        canPush = false;
                    } else {
                        // Check if target cells are '.' (empty)
                        if (newMap[checkR][checkC] != '.' || newMap[checkR][checkC + 1] != '.') {
                            canPush = false;
                        }
                    }

                    if (!canPush) {
                        // Can't move
                        continue;
                    }

                    // We can push
                    // Move all boxes from the back of the chain to the front, clearing old
                    // positions
                    // First, clear old positions
                    for (int[] bpos : boxChain) {
                        int rr = bpos[0];
                        int cc = bpos[1];
                        // box occupies (rr,cc)='[' and (rr,cc+1)=']'
                        newMap[rr][cc] = '.';
                        newMap[rr][cc + 1] = '.';
                    }

                    // Place them in new positions
                    for (int[] bpos : boxChain) {
                        int rr = bpos[0] + dr;
                        int cc = bpos[1] + dc;
                        newMap[rr][cc] = '[';
                        newMap[rr][cc + 1] = ']';
                    }

                    // Move robot
                    newMap[robotRow][robotCol] = '.';
                    newMap[nr][nc] = '@';
                    robotRow = nr;
                    robotCol = nc;
                }
            }

            // After moves done, sum the GPS coordinates of all boxes.
            // A box at row r and '[' at column c -> GPS = 100*r + c
            int sum = 0;
            for (int r = 0; r < newMap.length; r++) {
                for (int c = 0; c < newMap[r].length - 1; c++) {
                    if (newMap[r][c] == '[' && newMap[r][c + 1] == ']') {
                        sum += 100 * r + c;
                    }
                }
            }

            System.out.println(sum);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
