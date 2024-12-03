import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 {
    public static void main(String[] args) throws IOException {
        // part1();
        part2();
    }

    public static void part1() throws IOException {
        String input = Files.readString(Path.of("input/day1.txt"));

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        input.lines().forEach(line -> {
            String[] parts = line.trim().split(" +");
            list1.add(Integer.parseInt(parts[0]));
            list2.add(Integer.parseInt(parts[1]));
        });

        Collections.sort(list1);
        Collections.sort(list2);

        int diff = 0;

        for (int i = 0; i < list1.size(); i++) {
            diff += Math.abs(list1.get(i) - list2.get(i));
        }

        System.out.println("part1: " + diff);
    }

    public static void part2() throws IOException {
        String input = Files.readString(Path.of("input/day1.txt"));

        List<Integer> list1 = new ArrayList<>();
        Map<Integer, Integer> intCount = new HashMap<>();

        input.lines().forEach(line -> {
            String[] parts = line.trim().split(" +");
            list1.add(Integer.parseInt(parts[0]));

            int p1 = Integer.parseInt(parts[1]);
            intCount.merge(p1, 1, Integer::sum);
        });

        int diff = 0;

        for (int i = 0; i < list1.size(); i++) {
            int elem = list1.get(i);
            diff += elem * intCount.getOrDefault(elem, 0);
        }

        System.out.println("part2: " + diff);
    }
}
