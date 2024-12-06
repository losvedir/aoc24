package Day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5Gemini {

  public static void main(String[] args) throws IOException {
    String input = Files.readString(Paths.get("input/day5.txt"));

    List<String> sections = Arrays.asList(input.split("\n\n"));
    List<String> ruleStrings = Arrays.asList(sections.get(0).split("\n"));
    List<String> updateStrings = Arrays.asList(sections.get(1).split("\n"));

    // Parse rules
    Map<Integer, Set<Integer>> rules = new HashMap<>();
    Pattern pattern = Pattern.compile("(\\d+)\\|(\\d+)");
    for (String ruleString : ruleStrings) {
      Matcher matcher = pattern.matcher(ruleString);
      if (matcher.find()) {
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        rules.computeIfAbsent(x, k -> new HashSet<>()).add(y);
      }
    }

    // Process updates
    List<List<Integer>> updates = new ArrayList<>();
    for (String updateString : updateStrings) {
      updates.add(Arrays.stream(updateString.split(","))
          .map(Integer::parseInt)
          .toList());
    }

    long sumOfMiddlePages = updates.stream()
        .filter(update -> isUpdateValid(update, rules))
        .mapToLong(Day5Gemini::findMiddlePageNumber)
        .sum();

    System.out.println("Sum of middle page numbers of correctly-ordered updates: " + sumOfMiddlePages);
  }

  private static boolean isUpdateValid(List<Integer> update, Map<Integer, Set<Integer>> rules) {
    for (int i = 0; i < update.size() - 1; i++) {
      for (int j = i + 1; j < update.size(); j++) {
        int page1 = update.get(i);
        int page2 = update.get(j);
        if (rules.containsKey(page2) && rules.get(page2).contains(page1)) {
          return false; // Violates a rule
        }
      }
    }
    return true;
  }

  private static long findMiddlePageNumber(List<Integer> update) {
    int middleIndex = update.size() / 2;
    return update.get(middleIndex);
  }
}
