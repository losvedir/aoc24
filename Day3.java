import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Day3 {
    public static void main(String[] args) throws IOException {
        var input = Files.readString(Path.of("input/day3.txt"));
        // part1(input);
        part2(input);
    }

    static void part1(String input) {
        var pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        var matcher = pattern.matcher(input);

        long sum = 0;

        while (matcher.find()) {
            var n1 = Integer.parseInt(matcher.group(1));
            var n2 = Integer.parseInt(matcher.group(2));
            sum += n1 * n2;
        }

        System.out.println("part1: " + sum);
    }

    static void part2(String input) {
        var pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|(do\\(\\))|(don't\\(\\))");

        var matcher = pattern.matcher(input);
        long sum = 0;
        var enabled = true;

        while (matcher.find()) {
            var g = matcher.group();
            if (g.equals("do()")) {
                enabled = true;
            } else if (g.equals("don't()")) {
                enabled = false;
            } else {
                if (enabled) {
                    var n1 = Integer.parseInt(matcher.group(1));
                    var n2 = Integer.parseInt(matcher.group(2));
                    sum += n1 * n2;
                }
            }
        }

        System.out.println("part2: " + sum);
    }
}
