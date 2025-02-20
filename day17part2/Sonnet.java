// package day17part2;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// public class Sonnet {
//     private int registerA;
//     private int registerB;
//     private int registerC;
//     private final List<Integer> program = new ArrayList<>();
//     private final List<Integer> outputs = new ArrayList<>();
//     private int instructionPointer = 0;

//     public static void main(String[] args) throws Exception {
//         var sonnet = new Sonnet();
//         sonnet.loadInput("input/day17.txt");
//         System.out.println("Lowest working value for A: " + sonnet.findLowestWorkingA());
//     }

//     public long findLowestWorkingA() {
//         // Start from 1 and increment until we find a working value
//         long a = 1;
//         while (!testValue(a)) {
//             a++;
//             // Reset if we've gone too high
//             if (a > 1_000_000_000) {
//                 throw new RuntimeException("No solution found under 1B");
//             }
//         }
//         return a;
//     }

//     private boolean testValue(long initialA) {
//         // Reset state
//         registerA = (int) initialA;
//         registerB = 0;
//         registerC = 0;
//         instructionPointer = 0;
//         outputs.clear();

//         // Run program
//         execute();

//         // Check if output matches program
//         if (outputs.size() != program.size()) {
//             return false;
//         }

//         // Compare each value
//         for (int i = 0; i < program.size(); i++) {
//             if (!outputs.get(i).equals(program.get(i))) {
//                 return false;
//             }
//         }

//         return true;
//     }

//     public void loadInput(String filename) throws Exception {
//         var content = Files.readString(Path.of(filename));

//         // Parse initial register values - we'll ignore them for part 2
//         Pattern registerPattern = Pattern.compile("Register ([ABC]): (\\d+)");
//         Matcher matcher = registerPattern.matcher(content);

//         while (matcher.find()) {
//             String register = matcher.group(1);
//             // We ignore the initial values as per part 2 requirements
//         }

//         // Parse program
//         Pattern programPattern = Pattern.compile("Program: ([\\d,]+)");
//         matcher = programPattern.matcher(content);
//         if (matcher.find()) {
//             String[] numbers = matcher.group(1).split(",");
//             for (String number : numbers) {
//                 program.add(Integer.parseInt(number.trim()));
//             }
//         }
//     }

//     public void execute() {
//         while (instructionPointer < program.size()) {
//             int opcode = program.get(instructionPointer);
//             int operand = program.get(instructionPointer + 1);

//             switch (opcode) {
//                 case 0 -> adv(operand); // adv - divide A by 2^operand
//                 case 1 -> bxl(operand); // bxl - XOR B with literal
//                 case 2 -> bst(operand); // bst - set B to operand mod 8
//                 case 3 -> { // jnz - jump if A not zero
//                     if (!jnz(operand)) {
//                         instructionPointer += 2;
//                     }
//                     continue;
//                 }
//                 case 4 -> bxc(operand); // bxc - XOR B with C
//                 case 5 -> out(operand); // out - output operand mod 8
//                 case 6 -> bdv(operand); // bdv - like adv but result to B
//                 case 7 -> cdv(operand); // cdv - like adv but result to C
//             }
//             instructionPointer += 2;
//         }
//     }

//     private int getComboValue(int operand) {
//         return switch (operand) {
//             case 0, 1, 2, 3 -> operand;
//             case 4 -> registerA;
//             case 5 -> registerB;
//             case 6 -> registerC;
//             default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
//         };
//     }

//     private void adv(int operand) {
//         registerA = registerA / (1 << getComboValue(operand));
//     }

//     private void bxl(int operand) {
//         registerB ^= operand;
//     }

//     private void bst(int operand) {
//         registerB = getComboValue(operand) % 8;
//     }

//     private boolean jnz(int operand) {
//         if (registerA != 0) {
//             instructionPointer = operand;
//             return true;
//         }
//         return false;
//     }

//     private void bxc(int operand) {
//         registerB ^= registerC;
//     }

//     private void out(int operand) {
//         outputs.add(getComboValue(operand) % 8);
//     }

//     private void bdv(int operand) {
//         registerB = registerA / (1 << getComboValue(operand));
//     }

//     private void cdv(int operand) {
//         registerC = registerA / (1 << getComboValue(operand));
//     }
// }

package day17part2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class Sonnet {
    private int registerA;
    private int registerB;
    private int registerC;
    private final List<Integer> program = new ArrayList<>();
    private final List<Integer> outputs = new ArrayList<>();
    private int instructionPointer = 0;

    // Cache for instruction results at different positions
    private final Map<CacheKey, Set<Long>> validValuesCache = new HashMap<>();

    private record CacheKey(int position, int registerB, int registerC) {
    }

    public static void main(String[] args) throws Exception {
        var sonnet = new Sonnet();
        sonnet.loadInput("input/day17.txt");
        System.out.println("Lowest working value for A: " + sonnet.findLowestWorkingA());
    }

    public long findLowestWorkingA() {
        // Start with larger steps to find the approximate range
        long step = 1_000_000;
        long a = step;
        long lastWorking = -1;

        while (step > 0) {
            boolean found = false;
            while (a <= Long.MAX_VALUE / 2) {
                int result = quickTest(a);
                if (result == 0) {
                    // Found a working value, try to optimize it
                    lastWorking = a;
                    found = true;
                    break;
                } else if (result < 0) {
                    // Too low, increase more aggressively
                    a *= 2;
                } else {
                    // Too high, move to next value
                    a += step;
                }

                if (a > Long.MAX_VALUE / 2) {
                    break;
                }
            }

            if (found) {
                // Reduce step size and search around last working value
                a = Math.max(1, lastWorking - step);
                step /= 10;
            } else if (step > 1) {
                // If no solution found, try again with smaller step
                a = step / 10;
                step /= 10;
            } else {
                throw new RuntimeException("No solution found");
            }
        }

        return lastWorking;
    }

    private int quickTest(long initialA) {
        // Reset state
        registerA = (int) initialA;
        registerB = 0;
        registerC = 0;
        instructionPointer = 0;
        outputs.clear();
        validValuesCache.clear();

        try {
            execute();

            // If we got here, check if output matches program
            if (outputs.size() != program.size()) {
                return 1; // Too high
            }

            for (int i = 0; i < program.size(); i++) {
                int diff = outputs.get(i) - program.get(i);
                if (diff != 0) {
                    return Integer.signum(diff);
                }
            }

            return 0; // Exact match
        } catch (ArithmeticException e) {
            return 1; // Too high
        } catch (Exception e) {
            return -1; // Too low
        }
    }

    public void loadInput(String filename) throws Exception {
        var content = Files.readString(Path.of(filename));
        Pattern programPattern = Pattern.compile("Program: ([\\d,]+)");
        var matcher = programPattern.matcher(content);
        if (matcher.find()) {
            String[] numbers = matcher.group(1).split(",");
            for (String number : numbers) {
                program.add(Integer.parseInt(number.trim()));
            }
        }
    }

    public void execute() {
        while (instructionPointer < program.size()) {
            int opcode = program.get(instructionPointer);
            int operand = program.get(instructionPointer + 1);

            // Check cache for this position and register state
            CacheKey key = new CacheKey(instructionPointer, registerB, registerC);
            if (validValuesCache.containsKey(key)) {
                Set<Long> validValues = validValuesCache.get(key);
                if (!validValues.contains((long) registerA)) {
                    throw new IllegalStateException("Invalid state detected");
                }
            }

            switch (opcode) {
                case 0 -> adv(operand);
                case 1 -> bxl(operand);
                case 2 -> bst(operand);
                case 3 -> {
                    if (!jnz(operand)) {
                        instructionPointer += 2;
                    }
                    continue;
                }
                case 4 -> bxc(operand);
                case 5 -> out(operand);
                case 6 -> bdv(operand);
                case 7 -> cdv(operand);
            }

            // Cache the valid value for this position
            validValuesCache.computeIfAbsent(key, k -> new HashSet<>())
                    .add((long) registerA);

            instructionPointer += 2;
        }
    }

    private int getComboValue(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> registerA;
            case 5 -> registerB;
            case 6 -> registerC;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }

    private void adv(int operand) {
        registerA = registerA / (1 << getComboValue(operand));
    }

    private void bxl(int operand) {
        registerB ^= operand;
    }

    private void bst(int operand) {
        registerB = getComboValue(operand) % 8;
    }

    private boolean jnz(int operand) {
        if (registerA != 0) {
            instructionPointer = operand;
            return true;
        }
        return false;
    }

    private void bxc(int operand) {
        registerB ^= registerC;
    }

    private void out(int operand) {
        outputs.add(getComboValue(operand) % 8);
    }

    private void bdv(int operand) {
        registerB = registerA / (1 << getComboValue(operand));
    }

    private void cdv(int operand) {
        registerC = registerA / (1 << getComboValue(operand));
    }
}
