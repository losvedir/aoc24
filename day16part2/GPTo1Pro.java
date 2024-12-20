package day16part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Part 2:
 * 
 * In part 1, we found the minimum cost to get from S to E. Now, we need to
 * determine which tiles are
 * on ANY of the best (lowest-cost) paths.
 * 
 * To do this:
 * 1. Compute distFromS[r][c][d]: the minimal cost of reaching each state (tile
 * + direction) from S.
 * 2. Compute distFromE[r][c][d]: the minimal cost of reaching each state (tile
 * + direction) from E,
 * but backwards. Essentially, this is the minimal cost to get from (r,c,d) to E
 * if we consider the
 * same movement and turn rules but in reverse.
 * 
 * For distFromE, we consider E as the "start" with all directions having 0
 * cost. We run a Dijkstra-like
 * algorithm with reversed transitions:
 * - In forward mode (from S), from (r,c,d) we can:
 * forward: (r+dr, c+dc, d) cost+1
 * turnLeft: (r,c,d.turnLeft()) cost+1000
 * turnRight: (r,c,d.turnRight()) cost+1000
 * 
 * For backward mode (from E), we reverse the edges:
 * - A forward move forward: (r,c,d) <- (r - dr, c - dc, d) if walkable, cost+1
 * - A forward turnLeft in forward mode is reversed by turnRight in backward
 * mode, and vice versa:
 * backward from (r,c,d):
 * could come from (r,c,d.turnRight()) with cost+1000 (because forward that was
 * a turnLeft)
 * could come from (r,c,d.turnLeft()) with cost+1000 (because forward that was a
 * turnRight)
 * 
 * 3. Once we have distFromS and distFromE, the bestCost is the minimal cost to
 * reach E from S found in part 1.
 * 
 * 4. A tile (r,c) is on a best path if there exists at least one direction d
 * such that:
 * distFromS[r][c][d] + distFromE[r][c][d] == bestCost
 * 
 * 5. Count how many such tiles and print that count.
 */
public class GPTo1Pro {
    enum Direction {
        NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

        final int dr, dc;

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
            throw new IllegalStateException();
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
            throw new IllegalStateException();
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
        int endR = -1, endC = -1;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char ch = lines.get(i).charAt(j);
                grid[i][j] = ch;
                if (ch == 'S') {
                    startR = i;
                    startC = j;
                }
                if (ch == 'E') {
                    endR = i;
                    endC = j;
                }
            }
        }

        // Run forward Dijkstra from S:
        long[][][] distFromS = runForwardDijkstra(grid, startR, startC);

        // The best cost to reach E from S:
        long bestCost = Long.MAX_VALUE;
        for (Direction d : Direction.values()) {
            bestCost = Math.min(bestCost, distFromS[endR][endC][d.ordinal()]);
        }

        // Run backward Dijkstra from E:
        long[][][] distFromE = runBackwardDijkstra(grid, endR, endC);

        // Now determine which tiles are on any best path:
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '#')
                    continue; // wall can't be on a path
                // Check if any direction d yields distFromS[r][c][d] + distFromE[r][c][d] ==
                // bestCost
                for (Direction d : Direction.values()) {
                    long ds = distFromS[r][c][d.ordinal()];
                    long de = distFromE[r][c][d.ordinal()];
                    if (ds != Long.MAX_VALUE && de != Long.MAX_VALUE && ds + de == bestCost) {
                        count++;
                        break;
                    }
                }
            }
        }

        System.out.println(count);
    }

    private static long[][][] runForwardDijkstra(char[][] grid, int startR, int startC) {
        int rows = grid.length;
        int cols = grid[0].length;

        long[][][] dist = new long[rows][cols][4];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Arrays.fill(dist[r][c], Long.MAX_VALUE);
            }
        }

        // Start facing EAST at S
        dist[startR][startC][Direction.EAST.ordinal()] = 0;
        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.add(new State(startR, startC, Direction.EAST, 0));

        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (cur.cost > dist[cur.r][cur.c][cur.d.ordinal()])
                continue;
            if (grid[cur.r][cur.c] == 'E') {
                // We found a shortest path to E in one particular direction, but we continue
                // to ensure dist arrays are fully computed for all states
            }

            // forward move
            int nr = cur.r + cur.d.dr;
            int nc = cur.c + cur.d.dc;
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] != '#') {
                long newCost = cur.cost + 1;
                if (newCost < dist[nr][nc][cur.d.ordinal()]) {
                    dist[nr][nc][cur.d.ordinal()] = newCost;
                    pq.add(new State(nr, nc, cur.d, newCost));
                }
            }

            // turn left
            Direction left = cur.d.turnLeft();
            long leftCost = cur.cost + 1000;
            if (leftCost < dist[cur.r][cur.c][left.ordinal()]) {
                dist[cur.r][cur.c][left.ordinal()] = leftCost;
                pq.add(new State(cur.r, cur.c, left, leftCost));
            }

            // turn right
            Direction right = cur.d.turnRight();
            long rightCost = cur.cost + 1000;
            if (rightCost < dist[cur.r][cur.c][right.ordinal()]) {
                dist[cur.r][cur.c][right.ordinal()] = rightCost;
                pq.add(new State(cur.r, cur.c, right, rightCost));
            }
        }
        return dist;
    }

    private static long[][][] runBackwardDijkstra(char[][] grid, int endR, int endC) {
        int rows = grid.length;
        int cols = grid[0].length;

        // distFromE[r][c][d]: minimal cost from (r,c,d) to E (backward search)
        long[][][] dist = new long[rows][cols][4];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Arrays.fill(dist[r][c], Long.MAX_VALUE);
            }
        }

        // At E, we can "end" in any direction at 0 cost
        PriorityQueue<State> pq = new PriorityQueue<>();
        for (Direction d : Direction.values()) {
            dist[endR][endC][d.ordinal()] = 0;
            pq.add(new State(endR, endC, d, 0));
        }

        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (cur.cost > dist[cur.r][cur.c][cur.d.ordinal()])
                continue;

            // Backward transitions:
            // 1) Forward step backward:
            // If forward from (r',c',d) leads to (r,c,d), then backward from (r,c,d) leads
            // to (r',c',d).
            // (r,c) = (r'+d.dr, c'+d.dc), so (r',c')=(r-d.dr,c-d.dc)
            int pr = cur.r - cur.d.dr;
            int pc = cur.c - cur.d.dc;
            if (pr >= 0 && pr < rows && pc >= 0 && pc < cols && grid[pr][pc] != '#') {
                long newCost = cur.cost + 1;
                if (newCost < dist[pr][pc][cur.d.ordinal()]) {
                    dist[pr][pc][cur.d.ordinal()] = newCost;
                    pq.add(new State(pr, pc, cur.d, newCost));
                }
            }

            // 2) Turning backward:
            // Forward left turn from (r,c,d') to (r,c,d) implies backward from (r,c,d) we
            // can turn right to get (r,c,d').
            // If d'=d.turnRight() then d = d'.turnLeft().
            // So from (r,c,d) backward we can go to (r,c,d.turnRight()) with cost+1000
            // (reverse of forward left turn)
            Direction dturnRight = cur.d.turnRight();
            long costTurn = cur.cost + 1000;
            if (costTurn < dist[cur.r][cur.c][dturnRight.ordinal()]) {
                dist[cur.r][cur.c][dturnRight.ordinal()] = costTurn;
                pq.add(new State(cur.r, cur.c, dturnRight, costTurn));
            }

            // Similarly for forward right turn reverse:
            // forward right turn from (r,c,d'') to (r,c,d) means backward we turn left.
            Direction dturnLeft = cur.d.turnLeft();
            if (costTurn < dist[cur.r][cur.c][dturnLeft.ordinal()]) {
                dist[cur.r][cur.c][dturnLeft.ordinal()] = costTurn;
                pq.add(new State(cur.r, cur.c, dturnLeft, costTurn));
            }
        }

        return dist;
    }

}
