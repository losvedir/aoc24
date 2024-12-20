package day16part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Gemini2Advanced {

    private static final int TURN_COST = 1000;
    private static final int MOVE_COST = 1;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day16.txt"));
            char[][] grid = parseGrid(lines);
            int startRow = -1, startCol = -1;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == 'S') {
                        startRow = i;
                        startCol = j;
                        break;
                    }
                }
            }

            int bestTiles = countBestTiles(grid, startRow, startCol);
            System.out.println("Number of tiles on best paths: " + bestTiles);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static char[][] parseGrid(List<String> lines) {
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    private static int countBestTiles(char[][] grid, int startRow, int startCol) {
        int rows = grid.length;
        int cols = grid[0].length;

        int lowestScore = findLowestScore(grid, startRow, startCol);

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.score));
        pq.offer(new State(startRow, startCol, Direction.EAST, 0));

        Set<String> bestTiles = new HashSet<>();
        Map<String, Integer> visited = new HashMap<>();

        while (!pq.isEmpty()) {
            State current = pq.poll();

            String stateKey = current.row + "," + current.col + "," + current.direction;

            if (visited.containsKey(stateKey) && visited.get(stateKey) <= current.score)
                continue;
            visited.put(stateKey, current.score);

            if (current.score <= lowestScore) {
                bestTiles.add(current.row + "," + current.col);
            } else
                continue;

            if (grid[current.row][current.col] == 'E')
                if (current.score == lowestScore)
                    continue;
                else
                    break;

            // Move forward
            int nextRow = current.row + current.direction.rowOffset;
            int nextCol = current.col + current.direction.colOffset;
            if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols && grid[nextRow][nextCol] != '#') {
                pq.offer(new State(nextRow, nextCol, current.direction, current.score + MOVE_COST));
            }

            // Turn clockwise
            Direction clockwise = current.direction.turnClockwise();
            pq.offer(new State(current.row, current.col, clockwise, current.score + TURN_COST));

            // Turn counterclockwise
            Direction counterClockwise = current.direction.turnCounterClockwise();
            pq.offer(new State(current.row, current.col, counterClockwise, current.score + TURN_COST));
        }
        return bestTiles.size();
    }

    private static int findLowestScore(char[][] grid, int startRow, int startCol) {
        int rows = grid.length;
        int cols = grid[0].length;

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.score));
        pq.offer(new State(startRow, startCol, Direction.EAST, 0));

        Map<String, Integer> visited = new HashMap<>();

        while (!pq.isEmpty()) {
            State current = pq.poll();

            String stateKey = current.row + "," + current.col + "," + current.direction;
            if (visited.containsKey(stateKey) && visited.get(stateKey) <= current.score) {
                continue;
            }
            visited.put(stateKey, current.score);

            if (grid[current.row][current.col] == 'E') {
                return current.score;
            }

            // Move forward
            int nextRow = current.row + current.direction.rowOffset;
            int nextCol = current.col + current.direction.colOffset;
            if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols && grid[nextRow][nextCol] != '#') {
                pq.offer(new State(nextRow, nextCol, current.direction, current.score + MOVE_COST));
            }

            // Turn clockwise
            Direction clockwise = current.direction.turnClockwise();
            pq.offer(new State(current.row, current.col, clockwise, current.score + TURN_COST));

            // Turn counterclockwise
            Direction counterClockwise = current.direction.turnCounterClockwise();
            pq.offer(new State(current.row, current.col, counterClockwise, current.score + TURN_COST));
        }

        return -1;
    }

    private enum Direction {
        NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

        final int rowOffset;
        final int colOffset;

        Direction(int colOffset, int rowOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        Direction turnClockwise() {
            return values()[(ordinal() + 1) % 4];
        }

        Direction turnCounterClockwise() {
            return values()[(ordinal() + 3) % 4];
        }
    }

    private static class State {
        int row;
        int col;
        Direction direction;
        int score;

        State(int row, int col, Direction direction, int score) {
            this.row = row;
            this.col = col;
            this.direction = direction;
            this.score = score;
        }
    }
}
