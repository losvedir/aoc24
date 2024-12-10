package Day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day9Part1Opus {
    public static void main(String[] args) {
        try {
            String input = Files.readString(Path.of("input/day9.txt")).trim();
            int checksum = calculateChecksum(input);
            System.out.println("Resulting filesystem checksum: " + checksum);
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }

    private static int calculateChecksum(String input) {
        List<Integer> fileSizes = new ArrayList<>();
        List<Integer> freeSpaces = new ArrayList<>();

        // Parse the input and populate fileSizes and freeSpaces lists
        boolean isFileSize = true;
        StringBuilder currentSize = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                currentSize.append(c);
            } else {
                int size = Integer.parseInt(currentSize.toString());
                if (isFileSize) {
                    fileSizes.add(size);
                } else {
                    freeSpaces.add(size);
                }
                isFileSize = !isFileSize;
                currentSize = new StringBuilder();
            }
        }

        // Compact the disk
        List<Integer> compactedDisk = new ArrayList<>();
        int freeSpaceIndex = 0;
        for (int fileSize : fileSizes) {
            while (fileSize > 0) {
                int availableSpace = freeSpaces.get(freeSpaceIndex);
                int spaceToUse = Math.min(fileSize, availableSpace);
                compactedDisk.add(spaceToUse);
                fileSize -= spaceToUse;
                freeSpaces.set(freeSpaceIndex, availableSpace - spaceToUse);
                if (freeSpaces.get(freeSpaceIndex) == 0) {
                    freeSpaceIndex++;
                }
            }
        }

        // Calculate the checksum
        int checksum = 0;
        int fileId = 0;
        for (int i = 0; i < compactedDisk.size(); i++) {
            int blockSize = compactedDisk.get(i);
            if (blockSize > 0) {
                checksum += i * fileId;
                fileId++;
            }
        }

        return checksum;
    }
}
