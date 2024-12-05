import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day4Part2Gemini {

  public static void main(String[] args) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get("input/day4.txt"));
    char[][] grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);
    int count = 0;

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == 'M') {
          count += checkForXMas(grid, i, j);
        }
      }
    }

    System.out.println("Total X-MASes found: " + count);
  }

  private static int checkForXMas(char[][] grid, int row, int col) {
    int count = 0;
    // Check all 4 diagonal directions for "MAS"
    count += checkDiagonal(grid, row, col, 1, 1);
    count += checkDiagonal(grid, row, col, 1, -1);
    count += checkDiagonal(grid, row, col, -1, 1);
    count += checkDiagonal(grid, row, col, -1, -1);
    return count;
  }

  private static int checkDiagonal(char[][] grid, int row, int col, int rowIncrement, int colIncrement) {
    try {
      // Check for "MAS" or "SAM" in the given diagonal direction
      if ((grid[row + rowIncrement][col + colIncrement] == 'A' &&
          grid[row + 2 * rowIncrement][col + 2 * colIncrement] == 'S') ||
          (grid[row + rowIncrement][col + colIncrement] == 'A' &&
              grid[row + 2 * rowIncrement][col + 2 * colIncrement] == 'S')) {
        return 1;
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      // Ignore out-of-bounds exceptions
    }
    return 0;
  }
}
