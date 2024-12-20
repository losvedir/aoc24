package day16part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GeminiFlash {

    private static final int FORWARD_COST = 1;
    private static final int TURN_COST = 1000;

    private static class State {
        int row;
        int col;
        int dir; // 0: East, 1: South, 2: West, 3: North
        int cost;

        public State(int row, int col, int dir, int cost) {
            this.row = row;
            this.col = col;
            this.dir = dir;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            State state = (State) o;
            return row == state.row && col == state.col && dir == state.dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, dir);
        }
    }

    public static int solve(List<String> map) {
        int rows = map.size();
        int cols = map.get(0).length();

        int startRow = -1;
        int startCol = -1;
        int endRow = -1;
        int endCol = -1;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map.get(r).charAt(c) == 'S') {
                    startRow = r;
                    startCol = c;
                } else if (map.get(r).charAt(c) == 'E') {
                    endRow = r;
                    endCol = c;
                }
            }
        }

        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost));
        queue.offer(new State(startRow, startCol, 0, 0));

        Set<State> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.row == endRow && current.col == endCol) {
                return current.cost;
            }

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
            int[] dr = directions[current.dir];
            int nextRow = current.row + dr[0];
            int nextCol = current.col + dr[1];

            if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols
                    && map.get(nextRow).charAt(nextCol) != '#') {
                queue.offer(new State(nextRow, nextCol, current.dir, current.cost + FORWARD_COST));
            }

            queue.offer(new State(current.row, current.col, (current.dir + 1) % 4, current.cost + TURN_COST));
            queue.offer(new State(current.row, current.col, (current.dir + 3) % 4, current.cost + TURN_COST));
        }

        return -1; // No path found
    }

    public static void main(String[] args) throws IOException {
        List<String> map = Files.readAllLines(Paths.get("input/day16.txt"));
        int result = solve(map);
        System.out.println(result);
    }
}
