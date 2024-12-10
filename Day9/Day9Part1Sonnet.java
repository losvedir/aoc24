package Day9;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day9Part1Sonnet {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("input/day9.txt")).trim();
        System.out.println("Checksum: " + solve(input));
    }

    public static BigInteger solve(String input) {
        // Parse the initial disk state
        List<FileBlock> files = parseInput(input);
        List<Integer> gaps = parseGaps(input);

        // Create the initial disk representation
        int totalLength = files.stream().mapToInt(f -> f.length).sum() +
                gaps.stream().mapToInt(i -> i).sum();
        int[] disk = new int[totalLength];
        int currentPos = 0;

        // Fill initial disk state
        for (int i = 0; i < files.size(); i++) {
            FileBlock file = files.get(i);
            // Fill file blocks
            for (int j = 0; j < file.length; j++) {
                disk[currentPos + j] = file.id;
            }
            currentPos += file.length;

            // Skip gap
            if (i < gaps.size()) {
                currentPos += gaps.get(i);
            }
        }

        // Compact the disk
        compactDisk(disk, files);

        // Calculate checksum
        return calculateChecksum(disk);
    }

    private static List<FileBlock> parseInput(String input) {
        List<FileBlock> files = new ArrayList<>();
        int fileId = 0;
        for (int i = 0; i < input.length(); i += 2) {
            int length = Character.getNumericValue(input.charAt(i));
            files.add(new FileBlock(fileId++, length));
        }
        return files;
    }

    private static List<Integer> parseGaps(String input) {
        List<Integer> gaps = new ArrayList<>();
        for (int i = 1; i < input.length(); i += 2) {
            gaps.add(Character.getNumericValue(input.charAt(i)));
        }
        return gaps;
    }

    private static void compactDisk(int[] disk, List<FileBlock> files) {
        for (int fileId = files.size() - 1; fileId >= 0; fileId--) {
            while (true) {
                // Find rightmost position of current file
                int rightmostPos = -1;
                for (int i = disk.length - 1; i >= 0; i--) {
                    if (disk[i] == fileId) {
                        rightmostPos = i;
                        break;
                    }
                }

                // Find leftmost free space before this file
                int leftmostGap = -1;
                for (int i = rightmostPos - 1; i >= 0; i--) {
                    if (disk[i] == 0 && (i == 0 || disk[i - 1] != 0)) {
                        leftmostGap = i;
                        break;
                    }
                }

                // If no gap found or file is already at leftmost position, move to next file
                if (leftmostGap == -1)
                    break;

                // Move the file one block left
                FileBlock file = files.get(fileId);
                for (int i = 0; i < file.length; i++) {
                    disk[leftmostGap + i] = fileId;
                    disk[rightmostPos - file.length + 1 + i] = 0;
                }
            }
        }
    }

    private static BigInteger calculateChecksum(int[] disk) {
        BigInteger checksum = BigInteger.ZERO;
        for (int i = 0; i < disk.length; i++) {
            if (disk[i] != 0) { // Skip free space
                checksum = checksum.add(BigInteger.valueOf((long) i * disk[i]));
            }
        }
        return checksum;
    }

    private static class FileBlock {
        final int id;
        final int length;

        FileBlock(int id, int length) {
            this.id = id;
            this.length = length;
        }
    }
}
