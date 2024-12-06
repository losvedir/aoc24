package Day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day5ChatGPT4o {

  public static void main(String[] args) throws IOException {
    Day5ChatGPT4o solver = new Day5ChatGPT4o();
    int result = solver.solve("input/day5.txt");
    System.out.println("The sum of middle pages is: " + result);
  }

  public int solve(String filePath) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filePath));

    // Split input into rules and updates
    int separatorIndex = lines.indexOf(""); // Blank line separates rules and updates
    List<String> ruleLines = lines.subList(0, separatorIndex);
    List<String> updateLines = lines.subList(separatorIndex + 1, lines.size());

    // Parse rules into a directed graph
    Map<Integer, Set<Integer>> dependencyGraph = new HashMap<>();
    for (String rule : ruleLines) {
      String[] parts = rule.split("\\|");
      int before = Integer.parseInt(parts[0]);
      int after = Integer.parseInt(parts[1]);
      dependencyGraph.computeIfAbsent(before, k -> new HashSet<>()).add(after);
    }

    int sumOfMiddlePages = 0;

    // Process each update
    for (String updateLine : updateLines) {
      List<Integer> update = Arrays.stream(updateLine.split(","))
          .map(Integer::parseInt)
          .toList();

      if (isOrdered(update, dependencyGraph)) {
        sumOfMiddlePages += getMiddlePage(update);
      }
    }

    return sumOfMiddlePages;
  }

  private boolean isOrdered(List<Integer> update, Map<Integer, Set<Integer>> dependencyGraph) {
    Map<Integer, Integer> pagePositions = new HashMap<>();
    for (int i = 0; i < update.size(); i++) {
      pagePositions.put(update.get(i), i);
    }

    for (Map.Entry<Integer, Set<Integer>> entry : dependencyGraph.entrySet()) {
      int before = entry.getKey();
      for (int after : entry.getValue()) {
        if (pagePositions.containsKey(before) && pagePositions.containsKey(after)) {
          if (pagePositions.get(before) >= pagePositions.get(after)) {
            return false; // Rule violated
          }
        }
      }
    }

    return true;
  }

  private int getMiddlePage(List<Integer> update) {
    return update.get(update.size() / 2);
  }
}
