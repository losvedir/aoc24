package Day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day9Part1o1Pro {
    public static void main(String[] args) throws IOException {
        String line = Files.readString(Paths.get("input/day9.txt")).trim();

        // Parse the input line into a sequence of file and free space segments.
        // The digits alternate between file length and free space length.
        // The pattern is: fileLen_0, freeLen_0, fileLen_1, freeLen_1, ... etc.
        // The first digit is a file length, the second is free space length, and so on.
        // We'll construct an array representing the disk: -1 for free space, or file ID
        // for file blocks.

        // First, parse into arrays:
        // We'll read pairs of digits from the input line. If the input length is odd,
        // the last digit will be a file length
        // with no trailing free space (meaning no free space segment at the end).

        int[] segments = new int[line.length()];
        for (int i = 0; i < line.length(); i++) {
            segments[i] = line.charAt(i) - '0';
        }

        // Count total length of the disk and file count
        // We alternate: even indices in segments[] are file lengths, odd indices are
        // space lengths (if present).

        int totalBlocks = 0;
        int fileCount = 0;
        for (int i = 0; i < segments.length; i += 2) {
            int fileLen = segments[i];
            totalBlocks += fileLen;
            fileCount++;
            // Check if there's a free space segment following this file:
            if (i + 1 < segments.length) {
                int spaceLen = segments[i + 1];
                totalBlocks += spaceLen;
            }
        }

        // Create the disk array
        int[] disk = new int[totalBlocks];

        // Fill the disk array
        // file ID starts at 0
        int fileID = 0;
        int index = 0;
        for (int i = 0; i < segments.length; i += 2) {
            int fileLen = segments[i];
            // Place file blocks with current fileID
            for (int f = 0; f < fileLen; f++) {
                disk[index++] = fileID;
            }
            fileID++;
            if (i + 1 < segments.length) {
                int spaceLen = segments[i + 1];
                // Place free spaces as -1
                for (int s = 0; s < spaceLen; s++) {
                    disk[index++] = -1;
                }
            }
        }

        // Now we perform the compaction:
        // Move file blocks one at a time from the end of the disk to the leftmost free
        // space block until no gaps remain.
        //
        // A "gap" means a free space that occurs before a file block somewhere to the
        // right.
        // We'll repeat:
        // 1. Find the first free space from the left that has at least one file block
        // after it.
        // 2. Find the last file block from the right.
        // 3. Move that file block to fill the free space.
        // 4. Repeat until no such gap exists.

        // To speed up the checking, we can do a loop until stable.
        // But straightforward approach: Just do while true:

        while (true) {
            // Find the first gap that has a file block after it
            int gapPos = -1;
            boolean hasGap = false;

            // We'll also track if there's any file block to the right of a given position
            // to quickly determine if a gap is valid
            // One way: scan from right to left to find if there's a file block to the
            // right.
            // But let's do a simple approach:
            // We'll find the first gap from the left and then check if there's a file block
            // to the right.

            // find first '.' from the left
            for (int i = 0; i < disk.length; i++) {
                if (disk[i] == -1) {
                    // Check if there's a file block after this position
                    boolean blockAfter = false;
                    for (int j = i + 1; j < disk.length; j++) {
                        if (disk[j] != -1) {
                            blockAfter = true;
                            break;
                        }
                    }
                    if (blockAfter) {
                        gapPos = i;
                        hasGap = true;
                        break;
                    }
                }
            }

            if (!hasGap) {
                // No gap that needs fixing
                break;
            }

            // Find the last file block from the right
            int lastFilePos = -1;
            for (int i = disk.length - 1; i >= 0; i--) {
                if (disk[i] != -1) {
                    lastFilePos = i;
                    break;
                }
            }

            // Move that last file block to the gap
            if (lastFilePos == -1) {
                // No file blocks at all? Then no movement needed, break.
                break;
            }

            int fileToMove = disk[lastFilePos];
            disk[gapPos] = fileToMove;
            disk[lastFilePos] = -1;
        }

        // After compaction is done, compute checksum:
        // "add up the result of multiplying each of these blocks' position with the
        // file ID number it contains"
        // skip free spaces
        long checksum = 0;
        for (int i = 0; i < disk.length; i++) {
            if (disk[i] != -1) {
                checksum += (long) i * disk[i];
            }
        }

        System.out.println(checksum);
    }
}
