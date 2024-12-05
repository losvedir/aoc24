// Chat-GPT o1 preview

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day4Part2 {
  public static void main(String[] args) throws IOException {
    var lines = Files.readAllLines(Path.of("input/day4.txt"));

    int numRows = lines.size();
    int numCols = lines.get(0).length();

    char[][] grid = new char[numRows][numCols];

    for (int i = 0; i < numRows; i++) {
      grid[i] = lines.get(i).toCharArray();
    }

    int count = 0;

    for (int row = 1; row < numRows - 1; row++) {
      for (int col = 1; col < numCols - 1; col++) {
        if (grid[row][col] != 'A') {
          continue;
        }

        char topLeft = grid[row - 1][col - 1];
        char bottomRight = grid[row + 1][col + 1];
        char topRight = grid[row - 1][col + 1];
        char bottomLeft = grid[row + 1][col - 1];

        boolean leftDiagonal = (topLeft == 'M' && bottomRight == 'S') ||
            (topLeft == 'S' && bottomRight == 'M');
        boolean rightDiagonal = (topRight == 'M' && bottomLeft == 'S') ||
            (topRight == 'S' && bottomLeft == 'M');

        if (leftDiagonal && rightDiagonal) {
          count++;
        }
      }
    }

    System.out.println("Number of X-MAS occurrences: " + count);
  }
}
