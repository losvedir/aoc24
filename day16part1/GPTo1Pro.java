package day16part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This solution attempts to find the path from 'S' to 'E' through a maze,
 * minimizing the cost where:
 * - Moving forward costs 1 point.
 * - Turning 90 degrees clockwise or counterclockwise costs 1000 points.
 * The reindeer starts at the 'S' tile facing East.
 *
 * We model this as a graph search problem, where each state consists of:
 * - Current position (row, col)
 * - Current direction the reindeer is facing
 *
 * Edges between states represent either:
 * - Moving forward one step in the current direction if not blocked (cost 1)
 * - Rotating left or right 90 degrees in place (cost 1000)
 *
 * We use a priority queue (Dijkstra’s algorithm) to find the minimal-cost path.
 */
public class GPTo1Pro {
    enum Direction {
        NORTH(-1, 0),
        EAST(0, 1),
        SOUTH(1, 0),
        WEST(0, -1);

        final int dr;
        final int dc;

        Direction(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
        }

        Direction turnLeft() {
            switch (this) {
                case NORTH:
                    return WEST;
                case WEST:
                    return SOUTH;
                case SOUTH:
                    return EAST;
                case EAST:
                    return NORTH;
            }
            throw new IllegalStateException("Unknown direction");
        }

        Direction turnRight() {
            switch (this) {
                case NORTH:
                    return EAST;
                case EAST:
                    return SOUTH;
                case SOUTH:
                    return WEST;
                case WEST:
                    return NORTH;
            }
            throw new IllegalStateException("Unknown direction");
        }
    }

    static class State implements Comparable<State> {
        int r, c;
        Direction d;
        long cost;

        State(int r, int c, Direction d, long cost) {
            this.r = r;
            this.c = c;
            this.d = d;
            this.cost = cost;
        }

        @Override
        public int compareTo(State o) {
            return Long.compare(this.cost, o.cost);
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day16.txt"));
        int rows = lines.size();
        int cols = lines.get(0).length();

        char[][] grid = new char[rows][cols];
        int startR = -1, startC = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = lines.get(i).charAt(j);
                if (grid[i][j] == 'S') {
                    startR = i;
                    startC = j;
                }
            }
        }

        // We'll run Dijkstra over states (r,c,d).
        // Distances can be stored in a map or 3D array. We'll use a HashMap since the
        // grid could be large.
        // But directions are only 4, so we can store in a 3D array: dist[r][c][d].
        // Distances default to a large number.

        long[][][] dist = new long[rows][cols][4];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Arrays.fill(dist[r][c], Long.MAX_VALUE);
            }
        }

        // Starting state: at S, facing EAST
        Direction startDirection = Direction.EAST;
        dist[startR][startC][startDirection.ordinal()] = 0;
        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.add(new State(startR, startC, startDirection, 0));

        long answer = Long.MAX_VALUE;

        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (cur.cost > dist[cur.r][cur.c][cur.d.ordinal()]) {
                continue; // Already found a better path
            }

            if (grid[cur.r][cur.c] == 'E') {
                // Found the end
                answer = Math.min(answer, cur.cost);
                break; // Because we use Dijkstra, this is the minimal cost
            }

            // Try going forward
            int nr = cur.r + cur.d.dr;
            int nc = cur.c + cur.d.dc;
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] != '#') {
                // We can move forward
                long newCost = cur.cost + 1;
                if (newCost < dist[nr][nc][cur.d.ordinal()]) {
                    dist[nr][nc][cur.d.ordinal()] = newCost;
                    pq.add(new State(nr, nc, cur.d, newCost));
                }
            }

            // Try turning left
            Direction left = cur.d.turnLeft();
            long leftCost = cur.cost + 1000;
            if (leftCost < dist[cur.r][cur.c][left.ordinal()]) {
                dist[cur.r][cur.c][left.ordinal()] = leftCost;
                pq.add(new State(cur.r, cur.c, left, leftCost));
            }

            // Try turning right
            Direction right = cur.d.turnRight();
            long rightCost = cur.cost + 1000;
            if (rightCost < dist[cur.r][cur.c][right.ordinal()]) {
                dist[cur.r][cur.c][right.ordinal()] = rightCost;
                pq.add(new State(cur.r, cur.c, right, rightCost));
            }
        }

        System.out.println(answer);
    }
}
