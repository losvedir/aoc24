package day17part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DeepSeek {
    private long A, B, C;
    private List<Long> program;
    private List<Long> output;

    public DeepSeek(long A, long B, long C, List<Long> program) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.program = program;
        this.output = new ArrayList<>();
    }

    public void run() {
        int ip = 0; // Instruction pointer
        while (ip < program.size()) {
            long opcode = program.get(ip);
            long operand = ip + 1 < program.size() ? program.get(ip + 1) : 0;

            switch ((int) opcode) {
                case 0: // adv
                    A = A / (1L << getComboValue(operand));
                    break;
                case 1: // bxl
                    B ^= operand;
                    break;
                case 2: // bst
                    B = getComboValue(operand) % 8;
                    break;
                case 3: // jnz
                    if (A != 0) {
                        ip = (int) operand * 2 - 2; // Adjust to compensate for ip += 2 after
                        break;
                    }
                    break;
                case 4: // bxc
                    B ^= C;
                    break;
                case 5: // out
                    output.add(getComboValue(operand) % 8);
                    break;
                case 6: // bdv
                    B = A / (1L << getComboValue(operand));
                    break;
                case 7: // cdv
                    C = A / (1L << getComboValue(operand));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown opcode: " + opcode);
            }

            ip += 2;
        }
    }

    private long getComboValue(long operand) {
        if (operand >= 0 && operand <= 3) {
            return operand;
        } else if (operand == 4) {
            return A;
        } else if (operand == 5) {
            return B;
        } else if (operand == 6) {
            return C;
        } else {
            throw new IllegalArgumentException("Invalid combo operand: " + operand);
        }
    }

    public String getOutput() {
        return String.join(",", output.stream().map(String::valueOf).toArray(String[]::new));
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input/day17.txt"));
        long A = Long.parseLong(lines.get(0).split(": ")[1]);
        long B = Long.parseLong(lines.get(1).split(": ")[1]);
        long C = Long.parseLong(lines.get(2).split(": ")[1]);
        List<Long> program = new ArrayList<>();
        for (String num : lines.get(3).split(": ")[1].split(",")) {
            program.add(Long.parseLong(num.trim()));
        }

        DeepSeek computer = new DeepSeek(A, B, C, program);
        computer.run();
        System.out.println(computer.getOutput());
    }
}
