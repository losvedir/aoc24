package day17part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Advent of Code - Day 17 Part 1
 *
 * This class executes the described 3-bit computer program with given initial
 * registers and program.
 * The instructions are as described in the puzzle statement.
 */
public class GPTo1Pro {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day17.txt"));

        long A = 0;
        long B = 0;
        long C = 0;
        List<Long> program = new ArrayList<>();

        // Parse the input file
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.startsWith("Register A:")) {
                A = Long.parseLong(line.substring("Register A:".length()).trim());
            } else if (line.startsWith("Register B:")) {
                B = Long.parseLong(line.substring("Register B:".length()).trim());
            } else if (line.startsWith("Register C:")) {
                C = Long.parseLong(line.substring("Register C:".length()).trim());
            } else if (line.startsWith("Program:")) {
                String progStr = line.substring("Program:".length()).trim();
                // Split by comma
                String[] parts = progStr.split(",");
                for (String p : parts) {
                    program.add(Long.parseLong(p.trim()));
                }
            }
        }

        // The instruction pointer (in units of instructions, each instruction 2 words:
        // opcode, operand)
        // Actually, IP is in terms of indices into the program array.
        long ip = 0;
        List<Long> output = new ArrayList<>();

        // Helper functions
        // Determine whether operand is combo or literal for each instruction:
        // Instructions:
        // adv (0) - combo
        // bxl (1) - literal
        // bst (2) - combo
        // jnz (3) - literal
        // bxc (4) - (operand ignored, but let's read anyway)
        // out (5) - combo
        // bdv (6) - combo
        // cdv (7) - combo

        while (true) {
            if (ip < 0 || ip >= program.size()) {
                // Beyond end of program => halt
                break;
            }
            long opcode = program.get((int) ip);
            long operand = 0;
            if (ip + 1 < program.size()) {
                operand = program.get((int) (ip + 1));
            } else {
                // If operand can't be read, end of program => halt
                break;
            }

            // We'll use a small helper method to get the value of operands.
            // For combo operands:
            // 0-3 = literal values 0-3
            // 4 = value of A
            // 5 = value of B
            // 6 = value of C
            // 7 should not appear
            //
            // For literal operands, value = operand itself.
            //
            // Which instructions have combo operands?
            // adv(0), bst(2), out(5), bdv(6), cdv(7) are combo
            // bxl(1), jnz(3) are literal
            // bxc(4) operand ignored, but let's treat as literal or combo?
            // Problem states "this instruction reads an operand but ignores it."
            // Just read but do nothing, treat it as literal or combo doesn't matter since
            // we won't use it.
            // We'll just treat it as literal (no effect).

            boolean isCombo;
            switch ((int) opcode) {
                case 0: // adv
                case 2: // bst
                case 5: // out
                case 6: // bdv
                case 7: // cdv
                    isCombo = true;
                    break;
                case 1: // bxl
                case 3: // jnz
                case 4: // bxc (ignored anyway)
                default:
                    isCombo = false;
                    break;
            }

            long operandValue;
            if (isCombo) {
                // Combo operand
                operandValue = comboValue(operand, A, B, C);
            } else {
                // Literal operand
                operandValue = operand;
            }

            // Execute the instruction
            switch ((int) opcode) {
                case 0: { // adv
                    // A = A / (2^operandValue)
                    // operandValue = combo operand
                    long divisor = (long) Math.pow(2, operandValue);
                    A = A / divisor;
                    ip += 2;
                    break;
                }
                case 1: { // bxl (literal)
                    // B = B XOR operandValue
                    B = B ^ operandValue;
                    ip += 2;
                    break;
                }
                case 2: { // bst (combo)
                    // B = operandValue % 8
                    B = operandValue % 8;
                    ip += 2;
                    break;
                }
                case 3: { // jnz (literal)
                    // if A != 0: jump to operandValue (operandValue is literal)
                    // else do nothing
                    if (A != 0) {
                        ip = operandValue;
                    } else {
                        ip += 2;
                    }
                    break;
                }
                case 4: { // bxc (ignore operand)
                    // B = B XOR C
                    B = B ^ C;
                    ip += 2;
                    break;
                }
                case 5: { // out (combo)
                    // output operandValue % 8
                    long val = operandValue % 8;
                    output.add(val);
                    ip += 2;
                    break;
                }
                case 6: { // bdv (combo)
                    // B = (A / (2^operandValue))
                    long divisor = (long) Math.pow(2, operandValue);
                    B = A / divisor;
                    ip += 2;
                    break;
                }
                case 7: { // cdv (combo)
                    // C = (A / (2^operandValue))
                    long divisor = (long) Math.pow(2, operandValue);
                    C = A / divisor;
                    ip += 2;
                    break;
                }
                default:
                    // Invalid opcode - halt
                    // (Should not happen in a valid program)
                    ip = program.size();
                    break;
            }
        }

        // Print the output
        // Join with commas
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < output.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(output.get(i));
        }
        System.out.println(sb.toString());
    }

    private static long comboValue(long operand, long A, long B, long C) {
        // combo operand:
        // 0-3 = literal 0-3
        // 4 = A, 5 = B, 6 = C, 7 = invalid
        if (operand >= 0 && operand <= 3) {
            return operand;
        } else if (operand == 4) {
            return A;
        } else if (operand == 5) {
            return B;
        } else if (operand == 6) {
            return C;
        } else {
            throw new IllegalArgumentException("Invalid combo operand 7 encountered.");
        }
    }
}
