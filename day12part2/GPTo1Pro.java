// package day12part2;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.*;

// /**
//  * This solution builds on part 1. The main difference is in how we count the
//  * price.
//  * Instead of using perimeter (simply counting all boundary edges), we must
//  * count the number of "sides".
//  * A "side" is defined as a straight continuous section of fence along the
//  * boundary of the region's polygon(s).
//  * 
//  * The approach:
//  * 1. Parse the input into a grid.
//  * 2. Find regions (connected components of the same plant type).
//  * 3. For each region:
//  * - Identify all boundary edges. Each boundary edge is a segment between two
//  * "corners" on the grid.
//  * - Construct a graph of these corner vertices and the edges between them.
//  * - This graph will form one or more closed polygonal loops (one outer boundary
//  * and possibly inner boundaries representing holes).
//  * - Extract each polygon loop.
//  * - Count the number of sides in each loop by merging consecutive collinear
//  * edges.
//  * Actually, to find the sides:
//  * - Determine the direction (horizontal or vertical) of each edge in the loop.
//  * - Walk the loop and count direction changes. Each direction change
//  * corresponds to a new side.
//  * Since it's a closed loop, we also consider the transition from the last edge
//  * back to the first.
//  *
//  * Steps for counting sides for a loop:
//  * Let the loop have edges E0, E1, ..., E(n-1), and their directions be D0, D1,
//  * ..., D(n-1).
//  * sides = 0
//  * For each vertex i (comparing edge i and edge (i+1)%n):
//  * if D(i) != D((i+1)%n) then sides++
//  * This counts how many times direction changes at vertices, which equals the
//  * number of sides.
//  *
//  * Finally, the price for the region = area *
//  * sum_of_sides_over_all_loops_for_that_region.
//  * Add up the prices for all regions.
//  */
// public class GPTo1Pro {
//     private static final int[][] DIRECTIONS = {
//             { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }
//     };

//     private record Cell(int r, int c) {
//     }

//     private record Corner(int r, int c) {
//     }

//     public static void main(String[] args) throws IOException {
//         var lines = Files.readAllLines(Path.of("input/day12small.txt"));
//         if (lines.isEmpty()) {
//             System.out.println(0);
//             return;
//         }

//         int rows = lines.size();
//         int cols = lines.get(0).length();
//         char[][] grid = new char[rows][cols];
//         for (int r = 0; r < rows; r++) {
//             var line = lines.get(r);
//             for (int c = 0; c < cols; c++) {
//                 grid[r][c] = line.charAt(c);
//             }
//         }

//         boolean[][] visited = new boolean[rows][cols];
//         long totalPrice = 0;

//         for (int r = 0; r < rows; r++) {
//             for (int c = 0; c < cols; c++) {
//                 if (!visited[r][c]) {
//                     totalPrice += processRegion(grid, visited, r, c);
//                 }
//             }
//         }

//         System.out.println(totalPrice);
//     }

//     private static long processRegion(char[][] grid, boolean[][] visited, int startR, int startC) {
//         int rows = grid.length;
//         int cols = grid[0].length;
//         char regionChar = grid[startR][startC];

//         // Flood fill to find all cells in this region
//         var stack = new ArrayDeque<Cell>();
//         stack.push(new Cell(startR, startC));
//         visited[startR][startC] = true;
//         var regionCells = new ArrayList<Cell>();

//         while (!stack.isEmpty()) {
//             var cell = stack.pop();
//             regionCells.add(cell);
//             for (var dir : DIRECTIONS) {
//                 int nr = cell.r() + dir[0];
//                 int nc = cell.c() + dir[1];
//                 if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
//                     continue;
//                 if (!visited[nr][nc] && grid[nr][nc] == regionChar) {
//                     visited[nr][nc] = true;
//                     stack.push(new Cell(nr, nc));
//                 }
//             }
//         }

//         int area = regionCells.size();

//         // Identify boundary edges
//         // A boundary edge exists where a side of a cell in the region touches either
//         // out-of-bounds or a cell of a different type.
//         // Each edge can be represented by two corners. Corners have coordinates (row,
//         // col) in a grid of corners.
//         // For cell (r,c), the corners are (r,c), (r,c+1), (r+1,c), (r+1,c+1).

//         // We will store the polygon boundary edges in a graph structure: adjacency map
//         // of Corner -> list of Corner
//         Map<Corner, List<Corner>> adjacency = new HashMap<>();

//         for (var cell : regionCells) {
//             int r = cell.r();
//             int c = cell.c();
//             // top edge
//             if (r == 0 || grid[r - 1][c] != regionChar) {
//                 addEdge(adjacency, new Corner(r, c), new Corner(r, c + 1));
//             }
//             // bottom edge
//             if (r == rows - 1 || grid[r + 1][c] != regionChar) {
//                 addEdge(adjacency, new Corner(r + 1, c), new Corner(r + 1, c + 1));
//             }
//             // left edge
//             if (c == 0 || grid[r][c - 1] != regionChar) {
//                 addEdge(adjacency, new Corner(r, c), new Corner(r + 1, c));
//             }
//             // right edge
//             if (c == cols - 1 || grid[r][c + 1] != regionChar) {
//                 addEdge(adjacency, new Corner(r, c + 1), new Corner(r + 1, c + 1));
//             }
//         }

//         // Now adjacency contains one or more closed loops of corners.
//         // Each loop represents either an outer boundary or a hole boundary.
//         // Find all loops: to do that, pick any unused edge and follow until we get a
//         // closed loop.

//         // To track visited edges, we can track visited vertices and edges as we trace
//         // loops.
//         // However, each vertex should have degree 2 for a properly formed polygon
//         // boundary (assuming well-formed input).
//         // We can just pick an unvisited vertex and follow the cycle.

//         Set<Corner> visitedCornersInLoops = new HashSet<>();
//         int totalSides = 0;

//         for (var startVertex : adjacency.keySet()) {
//             if (!visitedCornersInLoops.contains(startVertex)) {
//                 // find a loop
//                 totalSides += findAndCountSidesInLoop(adjacency, startVertex, visitedCornersInLoops);
//             }
//         }

//         // Price = area * sides
//         return (long) area * totalSides;
//     }

//     private static int findAndCountSidesInLoop(Map<Corner, List<Corner>> adjacency, Corner start, Set<Corner> visited) {
//         // Follow the loop: each vertex has exactly two neighbors for a simple polygon
//         // boundary.
//         // Start at 'start', pick one neighbor, and keep going until we return to start.
//         // We'll record the directions of edges as we go.
//         Corner current = start;
//         visited.add(current);

//         // pick an arbitrary neighbor to start "moving" along the loop
//         var neighbors = adjacency.getOrDefault(current, List.of());
//         if (neighbors.isEmpty()) {
//             // no loop
//             return 0;
//         }
//         Corner next = neighbors.get(0);

//         List<Character> directions = new ArrayList<>();
//         // We'll store 'H' for horizontal, 'V' for vertical edge directions.

//         Corner prev = current;
//         current = next;
//         visited.add(current);
//         // direction of edge (prev->current):
//         directions.add(edgeDirection(prev, current));

//         // move until we return to start
//         while (!current.equals(start)) {
//             // current vertex
//             var currNeighbors = adjacency.get(current);
//             // two neighbors: one is 'prev', the other is the next step
//             Corner nxt = (currNeighbors.get(0).equals(prev)) ? currNeighbors.get(1) : currNeighbors.get(0);
//             prev = current;
//             current = nxt;
//             visited.add(current);
//             directions.add(edgeDirection(prev, current));
//         }

//         // directions now hold the direction of each edge in the loop (in order)
//         // Count sides by counting direction changes between consecutive edges in a
//         // circular manner
//         int sides = 0;
//         int n = directions.size();
//         for (int i = 0; i < n; i++) {
//             int j = (i + 1) % n;
//             if (directions.get(i) != directions.get(j)) {
//                 sides++;
//             }
//         }

//         return sides;
//     }

//     private static char edgeDirection(Corner a, Corner b) {
//         // If same row, direction is horizontal
//         // If same column, direction is vertical
//         if (a.r == b.r)
//             return 'H';
//         else
//             return 'V';
//     }

//     private static void addEdge(Map<Corner, List<Corner>> adjacency, Corner a, Corner b) {
//         adjacency.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
//         adjacency.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
//     }
// }

package day12part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GPTo1Pro {
    private static final int[][] DIRECTIONS = {
            { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }
    };

    private record Cell(int r, int c) {
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("input/day12.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            var line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                grid[r][c] = line.charAt(c);
            }
        }

        boolean[][] visited = new boolean[rows][cols];
        long totalPrice = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!visited[r][c]) {
                    totalPrice += processRegion(grid, visited, r, c);
                }
            }
        }

        System.out.println(totalPrice);
    }

    private static long processRegion(char[][] grid, boolean[][] visited, int startR, int startC) {
        int rows = grid.length;
        int cols = grid[0].length;
        char regionChar = grid[startR][startC];

        // Flood fill to find all cells in this region
        var stack = new ArrayDeque<Cell>();
        stack.push(new Cell(startR, startC));
        visited[startR][startC] = true;
        var regionCells = new ArrayList<Cell>();

        int minR = startR, maxR = startR;
        int minC = startC, maxC = startC;

        while (!stack.isEmpty()) {
            var cell = stack.pop();
            regionCells.add(cell);
            if (cell.r < minR)
                minR = cell.r;
            if (cell.r > maxR)
                maxR = cell.r;
            if (cell.c < minC)
                minC = cell.c;
            if (cell.c > maxC)
                maxC = cell.c;

            for (var dir : DIRECTIONS) {
                int nr = cell.r() + dir[0];
                int nc = cell.c() + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                    continue;
                if (!visited[nr][nc] && grid[nr][nc] == regionChar) {
                    visited[nr][nc] = true;
                    stack.push(new Cell(nr, nc));
                }
            }
        }

        int area = regionCells.size();
        // If area = 1, sides = 4 (just a single cell)
        // But let's not jump to shortcuts. The logic should handle all cases.

        // Create a bounding box around the region to reduce memory usage
        int height = maxR - minR + 1;
        int width = maxC - minC + 1;

        // We define corners in this bounding box as coordinates in:
        // Rows: 0..height, Cols: 0..width for corners
        // horizontalEdges[r][c] = true if edge between corner(r,c) and corner(r,c+1)
        // verticalEdges[r][c] = true if edge between corner(r,c) and corner(r+1,c)
        boolean[][] inRegion = new boolean[height][width];
        for (Cell cell : regionCells) {
            inRegion[cell.r - minR][cell.c - minC] = true;
        }

        boolean[][] horizontalEdges = new boolean[height + 1][width];
        boolean[][] verticalEdges = new boolean[height][width + 1];

        // Mark boundary edges
        for (int rr = 0; rr < height; rr++) {
            for (int cc = 0; cc < width; cc++) {
                if (!inRegion[rr][cc])
                    continue;
                // top edge
                if (rr == 0 || !inRegion[rr - 1][cc]) {
                    horizontalEdges[rr][cc] = true;
                }
                // bottom edge
                if (rr == height - 1 || !inRegion[rr + 1][cc]) {
                    horizontalEdges[rr + 1][cc] = true;
                }
                // left edge
                if (cc == 0 || !inRegion[rr][cc - 1]) {
                    verticalEdges[rr][cc] = true;
                }
                // right edge
                if (cc == width - 1 || !inRegion[rr][cc + 1]) {
                    verticalEdges[rr][cc + 1] = true;
                }
            }
        }

        // Find loops by following edges
        // We have two types of edges: horizontal and vertical.
        // A loop is formed by a sequence of edges alternating between horizontal and
        // vertical.
        // We'll find all loops by scanning for an edge and following it around.

        boolean[][] visitedH = new boolean[height + 1][width];
        boolean[][] visitedV = new boolean[height][width + 1];

        int totalSides = 0;

        // To follow a loop:
        // Edges connect corners. A corner is identified by (r, c).
        // Horizontal edge: from (r, c) to (r, c+1)
        // Vertical edge: from (r, c) to (r+1, c)

        // We'll find an unvisited edge and start tracing.
        // We'll keep track of direction changes to count sides.

        // Directions: 'H' or 'V'
        // We'll store the moves as directions and count changes.

        // Helper to iterate all edges and find loops
        // Check horizontal edges
        for (int hr = 0; hr <= height; hr++) {
            for (int hc = 0; hc < width; hc++) {
                if (horizontalEdges[hr][hc] && !visitedH[hr][hc]) {
                    totalSides += traceLoop(hr, hc, 'H', horizontalEdges, verticalEdges, visitedH, visitedV);
                }
            }
        }
        // Check vertical edges
        for (int vr = 0; vr < height; vr++) {
            for (int vc = 0; vc <= width; vc++) {
                if (verticalEdges[vr][vc] && !visitedV[vr][vc]) {
                    totalSides += traceLoop(vr, vc, 'V', horizontalEdges, verticalEdges, visitedH, visitedV);
                }
            }
        }

        // Price = area * totalSides
        return (long) area * totalSides;
    }

    /**
     * Trace a loop starting from a given edge. The edge is either horizontal or
     * vertical.
     * We'll walk around until we get back to the start.
     * 
     * @param sr,sc starting coordinates (sr,sc) represent:
     *              If dir=='H', horizontal edge: (sr,sc)-(sr,sc+1)
     *              If dir=='V', vertical edge: (sr,sc)-(sr+1,sc)
     * @param dir   initial direction 'H' or 'V'
     * @return number of sides (direction changes) in this loop
     */
    private static int traceLoop(int sr, int sc, char dir,
            boolean[][] horizontalEdges, boolean[][] verticalEdges,
            boolean[][] visitedH, boolean[][] visitedV) {
        // We represent current position as a corner (cr, cc).
        // If the starting edge is horizontal and goes from (sr,sc) to (sr,sc+1):
        // starting corner = (sr, sc)
        // Similarly, vertical edge from (sr,sc) to (sr+1,sc):
        // starting corner = (sr, sc)

        // Actually, for convenience, let's define the "current corner" as the
        // lower-left corner of the edge if vertical,
        // and the left corner if horizontal. But edges can be traveled in either
        // direction. We need a consistent rule.
        // We'll always track the current corner as the first endpoint of the edge in a
        // consistent manner:
        // For a horizontal edge (hr, hc): edge is between corners (hr, hc) and (hr,
        // hc+1).
        // Let's pick (hr, hc) as the current corner when we start.
        // For a vertical edge (vr, vc): edge is between (vr, vc) and (vr+1, vc).
        // Let's pick (vr, vc) as current corner when we start.

        int cr = sr;
        int cc = sc;
        char currentDir = dir;
        var directions = new ArrayList<Character>();
        // Mark the starting edge as visited
        markVisited(sr, sc, dir, visitedH, visitedV);
        directions.add(currentDir);

        // Move along the loop until we return to (cr, cc) with the same conditions.
        // To find the next edge:
        // From a corner (cr, cc), there can be up to 4 edges: left/right horizontal,
        // up/down vertical.
        // We know each corner is part of the polygon boundary, so it should have
        // exactly 2 edges connected.

        // We'll attempt to turn either left or right to follow the loop consistently.
        // The polygon boundary is always formed by a continuous chain of edges.
        // At each step, we know the current direction of travel and the current corner.
        // We must find the next edge.
        // The next edge must share the current corner and must not be the one we just
        // came from.

        // However, we must figure out the correct edge to follow so that we trace the
        // polygon consistently.
        // A simple approach:
        // Given current direction 'H' or 'V' and corner (cr, cc):
        // If currentDir == 'H' and we came from (cr,cc+1) or (cr,cc-1), we know we
        // moved horizontally.
        // Next edge must turn either up or down (if possible) or continue straight (if
        // possible).
        //
        // But we must ensure we always follow a closed boundary correctly.
        // A trick: the polygon boundary is always connected. We can just check all
        // possible edges out of this corner and pick the one not visited that is not
        // the reverse of where we came from.
        //
        // Actually, we must keep track of the previous corner to ensure we don't go
        // back where we came from directly.

        // Let's store previous corner to avoid going back directly:
        // Initially, previous corner is determined by the starting edge direction:
        int pcr = cr;
        int pcc = cc;
        // For the first step, we need to know the next corner.
        // If dir=='H', we moved from (cr,cc) to (cr,cc+1) if going right or (cr,cc) to
        // (cr,cc-1) if going left,
        // but we started at the "left" endpoint to simplify. Actually, let's define a
        // stable "forward" direction:
        // We'll just pick the "other" corner of the starting edge as the next corner.
        // For initial step:
        int ncr, ncc;
        if (dir == 'H') {
            // The edge is (sr,sc)-(sr,sc+1)
            // We started at (cr,cc) = (sr,sc)
            // The next corner is (sr, sc+1)
            ncr = cr;
            ncc = cc + 1;
        } else {
            // dir=='V'
            // The edge is (sr,sc)-(sr+1,sc)
            // Started at (cr,cc) = (sr,sc)
            // Next corner is (sr+1, sc)
            ncr = cr + 1;
            ncc = cc;
        }

        // Update corners to move forward:
        pcr = cr;
        pcc = cc;
        cr = ncr;
        cc = ncc;

        // Continue until we return to start corner with start direction. However, we
        // must consider we might come back to the start corner but from a different
        // edge. We only know the loop is closed when we hit the same corner and the
        // same "next step" from which we started. Actually, since it's a loop, we can
        // stop when we return to the initial corner (sr, sc) and try to traverse the
        // same first edge again.
        // But we don't remember which was the initial direction chosen. Let's store
        // them:
        int startCornerR = sr;
        int startCornerC = sc;
        char startDirection = dir;

        // Actually, we started on an edge, not a corner alone. The loop must end when
        // we return to the starting corner and the next edge out of that corner is the
        // starting edge. Since we mark edges visited, let's just detect completion when
        // we come back to the startCorner and the next step edge would be the starting
        // edge, meaning we've closed the loop.

        // A simpler condition:
        // We know we started on the edge that began at (sr,sc) corner.
        // After the first move, our current corner is distinct from the start corner.
        // We'll continue until we get back to (startCornerR, startCornerC).

        while (!(cr == startCornerR && cc == startCornerC)) {
            // Find next edge from (cr, cc).
            // The previous corner is (pcr, pcc).
            // We have at most 4 candidates:
            // horizontal edges from (cr, cc) to (cr, cc±1)
            // vertical edges from (cr, cc) to (cr±1, cc)

            // We'll find the next corner that has an unvisited edge connecting here.
            // The current direction is determined by the last edge we followed:
            char nextDir;
            int nrr = cr, ncc2 = cc;
            // Check all possible edges: order doesn't matter, we must pick the one that
            // continues the polygon.
            // The polygon boundary is always connected, so there should be exactly one
            // other available edge.

            // Candidates:
            // Horizontal: (cr, cc-1) if cc>0, (cr, cc+1) if cc<width
            // Vertical: (cr-1, cc) if cr>0, (cr+1, cc) if cr<height

            // But remember we have arrays sized by bounding box, so max indices:
            // horizontalEdges: dimension (height+1) x width, valid cr in [0..height], cc in
            // [0..width-1]
            // verticalEdges: dimension height x (width+1), valid cr in [0..height-1], cc in
            // [0..width]

            // From corner (cr, cc):
            // Horizontal edges out of this corner:
            // if cc>0 and horizontalEdges[cr][cc-1]==true: next corner = (cr,cc-1)
            // if cc<width and horizontalEdges[cr][cc]==true: next corner = (cr,cc+1)
            // Vertical edges out of this corner:
            // if cr>0 and verticalEdges[cr-1][cc]==true: next corner = (cr-1,cc)
            // if cr<height and verticalEdges[cr][cc]==true: next corner = (cr+1,cc)

            // We must pick the edge that leads to a corner different from (pcr,pcc),
            // because we can't go back directly.

            List<EdgeCandidate> candidates = new ArrayList<>(4);

            // Horizontal candidates
            if (cc > 0 && horizontalEdges[cr][cc - 1] && !visitedH[cr][cc - 1]) {
                candidates.add(new EdgeCandidate(cr, cc - 1, 'H', cr, cc - 1, cr, cc));
            }
            if (cc < horizontalEdges[cr].length && cc < width && horizontalEdges[cr][cc] && !visitedH[cr][cc]) {
                candidates.add(new EdgeCandidate(cr, cc, 'H', cr, cc, cr, cc + 1));
            }

            // Vertical candidates
            if (cr > 0 && verticalEdges[cr - 1][cc] && !visitedV[cr - 1][cc]) {
                candidates.add(new EdgeCandidate(cr - 1, cc, 'V', cr - 1, cc, cr, cc));
            }
            if (cr < verticalEdges.length && cr < verticalEdges.length && verticalEdges[cr][cc] && !visitedV[cr][cc]) {
                candidates.add(new EdgeCandidate(cr, cc, 'V', cr, cc, cr + 1, cc));
            }

            // Among these candidates, pick the one that doesn't lead back to (pcr,pcc).
            // Each candidate has two corners: we know one is (cr,cc) and the other is given
            // by the direction.
            EdgeCandidate chosen = null;
            for (EdgeCandidate cand : candidates) {
                int ccr = (cand.dir == 'H') ? cand.sr : cand.sr; // row of first corner is cand.sr
                int ccc = (cand.dir == 'H') ? cand.sc : cand.sc;
                // The corners of the edge cand are:
                // If 'H': edge at horizontalEdges[cand.sr][cand.sc], corners: (cand.sr,cand.sc)
                // and (cand.sr,cand.sc+1)
                // If 'H', chosen corner different from (cr,cc) is:
                // if the edge is horizontalEdges[r][c], corners are (r,c) and (r,c+1).
                // We know one corner is (cr,cc), the other is:
                int otherR, otherC;
                if (cand.dir == 'H') {
                    otherR = cand.sr;
                    if (cr == cand.sr && cc == cand.sc) {
                        otherC = cand.sc + 1;
                    } else {
                        otherC = cand.sc;
                    }
                    otherR = cand.sr;
                } else {
                    // 'V'
                    // corners: (cand.sr,cand.sc) and (cand.sr+1,cand.sc)
                    if (cr == cand.sr && cc == cand.sc) {
                        otherR = cand.sr + 1;
                        otherC = cand.sc;
                    } else {
                        otherR = cand.sr;
                        otherC = cand.sc;
                    }
                }

                if (!(otherR == pcr && otherC == pcc)) {
                    chosen = cand;
                    break;
                }
            }

            if (chosen == null) {
                // This should not happen in a well-formed polygon,
                // but if it does, let's break to avoid infinite loops.
                break;
            }

            // Mark chosen edge visited
            markVisited(chosen.sr, chosen.sc, chosen.dir, visitedH, visitedV);

            // Determine the new current direction and corner
            if (chosen.dir == 'H') {
                // horizontal edge at horizontalEdges[chosen.sr][chosen.sc]
                // corners: (chosen.sr, chosen.sc) and (chosen.sr, chosen.sc+1)
                if (cr == chosen.sr && cc == chosen.sc) {
                    // we move horizontally to the right
                    pcr = cr;
                    pcc = cc;
                    cr = chosen.sr;
                    cc = chosen.sc + 1;
                    nextDir = 'H';
                } else {
                    // cr == chosen.sr && cc == chosen.sc+1
                    // we move horizontally to the left
                    pcr = cr;
                    pcc = cc;
                    cr = chosen.sr;
                    cc = chosen.sc;
                    nextDir = 'H';
                }
            } else {
                // dir == 'V'
                // verticalEdges[chosen.sr][chosen.sc]
                // corners: (chosen.sr, chosen.sc) and (chosen.sr+1, chosen.sc)
                if (cr == chosen.sr && cc == chosen.sc) {
                    // move down
                    pcr = cr;
                    pcc = cc;
                    cr = chosen.sr + 1;
                    cc = chosen.sc;
                    nextDir = 'V';
                } else {
                    // move up
                    pcr = cr;
                    pcc = cc;
                    cr = chosen.sr;
                    cc = chosen.sc;
                    nextDir = 'V';
                }
            }

            // Record direction
            directions.add(nextDir);
        }

        // Count direction changes:
        // directions is a list of 'H' or 'V'
        // sides = count how many times directions[i] != directions[i+1]
        int sides = 0;
        int n = directions.size();
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            if (directions.get(i) != directions.get(j)) {
                sides++;
            }
        }

        return sides;
    }

    // Helper to mark an edge visited
    private static void markVisited(int r, int c, char dir,
            boolean[][] visitedH, boolean[][] visitedV) {
        if (dir == 'H') {
            visitedH[r][c] = true;
        } else {
            visitedV[r][c] = true;
        }
    }

    private static class EdgeCandidate {
        int sr, sc;
        char dir;
        int r1, c1, r2, c2;

        EdgeCandidate(int sr, int sc, char dir, int r1, int c1, int r2, int c2) {
            this.sr = sr;
            this.sc = sc;
            this.dir = dir;
            this.r1 = r1;
            this.c1 = c1;
            this.r2 = r2;
            this.c2 = c2;
        }
    }
}
