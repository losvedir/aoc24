package day15part1;

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
            // We'll assume the map is a rectangular grid fully surrounded by '#' (at least
            // top and bottom lines)
            // and that after the map lines, there's at least one line containing moves.
            // The input may have multiple lines of moves that need to be concatenated.

            int mapHeight = 0;
            // The map typically is fully surrounded by walls, so let's detect when the map
            // ends:
            // We'll assume the map lines start immediately from the top of the file and
            // continue until we hit
            // a line that does not fit the pattern (for example, no '#' border or a line
            // that is shorter or different).
            // The problem's samples show the map as a rectangular block of lines starting
            // with '#',
            // and then moves lines follow (which contain '^', 'v', '<', '>').
            // A robust approach is:
            // - Read lines until we hit a line that does not contain '#' or '.' or 'O' or
            // '@' in a valid pattern.
            // - The first line not consistent with a map line will be considered part of
            // moves.

            // A simple heuristic: The map lines contain only the characters { '#', '.',
            // 'O', '@' } and possibly walls all around.
            // Moves lines contain '^','v','<','>', maybe spaces. Let's break when we find a
            // line that contains any move character.

            int lineIndex = 0;
            ArrayList<String> mapLines = new ArrayList<>();
            for (; lineIndex < lines.size(); lineIndex++) {
                String l = lines.get(lineIndex);
                // If we detect a move character in the line, break
                if (l.matches(".*[\\^v<>].*")) {
                    break;
                }
                // Otherwise, it's considered part of the map
                mapLines.add(l);
            }

            // Now, the rest of the lines from lineIndex onward form the moves
            StringBuilder movesBuilder = new StringBuilder();
            for (; lineIndex < lines.size(); lineIndex++) {
                String mLine = lines.get(lineIndex);
                movesBuilder.append(mLine);
            }
            String moves = movesBuilder.toString().replaceAll("\\s+", ""); // remove any whitespace

            char[][] map = new char[mapLines.size()][];
            for (int i = 0; i < mapLines.size(); i++) {
                map[i] = mapLines.get(i).toCharArray();
            }

            // Find the robot position
            int robotRow = -1;
            int robotCol = -1;
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[r].length; c++) {
                    if (map[r][c] == '@') {
                        robotRow = r;
                        robotCol = c;
                        break;
                    }
                }
                if (robotRow != -1)
                    break;
            }

            // Directions
            int[] dRow = { -1, 1, 0, 0 }; // up, down, left, right
            int[] dCol = { 0, 0, -1, 1 };
            // We'll map '^'->0, 'v'->1, '<'->2, '>'->3
            // a method to get direction index:
            java.util.Map<Character, Integer> dirMap = new java.util.HashMap<>();
            dirMap.put('^', 0);
            dirMap.put('v', 1);
            dirMap.put('<', 2);
            dirMap.put('>', 3);

            // Simulate each move
            for (int i = 0; i < moves.length(); i++) {
                char m = moves.charAt(i);
                if (!dirMap.containsKey(m))
                    continue; // ignore any unexpected chars
                int dir = dirMap.get(m);
                int nr = robotRow + dRow[dir];
                int nc = robotCol + dCol[dir];

                // Check what is at (nr, nc)
                if (map[nr][nc] == '#') {
                    // Robot can't move (blocked by wall)
                    continue;
                } else if (map[nr][nc] == '.') {
                    // Just move robot
                    map[robotRow][robotCol] = '.';
                    map[nr][nc] = '@';
                    robotRow = nr;
                    robotCol = nc;
                } else if (map[nr][nc] == 'O') {
                    // Need to push boxes
                    // We'll try to push the chain of boxes in this direction.
                    // Collect all consecutive boxes in that direction starting from (nr,nc).
                    java.util.List<int[]> boxes = new ArrayList<>();
                    int br = nr;
                    int bc = nc;
                    while (map[br][bc] == 'O') {
                        boxes.add(new int[] { br, bc });
                        br += dRow[dir];
                        bc += dCol[dir];
                    }
                    // Now, (br, bc) is the cell after the last box. Check if it's free to move
                    // into.
                    if (map[br][bc] == '.') {
                        // We can push
                        // Move the last box into (br, bc)
                        map[br][bc] = 'O';
                        // Move all previous boxes one step forward
                        for (int b = boxes.size() - 1; b >= 1; b--) {
                            int[] curBox = boxes.get(b);
                            int[] prevBox = boxes.get(b - 1);
                            map[curBox[0]][curBox[1]] = '.'; // this will be overwritten eventually but let's keep
                                                             // consistent
                            map[prevBox[0]][prevBox[1]] = '.'; // we will move them forward
                            // The box at index b moves to where box b-1 was
                            int nrBox = prevBox[0] + dRow[dir];
                            int ncBox = prevBox[1] + dCol[dir];
                            // Actually, it's easier to do from front to back:
                            // Let's rewrite pushing logic more clearly:
                        }

                        // Let's rewrite pushing logic more simply:
                        // We have a chain of boxes. We know the last cell after the chain is '.'
                        // We push from the far end to avoid overwriting:

                        // Clear old positions of boxes
                        for (int[] boxPos : boxes) {
                            map[boxPos[0]][boxPos[1]] = '.';
                        }

                        // Place boxes in new positions shifted by one step
                        for (int[] boxPos : boxes) {
                            int newR = boxPos[0] + dRow[dir];
                            int newC = boxPos[1] + dCol[dir];
                            map[newR][newC] = 'O';
                        }

                        // Move robot
                        map[robotRow][robotCol] = '.';
                        map[nr][nc] = '@';
                        robotRow = nr;
                        robotCol = nc;

                    } else {
                        // If the next cell after the boxes is not '.' (it could be '#' or '@' or 'O'),
                        // we can't move
                        // Actually '@' won't be encountered in the chain, but '#' or out of bounds or
                        // 'O' means blocked.
                        // Just do nothing.
                        continue;
                    }
                } else {
                    // If it's '@' or something unexpected, just no movement.
                    // There should never be another '@' as there's only one robot.
                    continue;
                }
            }

            // After all moves, sum the GPS coordinates of all boxes.
            // GPS coordinate = 100 * rowIndex + colIndex
            int sum = 0;
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[r].length; c++) {
                    if (map[r][c] == 'O') {
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
