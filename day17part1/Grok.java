package day17part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Grok {

    private static final int REG_A = 0;
    private static final int REG_B = 1;
    private static final int REG_C = 2;
    
    private int[] registers = new int[3];
    private List<Integer> output = new ArrayList<>();

    public Grok(int a, int b, int c) {
        registers[REG_A] = a;
        registers[REG_B] = b;
        registers[REG_C] = c;
    }

    public String runProgram(List<Integer> program) {
        for (int ip = 0; ip < program.size(); ip += 2) {
            if (ip >= program.size()) break;
            int opcode = program.get(ip);
            int operand = program.get(ip + 1);

            switch (opcode) {
                case 0: // adv
                    registers[REG_A] /= (1 << operand);
                    break;
                case 1: // bxl
                    registers[REG_B] ^= operand;
                    break;
                case 2: // bst
                    registers[REG_B] = operand % 8;
                    break;
                case 3: // jnz
                    if (registers[REG_A] != 0) {
                        ip = operand - 2; // -2 because we'll add 2 at the end of the loop
                    }
                    break;
                case 4: // bxc
                    registers[REG_B] ^= registers[REG_C];
                    break;
                case 5: // out
                    output.add(operand % 8);
                    break;
                case 6: // bdv
                    registers[REG_B] = registers[REG_A] / (1 << operand);
                    break;
                case 7: // cdv
                    registers[REG_C] = registers[REG_A] / (1 << operand);
                    break;
            }
        }

        // Convert output list to string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < output.size(); i++) {
            sb.append(output.get(i));
            if (i < output.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        // Assuming input file format is comma separated integers
        List<Integer> program = Files.lines(Paths.get("input/day17.txt"))
                .flatMap(line -> java.util.stream.Stream.of(line.split(",")))
                .map(Integer::parseInt)
                .toList();
        
        Grok computer = new Grok(729, 0, 0); // Initial values from the problem statement
        String result = computer.runProgram(program);
        System.out.println("Output: " + result);
    }
}
