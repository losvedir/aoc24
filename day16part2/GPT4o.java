package day16part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GPT4o {

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day16.txt"));
            System.out.println(findBestPathTiles(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class State {
        int x, y, direction, score;

        State(int x, int y, int direction, int score) {
            this.x = x;
            this.y = y;
            this.direction = direction; // 0: East, 1: South, 2: West, 3: North
            this.score = score;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            State state = (State) obj;
            return x == state.x && y == state.y && direction == state.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, direction);
        }
    }

    public static int findBestPathTiles(List<String> maze) {
        int rows = maze.size();
        int cols = maze.get(0).length();
        char[][] grid = new char[rows][cols];
        State start = null;
        int endX = 0, endY = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = maze.get(i).charAt(j);
                if (grid[i][j] == 'S') {
                    start = new State(i, j, 0, 0);
                } else if (grid[i][j] == 'E') {
                    endX = i;
                    endY = j;
                }
            }
        }

        // Step 1: Find the minimum score using a Dijkstra-like approach
        int minScore = findLowestScore(maze, grid, start, endX, endY);

        // Step 2: Trace all tiles that are part of the best paths
        Set<String> bestPathTiles = new HashSet<>();
        traceBestPaths(grid, start, endX, endY, minScore, bestPathTiles);

        return bestPathTiles.size();
    }

    private static int findLowestScore(List<String> maze, char[][] grid, State start, int endX, int endY) {
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // East, South, West, North
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.score));
        Set<State> visited = new HashSet<>();

        queue.add(start);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.x == endX && current.y == endY) {
                return current.score;
            }

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);

            // Try moving forward
            int nx = current.x + directions[current.direction][0];
            int ny = current.y + directions[current.direction][1];
            if (isValid(nx, ny, grid)) {
                queue.add(new State(nx, ny, current.direction, current.score + 1));
            }

            // Try rotating clockwise and counterclockwise
            queue.add(new State(current.x, current.y, (current.direction + 1) % 4, current.score + 1000));
            queue.add(new State(current.x, current.y, (current.direction + 3) % 4, current.score + 1000));
        }

        return -1; // Should never reach here
    }

    private static void traceBestPaths(char[][] grid, State start, int endX, int endY, int minScore,
            Set<String> bestPathTiles) {
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // East, South, West, North
        Queue<State> queue = new LinkedList<>();
        Map<State, Integer> visited = new HashMap<>();

        queue.add(start);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (visited.containsKey(current) && visited.get(current) <= current.score) {
                continue;
            }

            visited.put(current, current.score);
            bestPathTiles.add(current.x + "," + current.y);

            // Try moving forward
            int nx = current.x + directions[current.direction][0];
            int ny = current.y + directions[current.direction][1];
            if (isValid(nx, ny, grid) && current.score + 1 <= minScore) {
                queue.add(new State(nx, ny, current.direction, current.score + 1));
            }

            // Try rotating clockwise and counterclockwise
            if (current.score + 1000 <= minScore) {
                queue.add(new State(current.x, current.y, (current.direction + 1) % 4, current.score + 1000));
                queue.add(new State(current.x, current.y, (current.direction + 3) % 4, current.score + 1000));
            }
        }
    }

    private static boolean isValid(int x, int y, char[][] grid) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] != '#';
    }
}
