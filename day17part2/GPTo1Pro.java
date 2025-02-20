package day17part2;

public class GPTo1Pro {
    public static void main(String[] args) {
        // The given program's output must match its own instructions:
        long[] desiredOutput = { 2, 4, 1, 3, 7, 5, 4, 1, 1, 3, 0, 3, 5, 5, 3, 0 };

        // Number of outputs = length of desiredOutput = 16
        int iterations = desiredOutput.length;

        // We know after producing all outputs, A_{17}=0
        long A_next = 0; // A_{17}

        // Work backward from i=16 down to i=1
        for (int i = iterations - 1; i >= 0; i--) {
            long output_i = desiredOutput[i];
            boolean found = false;
            long A_i_candidate = -1;

            // Try all r in [0..7]
            for (long r = 0; r < 8; r++) {
                long A_i = 8 * A_next + r;
                long E_i = (r ^ 3);
                // Compute the candidate output:
                // candidate_output = (r XOR (((A_i >> E_i) & 7)))
                long candidate_output = (r ^ ((A_i >> E_i) & 7));
                if (candidate_output == output_i) {
                    // Found a suitable A_i
                    A_i_candidate = A_i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No solution found going backward. Something's off.");
                return;
            }

            // Set A_next for next iteration (going backward)
            A_next = A_i_candidate;
        }

        // After loop, A_next is actually A_1
        // We want the smallest positive A_1. If the logic is correct, we got a unique
        // solution.
        System.out.println("The smallest positive initial A that works is: " + A_next);
    }
}
