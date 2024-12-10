package Day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day9Part14o {

    public static void main(String[] args) {
        try {
            String diskMap = Files.readString(Paths.get("input/day9.txt")).trim();
            System.out.println("Checksum: " + calculateChecksum(diskMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calculateChecksum(String diskMap) {
        List<Block> blocks = parseDiskMap(diskMap);
        char[] diskState = createInitialDiskState(blocks);

        // Perform compaction
        compactFiles(diskState);

        // Calculate checksum
        return calculateChecksum(diskState);
    }

    private static List<Block> parseDiskMap(String diskMap) {
        List<Block> blocks = new ArrayList<>();
        boolean isFile = true;
        int fileId = 0;

        for (int i = 0; i < diskMap.length(); i++) {
            int length = Character.getNumericValue(diskMap.charAt(i));
            if (isFile) {
                blocks.add(new Block(length, fileId++));
            }
            isFile = !isFile;
        }

        return blocks;
    }

    private static char[] createInitialDiskState(List<Block> blocks) {
        StringBuilder diskBuilder = new StringBuilder();
        for (Block block : blocks) {
            if (block.id != -1) {
                diskBuilder.append(String.valueOf(block.id).repeat(block.length));
            } else {
                diskBuilder.append(".".repeat(block.length));
            }
        }
        return diskBuilder.toString().toCharArray();
    }

    private static void compactFiles(char[] diskState) {
        int writePosition = 0;
        for (int readPosition = 0; readPosition < diskState.length; readPosition++) {
            if (diskState[readPosition] != '.') {
                diskState[writePosition++] = diskState[readPosition];
            }
        }
        // Fill remaining positions with free space
        while (writePosition < diskState.length) {
            diskState[writePosition++] = '.';
        }
    }

    private static int calculateChecksum(char[] diskState) {
        int checksum = 0;
        for (int i = 0; i < diskState.length; i++) {
            if (diskState[i] != '.') {
                int fileId = Character.getNumericValue(diskState[i]);
                checksum += i * fileId;
            }
        }
        return checksum;
    }

    static class Block {
        int length;
        int id;

        Block(int length, int id) {
            this.length = length;
            this.id = id;
        }
    }
}
