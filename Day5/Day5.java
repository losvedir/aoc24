package Day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day5 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("input/day5.txt"));
        List<List<Integer>> constraints = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        var constraintsSection = true;
        for (String line : lines) {
            if (line.equals("")) {
                constraintsSection = false;
                continue;
            }

            if (constraintsSection) {
                var cs = Arrays.stream(line.split("\\|"))
                        .map(Integer::parseInt)
                        .toList();
                constraints.add(cs);
            } else {
                var update = Arrays.stream(line.split(","))
                        .map(Integer::parseInt)
                        .toList();
                updates.add(update);
            }
        }

        // part1(updates, constraints);
        part2(updates, constraints);
    }

    static void part1(List<List<Integer>> updates, List<List<Integer>> constraints) {
        long sum = 0;

        for (List<Integer> update : updates) {
            if (isValid(update, constraints)) {
                int i = update.size() / 2;
                sum += update.get(i);
            }
        }

        System.out.println("part1: " + sum);
    }

    static void part2(List<List<Integer>> updates, List<List<Integer>> constraints) {
        long sum = 0;

        Set<List<Integer>> cs = new HashSet<>(constraints);

        for (List<Integer> update : updates) {
            if (!isValid(update, constraints)) {
                List<Integer> sorted = update
                        .stream()
                        .sorted((u1, u2) -> {
                            if (cs.contains(Arrays.asList(u1, u2))) {
                                return -1;
                            } else if (cs.contains(Arrays.asList(u2, u1))) {
                                return 1;
                            } else {
                                return 0;
                            }
                        })
                        .toList();
                sum += sorted.get(sorted.size() / 2);
            }
        }

        System.out.println("part2: " + sum);
    }

    static boolean isValid(List<Integer> update, List<List<Integer>> constraints) {
        Map<Integer, Integer> positionOf = new HashMap<>();
        for (int i = 0; i < update.size(); i++) {
            positionOf.put(update.get(i), i);
        }

        for (List<Integer> c : constraints) {
            var before = c.get(0);
            var after = c.get(1);
            if (positionOf.containsKey(before) && positionOf.containsKey(after)) {
                if (positionOf.get(before) > positionOf.get(after)) {
                    return false;
                }
            }
        }

        return true;
    }
}
