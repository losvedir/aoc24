package Day9;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day9Part1Gemini {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input/day9.txt"));
        System.out.println(compactAndCalculateChecksum(input));
    }

    public static BigInteger compactAndCalculateChecksum(String input) {
        List<Integer> fileSizes = new ArrayList<>();
        List<Integer> freeSpaceSizes = new ArrayList<>();

        // Parse input
        for (int i = 0; i < input.length(); i += 2) {
            fileSizes.add(Character.getNumericValue(input.charAt(i)));
            freeSpaceSizes.add(Character.getNumericValue(input.charAt(i + 1)));
        }

        // Compact the disk
        List<Integer> compactedDisk = compactDisk(fileSizes, freeSpaceSizes);

        // Calculate checksum
        return calculateChecksum(compactedDisk);
    }

    private static List<Integer> compactDisk(List<Integer> fileSizes, List<Integer> freeSpaceSizes) {
        List<Integer> compactedDisk = new ArrayList<>();
        int fileId = 0;
        for (int i = 0; i < fileSizes.size(); i++) {
            int fileSize = fileSizes.get(i);
            for (int j = 0; j < fileSize; j++) {
                compactedDisk.add(fileId);
            }
            fileId++;
            for (int j = 0; j < freeSpaceSizes.get(i); j++) {
                compactedDisk.add(-1); // -1 represents free space
            }
        }

        int freeSpaceIndex = compactedDisk.indexOf(-1);
        int lastFileIndex = compactedDisk.lastIndexOf(fileId - 1);

        while (freeSpaceIndex != -1 && freeSpaceIndex < lastFileIndex) {
            compactedDisk.set(freeSpaceIndex, compactedDisk.get(lastFileIndex));
            compactedDisk.set(lastFileIndex, -1);

            freeSpaceIndex = compactedDisk.indexOf(-1);
            lastFileIndex = compactedDisk.lastIndexOf(fileId - 1);
        }

        return compactedDisk;
    }

    private static BigInteger calculateChecksum(List<Integer> compactedDisk) {
        BigInteger checksum = BigInteger.ZERO;
        for (int i = 0; i < compactedDisk.size(); i++) {
            if (compactedDisk.get(i) != -1) {
                checksum = checksum.add(BigInteger.valueOf(i).multiply(BigInteger.valueOf(compactedDisk.get(i))));
            }
        }
        return checksum;
    }
}
