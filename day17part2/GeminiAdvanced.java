package day17part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GeminiAdvanced {

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input/day17.txt"));
            int registerB = Integer.parseInt(lines.get(1).split(": ")[1]);
            int registerC = Integer.parseInt(lines.get(2).split(": ")[1]);
            String[] programStr = lines.get(4).split(":")[1].trim().split(",");
            int[] program = new int[programStr.length];
            for (int i = 0; i < programStr.length; i++) {
                program[i] = Integer.parseInt(programStr[i]);
            }

            int initialA = findInitialA(registerB, registerC, program);
            System.out.println(initialA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int findInitialA(int registerB, int registerC, int[] program) {
        for (int initialA = 1;; initialA++) {
            String output = runProgram(initialA, registerB, registerC, program);
            String expectedOutput = getProgramString(program);
            if (output.equals(expectedOutput)) {
                return initialA;
            }
        }
    }

    public static String runProgram(int registerA, int registerB, int registerC, int[] program) {
        int instructionPointer = 0;
        List<Integer> output = new ArrayList<>();

        while (instructionPointer < program.length) {
            int opcode = program[instructionPointer];
            int operand = program[instructionPointer + 1];
            int comboValue = 0;

            switch (opcode) {
                case 0: // adv
                case 6: // bdv
                case 7: // cdv
                    comboValue = getComboValue(operand, registerA, registerB, registerC);
                    int divisor = (int) Math.pow(2, comboValue);
                    int result = registerA / divisor;
                    if (opcode == 0) {
                        registerA = result;
                    } else if (opcode == 6) {
                        registerB = result;
                    } else {
                        registerC = result;
                    }
                    instructionPointer += 2;
                    break;
                case 1: // bxl
                    registerB ^= operand;
                    instructionPointer += 2;
                    break;
                case 2: // bst
                    comboValue = getComboValue(operand, registerA, registerB, registerC);
                    registerB = comboValue % 8;
                    instructionPointer += 2;
                    break;
                case 3: // jnz
                    if (registerA != 0) {
                        instructionPointer = operand;
                    } else {
                        instructionPointer += 2;
                    }
                    break;
                case 4: // bxc
                    registerB ^= registerC;
                    instructionPointer += 2;
                    break;
                case 5: // out
                    comboValue = getComboValue(operand, registerA, registerB, registerC);
                    output.add(comboValue % 8);
                    instructionPointer += 2;
                    break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < output.size(); i++) {
            sb.append(output.get(i));
            if (i < output.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static int getComboValue(int operand, int registerA, int registerB, int registerC) {
        if (operand >= 0 && operand <= 3) {
            return operand;
        } else if (operand == 4) {
            return registerA;
        } else if (operand == 5) {
            return registerB;
        } else if (operand == 6) {
            return registerC;
        } else {
            return 0;
        }
    }

    private static String getProgramString(int[] program) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < program.length; i++) {
            sb.append(program[i]);
            if (i < program.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
