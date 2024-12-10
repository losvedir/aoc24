package Day9;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Advent of Code - Day 9 Part 1 (Original Problem)
 * 
 * This solution:
 * 1. Reads a single line of input representing a disk layout using a compact
 * format:
 * - The string alternates between file length and free length digits.
 * - For example: "12345" means:
 * file:1-block, free:2-block, file:3-block, free:4-block, file:5-block
 * - Another example "90909" means:
 * file:9-block, free:0-block, file:9-block, free:0-block, file:9-block (three
 * files of 9-blocks each)
 *
 * 2. Converts the compact representation into an array of characters, where:
 * - Each file segment is assigned an ID (0-based, increment for each file
 * segment encountered),
 * and each block of that file is represented by a digit character (the file
 * ID).
 * - Free space blocks are represented by '.'.
 *
 * 3. Performs the compaction process:
 * - While there exists a free block ('.') that is not strictly at the end
 * (i.e., there are still file blocks to the right of it),
 * move one file block from the rightmost end of the disk into that free block
 * position.
 * - This is done one block at a time, always taking from the rightmost file
 * block and putting it into the leftmost free block that occurs before the disk
 * ends.
 *
 * 4. After no more internal gaps remain, compute the checksum:
 * - For each file block at position i (0-based), add i * (fileID) to the
 * checksum.
 * - Skip free blocks.
 *
 * 5. Print the resulting checksum.
 */
public class Day9Part1o1 {
    public static void main(String[] args) throws IOException {
        String line = Files.readString(Paths.get("input/day9.txt")).trim();

        // Parse the input line into file and free segments
        // The input alternates: file length, free length, file length, free length, ...
        // Index 0: file length
        // Index 1: free length
        // Index 2: file length
        // Index 3: free length
        // and so forth
        ArrayList<Character> disk = new ArrayList<>();

        int fileId = 0; // ID for each file segment encountered
        for (int i = 0; i < line.length(); i++) {
            int length = line.charAt(i) - '0';
            if (i % 2 == 0) {
                // even index: file length
                if (length > 0) {
                    // Add 'length' blocks of file with ID 'fileId'
                    char fileChar = fileIdToChar(fileId);
                    for (int j = 0; j < length; j++) {
                        disk.add(fileChar);
                    }
                    fileId++;
                } else {
                    // zero-length file means no blocks, but still consider it a "file segment"
                    // The problem states something like "90909" means three 9-block files in a row.
                    // Actually, a zero-length file segment means no blocks are added,
                    // but we still encountered a "file length" entry.
                    // Wait, the examples: "90909" => 9 file, 0 free, 9 file, 0 free, 9 file
                    // There are 3 file segments: each of length 9. No zero-length file segment is
                    // given.
                    // If length = 0 here, that means no file blocks. According to problem
                    // statement,
                    // the ID increments with each file segment (since each even index is a file
                    // segment),
                    // even if length=0, it's still a file segment (just empty).
                    // We must confirm with given examples. The problem states "The digits alternate
                    // between indicating the length of a file and the length of free space."
                    // Actually, a zero-length file would be a degenerate case. Let's assume we must
                    // still increment the file ID, because it's a new file segment, just empty.
                    fileId++;
                }
            } else {
                // odd index: free length
                for (int j = 0; j < length; j++) {
                    disk.add('.');
                }
            }
        }

        // Now we have a full layout of the disk as a list of chars
        // Next: Compact the disk
        compactDisk(disk);

        // Compute the checksum
        BigInteger checksum = BigInteger.ZERO;
        for (int i = 0; i < disk.size(); i++) {
            char c = disk.get(i);
            if (c != '.') {
                int fid = charToFileId(c);
                checksum = checksum.add(BigInteger.valueOf(i).multiply(BigInteger.valueOf(fid)));
            }
        }

        System.out.println(checksum);
    }

    /**
     * Convert file ID to character.
     * File IDs can grow large since the input can be long.
     * The problem doesn't explicitly limit file IDs, but given the puzzle nature,
     * let's assume single-digit IDs are insufficient.
     * 
     * However, the examples show file IDs as digits (0,1,2,...9) in the ASCII art.
     * We must be careful. The problem states "Using one character for each block
     * where digits are the file ID".
     * If the number of files exceeds 10, what to do? The puzzle examples show large
     * disk.
     * It's possible to have more than 10 files. The problem doesn't say we cannot
     * have that.
     *
     * Let's just map fileId to a character by:
     * - 0-9 -> '0'-'9'
     * - 10-35 -> 'A'-'Z'
     * If more than 36 files, we might need more logic, but let's assume the input
     * won't exceed that.
     */
    private static char fileIdToChar(int fileId) {
        if (fileId < 10) {
            return (char) ('0' + fileId);
        } else if (fileId < 36) {
            return (char) ('A' + (fileId - 10));
        } else {
            // Arbitrary fallback for very large number of files.
            // It's highly unlikely the puzzle input would exceed this,
            // but let's just do something safe:
            return (char) ('a' + (fileId - 36) % 26);
        }
    }

    private static int charToFileId(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'A' && c <= 'Z') {
            return 10 + (c - 'A');
        } else if (c >= 'a' && c <= 'z') {
            return 36 + (c - 'a');
        } else {
            throw new IllegalArgumentException("Unexpected file character: " + c);
        }
    }

    /**
     * Compacting the disk:
     * While there is a '.' that is not at the trailing end (meaning after it there
     * are file blocks),
     * move one file block from the rightmost end of the disk into that '.'
     * position.
     */
    private static void compactDisk(ArrayList<Character> disk) {
        while (true) {
            int leftmostGap = findLeftmostGap(disk);
            if (leftmostGap == -1) {
                // No internal gap found
                break;
            }
            int rightmostFile = findRightmostFile(disk);
            if (rightmostFile == -1) {
                // No file blocks at the end? Then no moves possible
                break;
            }

            // Move the rightmost file block to this gap
            char fileChar = disk.get(rightmostFile);
            disk.set(leftmostGap, fileChar);
            disk.set(rightmostFile, '.');
        }
    }

    /**
     * Find the leftmost '.' that has at least one file block to its right.
     * If no such gap exists, return -1.
     */
    private static int findLeftmostGap(ArrayList<Character> disk) {
        // To be considered a gap, there must be a '.' that comes before any file block
        // on its right.
        // A simple approach:
        // Scan from left to right. For each '.', check if there's a file block to the
        // right.
        // But checking each '.' for file on the right would be O(n^2).
        // Let's do it more efficiently:
        // Find the position of the last file block. If there's no file block at all, no
        // gap.
        // If there's a '.' before that position, that's a potential gap.
        // We want the leftmost '.' that is before the last file block.
        //
        // So:
        // 1) Find index of rightmost file (if none, return -1)
        // 2) Then scan from left to right for '.' until we find one that is strictly
        // less than rightmostFile index.
        int rightmostFile = findRightmostFile(disk);
        if (rightmostFile == -1)
            return -1; // no files, no gap

        for (int i = 0; i < rightmostFile; i++) {
            if (disk.get(i) == '.') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the rightmost file block.
     * Return its index, or -1 if none found.
     */
    private static int findRightmostFile(ArrayList<Character> disk) {
        for (int i = disk.size() - 1; i >= 0; i--) {
            if (disk.get(i) != '.') {
                return i;
            }
        }
        return -1;
    }
}
