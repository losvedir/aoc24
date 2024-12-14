package day13part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GPTo1 {
    record Machine(long aX, long aY, long bX, long bY, long prizeX, long prizeY) {
    }

    private static final long OFFSET = 10000000000000L;

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("input/day13.txt"));
        var machines = parseMachines(lines);

        long totalCost = 0;
        int solvableCount = 0;

        for (var m : machines) {
            long minCost = solveMachine(m);
            if (minCost >= 0) {
                solvableCount++;
                totalCost += minCost;
            }
        }

        System.out.println(totalCost);
    }

    private static long solveMachine(Machine machine) {
        // Adjust prize coordinates by the large offset
        long pX = machine.prizeX() + OFFSET;
        long pY = machine.prizeY() + OFFSET;

        long aX = machine.aX();
        long aY = machine.aY();
        long bX = machine.bX();
        long bY = machine.bY();

        // Solve the system:
        // A*aX + B*bX = pX
        // A*aY + B*bY = pY

        long D = aX * bY - aY * bX; // determinant

        if (D == 0) {
            // The equations are dependent. Check consistency:
            // For consistency, (pX, pY) must be on the line spanned by (aX,bX)/(aY,bY).
            // Let's check if the ratio pX:aX = pY:aY and pX:bX = pY:bY whenever these
            // denominators are not zero.
            // Actually, a more robust check:
            // If aX*bY = aY*bX, then we must have pX*bY = pY*bX for a solution to exist.
            if (aX * bY == aY * bX) {
                if (pX * bY == pY * bX) {
                    // Infinite solutions scenario.
                    // Equation reduces to: aX*A + bX*B = pX (and similarly for Y).
                    // We must find non-negative integers A,B minimizing cost=3A+B.
                    // Parametric form:
                    // aX*A = pX - bX*B
                    // A = (pX - bX*B)/aX
                    // We must pick B so that (pX - bX*B) is divisible by aX and A≥0,B≥0.
                    // Minimizing 3A+B:
                    // Let's try a mathematical approach:
                    //
                    // A≥0 => pX - bX*B ≥0 => B ≤ pX/bX if bX>0. If bX<0, inequality flips.
                    // Also, B≥0.
                    //
                    // We need to find a B≥0 that makes (pX - bX*B) divisible by aX and yields a
                    // minimal cost.
                    //
                    // Because bX and aX may be large, we must find a B that solves this congruence:
                    // pX - bX*B ≡ 0 (mod aX)
                    // => bX*B ≡ pX (mod aX)
                    //
                    // Solve bX*B ≡ pX (mod aX) using extended Euclidean algorithm:
                    //
                    // Once we have a particular solution for B mod aX/gcd(aX,bX),
                    // We can adjust B by multiples of (aX/gcd) to find a suitable B≥0,A≥0 with
                    // minimal cost.
                    //
                    // This is more complex, but let's implement it since we must handle infinite
                    // solutions.

                    long gcd = gcd(aX, bX);
                    if (pX % gcd != 0) {
                        // No solution
                        return -1;
                    }

                    // Reduced equation: let aX' = aX/gcd, bX' = bX/gcd, pX' = pX/gcd.
                    long aXprime = aX / gcd;
                    long bXprime = bX / gcd;
                    long pXprime = pX / gcd;

                    // Solve bX'*B ≡ pX' (mod aX')
                    // We can find the modular inverse of bX' mod aX' if gcd=1. But gcd might not be
                    // 1. We factored gcd out already.
                    // After factoring gcd, aX' and bX' are coprime, so we can find inverse.
                    long bXinv = modInverse(bXprime, aXprime);

                    // A particular solution:
                    long B0 = ((pXprime % aXprime) * bXinv) % aXprime;

                    // B can be written as B = B0 + k*aXprime for any integer k.
                    // Now we must ensure A≥0 and B≥0.
                    // A = (pX - bX*B)/aX
                    // = (pX - bX*(B0 + k*aXprime))/aX
                    // = (pX - bX*B0 - bX*k*aXprime)/aX
                    //
                    // Since bX = bXprime*gcd, aX = aXprime*gcd,
                    // A = (pX/gcd - bX'/gcd * B)/aX' * (No, careful. Let's stay in original terms.)
                    //
                    // Let's just iterate over possible k to find a non-negative (A,B). Because aX'
                    // and B0 might be large, we must be careful.
                    // But we have no direct limit on button presses. Possibly enormous. We need a
                    // more direct approach.

                    // Let's rewrite conditions:
                    // A≥0 => (pX - bX*B)≥0 => B ≤ pX/bX if bX>0. If bX<0, we get another
                    // inequality.
                    // Let's handle sign of bX:
                    if (bX > 0) {
                        // B ≤ floor(pX/bX).
                        long Bmax = pX / bX;
                        // Also B≥0.
                        // We have B = B0 + k*aXprime.
                        // We want to find some k so that 0 ≤ B ≤ Bmax.
                        // That is 0 ≤ B0 + k*aXprime ≤ Bmax.
                        // Solve inequalities for k:
                        // -B0/aXprime ≤ k ≤ (Bmax - B0)/aXprime
                        //
                        // k must be integer. Let's define:
                        long kMin = (long) Math.ceil(-(double) B0 / aXprime);
                        long kMax = (long) Math.floor((double) (Bmax - B0) / aXprime);

                        // Now we have a range for k. For each valid k, A=(pX - bX*B)/aX.
                        // We need to pick the k that minimizes cost=3A+B.
                        // Let's express cost in terms of k:
                        // A = (pX - bX*(B0 + k*aXprime))/aX
                        // = (pX - bX*B0 - bX*aXprime*k)/aX
                        // cost = 3A + B
                        // = 3((pX - bX*B0 - bX*aXprime*k)/aX) + (B0 + k*aXprime)
                        //
                        // Because A and B can be huge, iterating is not feasible. We need a direct
                        // minimization approach.

                        // cost(k) = 3((pX - bX*B0)/aX - (bX*aXprime/aX)*k) + B0 + k*aXprime
                        // = 3((pX - bX*B0)/aX) - 3(bX*aXprime/aX)*k + B0 + k*aXprime
                        // Let's factor k:
                        // cost(k) = [constant] + k(aXprime - 3(bX*aXprime/aX))
                        // = [constant] + k(aXprime(1 - 3bX/aX))
                        //
                        // Wait, bX*aXprime = bX*(aX/gcd)/??? We might get too complicated.

                        // To simplify, let's just try the two boundary values of k (kMin and kMax),
                        // because cost as a linear function of k will be unimodal in a bounded integer
                        // range (since we have linear constraints and linear cost).

                        // We'll try kMin and kMax (and maybe if needed one value in between) to find
                        // the minimal cost solution.

                        if (kMin > kMax) {
                            // No feasible k.
                            return -1;
                        }

                        long bestCost = -1;

                        // Check kMin and kMax:
                        long[] candidates = { kMin, kMax };
                        for (long k : candidates) {
                            if (k < kMin || k > kMax)
                                continue;
                            long Bcand = B0 + k * aXprime;
                            if (Bcand < 0 || Bcand > Bmax)
                                continue; // ensure within bounds
                            // Compute A:
                            long numeratorA = pX - bX * Bcand;
                            if (numeratorA < 0)
                                continue;
                            if (numeratorA % aX != 0)
                                continue; // must divide evenly
                            long Acand = numeratorA / aX;
                            if (Acand < 0)
                                continue;
                            long cost = 3 * Acand + Bcand;
                            if (bestCost < 0 || cost < bestCost) {
                                bestCost = cost;
                            }
                        }

                        return bestCost;
                    } else {
                        // If bX ≤ 0, then A≥0 => pX - bX*B≥0 => B≥pX/bX (since bX<0, dividing flips
                        // inequality).
                        // Similar reasoning applies. Let's handle negative bX similarly by flipping
                        // logic:

                        // If bX<0, A≥0 => B ≥ ceil(pX/bX). Because dividing by negative flips sign, be
                        // careful:
                        // pX/bX is negative or positive depending on sign of pX.
                        // We'll do a similar approach: find feasible range for B from A≥0 and from B≥0.
                        // This is symmetrical. Let's just normalize by searching for feasible B range
                        // and test kMin, kMax similarly.

                        // B≥0 anyway.
                        // A≥0 => pX - bX*B≥0 => bX*B ≤ pX. Since bX<0, dividing by negative number
                        // flips inequality:
                        // B≥ pX/bX doesn't make sense directly, let's rewrite:
                        // pX - bX*B≥0
                        // -bX*B ≥ -pX
                        // Since -bX>0 (bX<0), dividing:
                        // B ≤ pX/(-bX).

                        // Actually, let's solve step by step:
                        // A≥0 => pX ≥ bX*B
                        // Because bX<0, bX*B ≤0 for B≥0. pX≥0 presumably? Not necessarily, but prize
                        // coordinates plus offset are definitely positive, so pX>0.
                        // pX≥bX*B with bX<0 means bX*B ≤ pX, but since bX<0, for B≥0, bX*B ≤0. pX≥0 and
                        // large. This inequality pX≥bX*B (with bX<0) is always true for non-negative B
                        // if pX≥0, because bX*B ≤0 ≤pX. So the upper bound is not restrictive.

                        // The main restriction is A≥0 and divisibility. Actually, if bX<0, increasing B
                        // decreases pX - bX*B, so no immediate contradiction. We must still do the
                        // param approach:
                        // B = B0 + k*aXprime
                        // B≥0 => k≥ -B0/aXprime
                        // There's no upper bound from A≥0 side that is obvious. If pX≥0 and bX<0, as B
                        // grows large, pX - bX*B grows, making A large. This is good.
                        // The only limit might be cost minimization, which might push towards smaller
                        // values of B if possible.

                        // Without an upper bound, we must pick the smallest feasible B≥0 that gives a
                        // valid A≥0. The smallest feasible B≥0 can be found by choosing k so that B≥0
                        // and minimal.
                        // minimal B≥0 occurs at k = smallest integer k≥ -B0/aXprime.

                        long kMin = (long) Math.ceil((0.0 - B0) / (double) aXprime);
                        // We'll just take this minimal feasible k (and maybe kMin-1 to see if there's a
                        // cheaper solution if it exists and still feasible).

                        // Check kMin and kMin-1:
                        long bestCost = -1;
                        long[] candidates = { kMin, kMin - 1 };
                        for (long k : candidates) {
                            long Bcand = B0 + k * aXprime;
                            if (Bcand < 0)
                                continue;
                            // Check A:
                            long numeratorA = pX - bX * Bcand;
                            if (numeratorA < 0)
                                continue;
                            if (numeratorA % aX != 0)
                                continue;
                            long Acand = numeratorA / aX;
                            if (Acand < 0)
                                continue;
                            long cost = 3 * Acand + Bcand;
                            if (bestCost < 0 || cost < bestCost)
                                bestCost = cost;
                        }

                        return bestCost;
                    }

                } else {
                    // No solution
                    return -1;
                }
            } else {
                // No solution
                return -1;
            }
        } else {
            // D != 0, unique solution case
            long numeratorA = pX * bY - pY * bX;
            long numeratorB = aX * pY - aY * pX;

            // Check divisibility
            if (numeratorA % D != 0 || numeratorB % D != 0) {
                return -1;
            }

            long A = numeratorA / D;
            long B = numeratorB / D;

            if (A < 0 || B < 0) {
                return -1;
            }

            // cost = 3*A + B
            return 3 * A + B;
        }
    }

    private static List<Machine> parseMachines(List<String> lines) {
        var machines = new ArrayList<Machine>();
        var buffer = new ArrayList<String>();
        for (var line : lines) {
            if (line.isBlank()) {
                if (!buffer.isEmpty()) {
                    machines.add(parseOneMachine(buffer));
                    buffer.clear();
                }
            } else {
                buffer.add(line);
            }
        }
        if (!buffer.isEmpty()) {
            machines.add(parseOneMachine(buffer));
        }
        return machines;
    }

    private static Machine parseOneMachine(ArrayList<String> lines) {
        // Expect 3 lines:
        // Button A: X+..., Y+...
        // Button B: X+..., Y+...
        // Prize: X=..., Y=...

        var aLine = lines.get(0).strip();
        var afterAColon = aLine.split(":", 2)[1].trim();
        var partsA = afterAColon.split(",");
        long aX = parseCoordinate(partsA[0].trim());
        long aY = parseCoordinate(partsA[1].trim());

        var bLine = lines.get(1).strip();
        var afterBColon = bLine.split(":", 2)[1].trim();
        var partsB = afterBColon.split(",");
        long bX = parseCoordinate(partsB[0].trim());
        long bY = parseCoordinate(partsB[1].trim());

        var pLine = lines.get(2).strip();
        var afterPColon = pLine.split(":", 2)[1].trim();
        var partsP = afterPColon.split(",");
        long pX = parsePrizeCoordinate(partsP[0].trim());
        long pY = parsePrizeCoordinate(partsP[1].trim());

        return new Machine(aX, aY, bX, bY, pX, pY);
    }

    private static long parseCoordinate(String s) {
        // s like "X+94" or "Y+34"
        return Long.parseLong(s.substring(1));
    }

    private static long parsePrizeCoordinate(String s) {
        // s like "X=8400"
        return Long.parseLong(s.substring(2));
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }

    // Compute modular inverse of a modulo m using Extended Euclid, assuming
    // gcd(a,m)=1
    private static long modInverse(long a, long m) {
        long[] res = extendedEuclid(a, m);
        long g = res[0];
        long x = res[1];
        // long y = res[2]; // not needed
        if (g != 1) {
            throw new ArithmeticException("No inverse");
        }
        x = x % m;
        if (x < 0)
            x += m;
        return x;
    }

    // Extended Euclid: returns [g, x, y] such that a*x + b*y = g = gcd(a,b)
    private static long[] extendedEuclid(long a, long b) {
        if (b == 0)
            return new long[] { a, 1, 0 };
        long[] d = extendedEuclid(b, a % b);
        long g = d[0];
        long x = d[1];
        long y = d[2];
        return new long[] { g, y, x - (a / b) * y };
    }
}
