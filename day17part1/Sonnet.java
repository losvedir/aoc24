package day17part1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sonnet {
    private int registerA;
    private int registerB;
    private int registerC;
    private final List<Integer> program = new ArrayList<>();
    private final List<Integer> outputs = new ArrayList<>();
    private int instructionPointer = 0;

    public static void main(String[] args) throws Exception {
        var sonnet = new Sonnet();
        sonnet.loadInput("input/day17.txt");
        sonnet.execute();
        System.out.println(sonnet.getOutput());
    }

    public void loadInput(String filename) throws Exception {
        var content = Files.readString(Path.of(filename));

        // Parse initial register values
        Pattern registerPattern = Pattern.compile("Register ([ABC]): (\\d+)");
        Matcher matcher = registerPattern.matcher(content);

        while (matcher.find()) {
            String register = matcher.group(1);
            int value = Integer.parseInt(matcher.group(2));
            switch (register) {
                case "A" -> registerA = value;
                case "B" -> registerB = value;
                case "C" -> registerC = value;
            }
        }

        // Parse program
        Pattern programPattern = Pattern.compile("Program: ([\\d,]+)");
        matcher = programPattern.matcher(content);
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

            switch (opcode) {
                case 0 -> adv(operand); // adv - divide A by 2^operand
                case 1 -> bxl(operand); // bxl - XOR B with literal
                case 2 -> bst(operand); // bst - set B to operand mod 8
                case 3 -> { // jnz - jump if A not zero
                    if (!jnz(operand)) {
                        instructionPointer += 2;
                    }
                    continue;
                }
                case 4 -> bxc(operand); // bxc - XOR B with C
                case 5 -> out(operand); // out - output operand mod 8
                case 6 -> bdv(operand); // bdv - like adv but result to B
                case 7 -> cdv(operand); // cdv - like adv but result to C
            }
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

    public String getOutput() {
        return String.join(",", outputs.stream().map(String::valueOf).toList());
    }
}
