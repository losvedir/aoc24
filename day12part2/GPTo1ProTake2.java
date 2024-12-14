package day12part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * We need to:
 * 1) Identify all regions of identical plant type. Each region is one or more
 * connected garden plots (4-directionally).
 * 2) For each region, we must find:
 * - The area: number of plots.
 * - The perimeter edges. A perimeter edge occurs whenever a plot in the region
 * is adjacent to either:
 * * Out of bounds, or
 * * A plot of a different type.
 * - The "number of sides," defined as the count of straight fence segments
 * forming the boundary polygons of the region.
 * Essentially, we must form polygons from the perimeter edges. Consecutive
 * edges in the same direction form one side.
 * If the region has holes or multiple disconnected boundary loops, we count
 * sides for all loops combined.
 *
 * Steps:
 * - Parse the input map.
 * - Use a visited grid to find each region via DFS or BFS.
 * - For each region:
 * * Collect all its cells.
 * * Determine perimeter edges (each perimeter edge corresponds to a boundary
 * between the region and non-region).
 * Represent these edges in terms of "grid corners" (vertices at cell corners).
 * * Build a graph of these perimeter edges (each edge is between two corner
 * points).
 * * Each connected set of edges forms a loop (a polygon). To find the "number
 * of sides":
 * - Trace around each loop.
 * - While tracing, combine consecutive collinear edges into a single side.
 * (Consecutive edges along the same row or column form one straight side.)
 * * Sum the sides from all loops to get the region's total sides.
 * - The price for each region = area * sides.
 * - Sum all region prices for the final answer.
 *
 * Efficiency considerations:
 * - The grid can be large (e.g., 140x140). A straightforward BFS/DFS is fine.
 * - Building perimeter edges and processing them should also be efficient.
 *
 * We'll store the garden as a char[][].
 * We'll define a helper for finding regions and their boundaries.
 */
public class GPTo1ProTake2 {
    static final int[] DR = { -1, 1, 0, 0 };
    static final int[] DC = { 0, 0, -1, 1 };

    public static void main(String[] args) throws IOException {
        char[][] grid = readGrid("input/day12.txt");
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];

        long totalPrice = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!visited[r][c]) {
                    // Find a region of the same type using BFS/DFS
                    char type = grid[r][c];
                    List<int[]> regionCells = new ArrayList<>();
                    Queue<int[]> q = new ArrayDeque<>();
                    q.add(new int[] { r, c });
                    visited[r][c] = true;

                    while (!q.isEmpty()) {
                        int[] cur = q.poll();
                        regionCells.add(cur);
                        int rr = cur[0];
                        int cc = cur[1];
                        for (int i = 0; i < 4; i++) {
                            int nr = rr + DR[i];
                            int nc = cc + DC[i];
                            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                                continue;
                            if (!visited[nr][nc] && grid[nr][nc] == type) {
                                visited[nr][nc] = true;
                                q.add(new int[] { nr, nc });
                            }
                        }
                    }

                    int area = regionCells.size();

                    // Determine perimeter edges
                    // We'll represent perimeter edges as edges between corner points.
                    // A corner point can be represented by (row, col) in a "vertex grid" that is
                    // (rows+1)x(cols+1).
                    // For each cell in the region, check all four edges. If it borders a non-region
                    // cell or OOB, that's a perimeter edge.
                    // Horizontal edge: top edge between (r,c) and (r,c+1) or bottom edge between
                    // (r+1,c) and (r+1,c+1)
                    // Vertical edge: left edge between (r,c) and (r+1,c) or right edge between
                    // (r,c+1) and (r+1,c+1)

                    // Build a set of edges. Each edge is between two vertices:
                    // Represent a vertex as an integer ID: vertexID = v_r*(cols+1) + v_c
                    // where v_r and v_c are corner coordinates.
                    // We'll use a map from vertex to adjacency list to form polygons later.

                    Set<Edge> perimeterEdges = new HashSet<>();
                    // A helper function to add an edge to perimeterEdges
                    // We'll store edges in a normalized form (smaller vertex ID first).
                    // Edge definition: two distinct vertices.
                    class AddEdge {
                        void add(int r1, int c1, int r2, int c2) {
                            int v1 = r1 * (cols + 1) + c1;
                            int v2 = r2 * (cols + 1) + c2;
                            if (v1 > v2) {
                                int tmp = v1;
                                v1 = v2;
                                v2 = tmp;
                            }
                            perimeterEdges.add(new Edge(v1, v2));
                        }
                    }
                    AddEdge edgeAdder = new AddEdge();

                    // To quickly check if a cell is in the region
                    // We can use a boolean[][] mask or a HashSet of coordinates.
                    // Given large input, boolean mask is better.
                    boolean[][] inRegion = new boolean[rows][cols];
                    for (int[] cell : regionCells) {
                        inRegion[cell[0]][cell[1]] = true;
                    }

                    for (int[] cell : regionCells) {
                        int rr = cell[0];
                        int cc = cell[1];
                        // Check up:
                        if (rr == 0 || !inRegion[rr - 1][cc]) {
                            // perimeter edge on top
                            edgeAdder.add(rr, cc, rr, cc + 1);
                        }
                        // Check down:
                        if (rr == rows - 1 || !inRegion[rr + 1][cc]) {
                            // perimeter edge on bottom
                            edgeAdder.add(rr + 1, cc, rr + 1, cc + 1);
                        }
                        // Check left:
                        if (cc == 0 || !inRegion[rr][cc - 1]) {
                            // perimeter edge on left
                            edgeAdder.add(rr, cc, rr + 1, cc);
                        }
                        // Check right:
                        if (cc == cols - 1 || !inRegion[rr][cc + 1]) {
                            // perimeter edge on right
                            edgeAdder.add(rr, cc + 1, rr + 1, cc + 1);
                        }
                    }

                    // Now we have a set of edges representing the perimeter.
                    // Build adjacency for these vertices
                    Map<Integer, List<Integer>> adj = new HashMap<>();
                    for (Edge e : perimeterEdges) {
                        adj.computeIfAbsent(e.v1, k -> new ArrayList<>()).add(e.v2);
                        adj.computeIfAbsent(e.v2, k -> new ArrayList<>()).add(e.v1);
                    }

                    // Find all polygon loops.
                    // Each connected component of this graph should form one or more closed loops.
                    // Actually, each connected component here should form exactly one loop because
                    // it represents a polygon boundary.
                    // But we must consider that holes inside might create multiple loops (i.e.
                    // multiple disconnected polygons).
                    // So we find cycles by just picking a start vertex and following edges until we
                    // come back.

                    // We'll track visited vertices in the perimeter graph:
                    Set<Integer> visitedVertices = new HashSet<>();
                    int totalSidesForRegion = 0;

                    for (int startVertex : adj.keySet()) {
                        if (!visitedVertices.contains(startVertex)) {
                            // Explore this polygon loop.
                            // The perimeter should form closed loops where each vertex has degree 2,
                            // forming a chain.
                            // If there's a branch, that would be strange for a proper polygon perimeter,
                            // but let's assume a well-formed input (since it's a perimeter).
                            List<Integer> loop = traceLoop(startVertex, adj, visitedVertices);
                            // 'loop' now lists the vertices in order forming one closed polygon chain
                            // We need directions of edges to combine collinear segments.
                            // Convert vertex loop to edges and determine directions.
                            // Vertex coordinates from ID:
                            // v_r = v / (cols+1), v_c = v % (cols+1)
                            int[] vr = new int[loop.size()];
                            int[] vc = new int[loop.size()];
                            for (int i = 0; i < loop.size(); i++) {
                                int v = loop.get(i);
                                vr[i] = v / (cols + 1);
                                vc[i] = v % (cols + 1);
                            }

                            // We'll have loop.size() edges, from i to i+1 (mod)
                            int sidesCount = 0;
                            int countEdges = loop.size(); // edges in polygon
                            // Determine directions and group them:
                            // An edge can be horizontal or vertical:
                            // If vr[i] == vr[i+1], horizontal edge
                            // If vc[i] == vc[i+1], vertical edge
                            // We'll merge consecutive edges in same direction and line.
                            // Actually, if we are just counting how many straight segments (lines) form the
                            // polygon,
                            // we increment sidesCount each time direction or alignment changes.
                            // Implementation detail:
                            // Start from first edge, track direction (H or V) and track the line (for H:
                            // same vr? for V: same vc?)
                            int idx = 0;
                            // Extract first edge direction
                            int nextIdx = (idx + 1) % countEdges;
                            boolean horizontal = (vr[idx] == vr[nextIdx]);
                            int lineCoord = horizontal ? vr[idx] : vc[idx]; // line coordinate for alignment
                            int sides = 1; // start with one side
                            for (int i = 1; i < countEdges; i++) {
                                int cur = i;
                                int curNext = (i + 1) % countEdges;
                                boolean curHorizontal = (vr[cur] == vr[curNext]);
                                int curLineCoord = curHorizontal ? vr[cur] : vc[cur];
                                if (curHorizontal != horizontal || curLineCoord != lineCoord) {
                                    // direction or alignment changed, start a new side
                                    sides++;
                                    horizontal = curHorizontal;
                                    lineCoord = curLineCoord;
                                }
                            }

                            totalSidesForRegion += sides;
                        }
                    }

                    // Price for this region
                    long price = (long) area * (long) totalSidesForRegion;
                    totalPrice += price;
                }
            }
        }

        System.out.println(totalPrice);
    }

    // Given a start vertex in a graph where each vertex has degree 2 or less and
    // forms cycles,
    // trace out the cycle. We'll just follow edges until we return to the start.
    private static List<Integer> traceLoop(int start, Map<Integer, List<Integer>> adj, Set<Integer> visited) {
        List<Integer> loop = new ArrayList<>();
        int current = start;
        int prev = -1;
        while (true) {
            loop.add(current);
            visited.add(current);

            // move to next
            List<Integer> nbrs = adj.get(current);
            int next = -1;
            for (int n : nbrs) {
                if (n != prev) {
                    next = n;
                    break;
                }
            }
            if (next == -1) {
                // This would indicate a dead end, which shouldn't happen for a proper loop
                break;
            }

            if (next == start) {
                // Completed the loop
                break;
            }

            prev = current;
            current = next;
        }
        return loop;
    }

    private static char[][] readGrid(String file) throws IOException {
        List<char[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.toCharArray());
            }
        }
        return lines.toArray(new char[0][]);
    }

    static class Edge {
        int v1, v2;

        Edge(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Edge))
                return false;
            Edge e = (Edge) o;
            return v1 == e.v1 && v2 == e.v2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1, v2);
        }
    }
}
