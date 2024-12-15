package day14part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GPTo1Pro {
    // Dimensions as given in the problem statement
    private static final int WIDTH = 101; // x from 0 to 100
    private static final int HEIGHT = 103; // y from 0 to 102

    record Robot(int x, int y, int vx, int vy) {
    }

    public static void main(String[] args) throws IOException {
        // Read the input lines
        List<String> lines = Files.readAllLines(Path.of("input/day14.txt"));

        var robots = lines.stream()
                .map(line -> {
                    // Example line: p=0,4 v=3,-3
                    // Split on space first
                    var parts = line.split("\\s+");
                    var posPart = parts[0].substring(2); // remove "p="
                    var velPart = parts[1].substring(2); // remove "v="

                    var posCoords = posPart.split(",");
                    var velCoords = velPart.split(",");

                    int x = Integer.parseInt(posCoords[0]);
                    int y = Integer.parseInt(posCoords[1]);
                    int vx = Integer.parseInt(velCoords[0]);
                    int vy = Integer.parseInt(velCoords[1]);

                    return new Robot(x, y, vx, vy);
                })
                .toList();

        // Part 2:
        // We suspect (based on experience with similar puzzles) that the "Easter egg"
        // emerges when the positions of the robots form a minimal bounding rectangle.
        // The puzzle states they wrap around edges, forming a toroidal topology.
        //
        // On a torus (width=101, height=103), to find the minimal bounding rectangle of
        // a set of points, we need to consider that we can "choose" where zero starts
        // due to wrapping. The minimal bounding rectangle is the smallest rectangle
        // that
        // can contain all points on a wrap-around surface.
        //
        // One known approach:
        // For each time t:
        // 1) Compute all positions modulo WIDTH and HEIGHT.
        // 2) Find the minimal bounding rectangle on the torus.
        // On a circular dimension (like x wrapping), the minimal interval
        // covering all points on a circle is:
        // circle_length - (largest gap between consecutive points on the circle).
        // 3) The rectangle area = minimal_x_interval * minimal_y_interval
        // We'll track when this area is smallest.
        //
        // We'll guess that if we run forward in time, the pattern forms at the time
        // when this minimal bounding area is at a minimum. After that, it may start to
        // get larger.
        //
        // We'll run forward until we detect that area is growing again after finding a
        // minimum.
        // The problem states: "What is the fewest number of seconds that must elapse?"
        // So we want the first time that the minimal bounding rectangle is minimal.
        //
        // Practical approach:
        // We'll start from t=0 and go forward until we see a local minimum.
        // We should put a reasonable upper bound on time. In AoC-type puzzles,
        // it's often safe to search until the bounding area starts increasing again
        // significantly.
        //
        // We'll track the minimal area found and if we pass some threshold of time
        // without improvement, we stop.
        // (In a real AoC scenario, we know that the pattern emerges in a reasonable
        // time.)

        int maxTime = 100000; // arbitrary large upper bound to avoid infinite loops
        long minArea = Long.MAX_VALUE;
        int bestTime = 0;

        // We'll implement a helper function to compute minimal rectangle dimension on a
        // torus dimension
        // given all coordinates in that dimension and the length of that dimension.

        for (int t = 0; t < maxTime; t++) {
            // Compute positions at time t
            int[] xs = new int[robots.size()];
            int[] ys = new int[robots.size()];
            for (int i = 0; i < robots.size(); i++) {
                Robot r = robots.get(i);
                int nx = (r.x() + r.vx() * t) % WIDTH;
                int ny = (r.y() + r.vy() * t) % HEIGHT;
                if (nx < 0)
                    nx += WIDTH;
                if (ny < 0)
                    ny += HEIGHT;
                xs[i] = nx;
                ys[i] = ny;
            }

            int xRange = minimalIntervalOnTorus(xs, WIDTH);
            int yRange = minimalIntervalOnTorus(ys, HEIGHT);

            long area = (long) xRange * yRange;
            if (area < minArea) {
                minArea = area;
                bestTime = t;
            } else {
                // Heuristic: once we see that area is growing again for a while, we might have
                // passed the optimal time.
                // Let's say if we pass a certain amount of time after a minimum without
                // improvement, we break.
                // This is a guess, but usually AoC messages form a clear minimum.
                if (t - bestTime > 2000) { // arbitrary threshold
                    break;
                }
            }
        }

        System.out.println(bestTime);
    }

    // Find minimal interval on a toroidal dimension that covers all points
    // Method:
    // 1. Sort points.
    // 2. Duplicate them by adding dimension length to the second half to represent
    // wrap.
    // 3. Find largest gap between consecutive points in the circular sense.
    // minimal interval = dimension length - largest gap
    private static int minimalIntervalOnTorus(int[] coords, int dimension) {
        java.util.Arrays.sort(coords);
        int n = coords.length;

        // If all points are the same, interval is 0
        boolean allSame = true;
        for (int i = 1; i < n; i++) {
            if (coords[i] != coords[0]) {
                allSame = false;
                break;
            }
        }
        if (allSame)
            return 0;

        // Consider the circle: The largest gap on a circle is found by checking gaps
        // between consecutive points and also considering the wrap-around gap.
        // Actually, we can consider an equivalent linear approach:
        // Duplicate the sorted array with offset dimension to handle wrap
        int[] extended = new int[2 * n];
        for (int i = 0; i < n; i++) {
            extended[i] = coords[i];
            extended[i + n] = coords[i] + dimension;
        }

        // We must cover all points with a continuous interval of minimal length.
        // This is equivalent to finding the smallest interval containing all points if
        // we can wrap around.
        // Another way:
        // The minimal interval covering all points on a circle = dimension - largest
        // gap on circle.
        // The largest gap on the circle: we look at consecutive differences in the
        // sorted array,
        // plus the gap from last to first (with wrap).

        // The gap from last to first with wrap is (coords[0] + dimension) -
        // coords[n-1].
        // We'll find the largest gap by scanning original sorted coords plus wrap:

        int largestGap = 0;
        for (int i = 1; i < n; i++) {
            int gap = coords[i] - coords[i - 1];
            if (gap > largestGap)
                largestGap = gap;
        }
        // Check wrap gap:
        int wrapGap = (coords[0] + dimension) - coords[n - 1];
        if (wrapGap > largestGap)
            largestGap = wrapGap;

        int interval = dimension - largestGap;
        return interval;
    }
}
