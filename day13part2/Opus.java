// package day13part2;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.ArrayList;
// import java.util.List;

// public class Opus {
//     private static class Machine {
//         int aX, aY;
//         int bX, bY;
//         long prizeX, prizeY;

//         public Machine(int aX, int aY, int bX, int bY, long prizeX, long prizeY) {
//             this.aX = aX;
//             this.aY = aY;
//             this.bX = bX;
//             this.bY = bY;
//             this.prizeX = prizeX;
//             this.prizeY = prizeY;
//         }

//         public long calculateMinTokens() {
//             long ax = aX;
//             long ay = aY;
//             long bx = bX;
//             long by = bY;

//             for (long a = 0; a <= prizeX / ax; a++) {
//                 long remainingX = prizeX - a * ax;
//                 if (remainingX % bx == 0) {
//                     long b = remainingX / bx;
//                     if (a * ay + b * by == prizeY) {
//                         return a * 3 + b;
//                     }
//                 }
//             }
//             return -1;
//         }
//     }

//     public static void main(String[] args) throws IOException {
//         List<String> lines = Files.readAllLines(Path.of("input/day13.txt"));
//         List<Machine> machines = new ArrayList<>();

//         for (int i = 0; i < lines.size(); i += 4) {
//             int aX = Integer.parseInt(lines.get(i).split("X\\+|,")[1]);
//             int aY = Integer.parseInt(lines.get(i).split("Y\\+")[1]);
//             int bX = Integer.parseInt(lines.get(i + 1).split("X\\+|,")[1]);
//             int bY = Integer.parseInt(lines.get(i + 1).split("Y\\+")[1]);
//             long prizeX = Long.parseLong(lines.get(i + 2).split("X=|,")[1]) + 10000000000000L;
//             long prizeY = Long.parseLong(lines.get(i + 2).split("Y=")[1]) + 10000000000000L;

//             machines.add(new Machine(aX, aY, bX, bY, prizeX, prizeY));
//         }

//         long totalTokens = 0;
//         int prizesWon = 0;

//         for (Machine machine : machines) {
//             long minTokens = machine.calculateMinTokens();
//             if (minTokens != -1) {
//                 totalTokens += minTokens;
//                 prizesWon++;
//             }
//         }

//         System.out.println("Minimum tokens needed to win " + prizesWon + " prizes: " + totalTokens);
//     }
// }

package day13part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Opus {
    private static class Machine {
        int aX, aY;
        int bX, bY;
        long prizeX, prizeY;

        public Machine(int aX, int aY, int bX, int bY, long prizeX, long prizeY) {
            this.aX = aX;
            this.aY = aY;
            this.bX = bX;
            this.bY = bY;
            this.prizeX = prizeX;
            this.prizeY = prizeY;
        }

        public long calculateMinTokens() {
            long gcd = gcd(Math.abs(aX), Math.abs(bX));
            if ((prizeX % gcd != 0) || (prizeY % gcd != 0)) {
                return -1;
            }

            long x = solveLinearCongruence(aX / gcd, bX / gcd, prizeX / gcd);
            long y = solveLinearCongruence(aY / gcd, bY / gcd, prizeY / gcd);

            if (x == -1 || y == -1) {
                return -1;
            }

            return x * 3 + y;
        }

        private long gcd(long a, long b) {
            if (b == 0) {
                return a;
            }
            return gcd(b, a % b);
        }

        private long solveLinearCongruence(long a, long b, long c) {
            long gcd = gcd(a, b);
            if (c % gcd != 0) {
                return -1;
            }

            a /= gcd;
            b /= gcd;
            c /= gcd;

            long x = 0, y = 1;
            long prevX = 1, prevY = 0;
            while (b != 0) {
                long q = a / b;
                long temp = x;
                x = prevX - q * x;
                prevX = temp;
                temp = y;
                y = prevY - q * y;
                prevY = temp;
                temp = a;
                a = b;
                b = temp % b;
            }

            return (prevX * c % a + a) % a;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day13.txt"));
        List<Machine> machines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 4) {
            int aX = Integer.parseInt(lines.get(i).split("X\\+|,")[1]);
            int aY = Integer.parseInt(lines.get(i).split("Y\\+")[1]);
            int bX = Integer.parseInt(lines.get(i + 1).split("X\\+|,")[1]);
            int bY = Integer.parseInt(lines.get(i + 1).split("Y\\+")[1]);
            long prizeX = Long.parseLong(lines.get(i + 2).split("X=|,")[1]) + 10000000000000L;
            long prizeY = Long.parseLong(lines.get(i + 2).split("Y=")[1]) + 10000000000000L;

            machines.add(new Machine(aX, aY, bX, bY, prizeX, prizeY));
        }

        long totalTokens = 0;
        int prizesWon = 0;

        for (Machine machine : machines) {
            long minTokens = machine.calculateMinTokens();
            if (minTokens != -1) {
                totalTokens += minTokens;
                prizesWon++;
            }
        }

        System.out.println("Minimum tokens needed to win " + prizesWon + " prizes: " + totalTokens);
    }
}
