package Day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day9Part2o1Pro {

    static class FileRecord {
        int id;
        int startIndex;
        int length;

        FileRecord(int id, int startIndex, int length) {
            this.id = id;
            this.startIndex = startIndex;
            this.length = length;
        }
    }

    public static void main(String[] args) throws IOException {
        String line = Files.readString(Paths.get("input/day9.txt")).trim();

        // Parse the input line into file and free space segments (same logic as Part 1)
        int[] segments = new int[line.length()];
        for (int i = 0; i < line.length(); i++) {
            segments[i] = line.charAt(i) - '0';
        }

        // Calculate total size and file count
        int totalBlocks = 0;
        int fileCount = 0;
        for (int i = 0; i < segments.length; i += 2) {
            int fileLen = segments[i];
            totalBlocks += fileLen;
            fileCount++;
            if (i + 1 < segments.length) {
                int spaceLen = segments[i + 1];
                totalBlocks += spaceLen;
            }
        }

        // Build the initial disk array
        int[] disk = new int[totalBlocks];
        int fileID = 0;
        int idx = 0;
        for (int i = 0; i < segments.length; i += 2) {
            int fileLen = segments[i];
            for (int f = 0; f < fileLen; f++) {
                disk[idx++] = fileID;
            }
            fileID++;
            if (i + 1 < segments.length) {
                int spaceLen = segments[i + 1];
                for (int s = 0; s < spaceLen; s++) {
                    disk[idx++] = -1;
                }
            }
        }

        // Identify files and their locations
        // We know fileIDs range from 0 to fileCount-1
        // We will find the start index (leftmost block) and length of each file.
        FileRecord[] files = new FileRecord[fileCount];
        for (int fid = 0; fid < fileCount; fid++) {
            int start = -1;
            int length = 0;
            for (int i = 0; i < disk.length; i++) {
                if (disk[i] == fid) {
                    if (start == -1)
                        start = i;
                    length++;
                }
            }
            files[fid] = new FileRecord(fid, start, length);
        }

        // Now we attempt to move files in order of decreasing file ID.
        for (int fid = fileCount - 1; fid >= 0; fid--) {
            FileRecord file = files[fid];
            if (file.startIndex == -1 || file.length == 0) {
                // File doesn't exist? Possibly no length?
                // Just skip it.
                continue;
            }

            // We look for a contiguous run of free space entirely before file.startIndex
            // that can fit file.length blocks.
            // We should find the leftmost such run.
            int neededLength = file.length;
            int bestStart = -1; // The start of the chosen free run

            // Search from left to (file.startIndex - 1)
            // We'll iterate through disk from 0 to file.startIndex-1 and look for free
            // runs.
            int searchEnd = file.startIndex - 1;
            if (searchEnd < 0) {
                // No space to the left at all, can't move this file
                continue;
            }

            int runStart = -1;
            for (int i = 0; i <= searchEnd; i++) {
                if (disk[i] == -1) {
                    if (runStart == -1)
                        runStart = i;
                } else {
                    // End of a free run
                    if (runStart != -1) {
                        int runLen = i - runStart;
                        if (runLen >= neededLength) {
                            // We found a suitable run
                            bestStart = runStart;
                            break;
                        }
                        runStart = -1;
                    }
                }
            }
            // Check if we ended with a run at the very end
            if (bestStart == -1 && runStart != -1) {
                int runLen = (searchEnd + 1) - runStart;
                if (runLen >= neededLength) {
                    bestStart = runStart;
                }
            }

            // If we found a suitable run, move the entire file
            if (bestStart != -1) {
                // Clear original file location
                int oldStart = file.startIndex;
                int blocksCleared = 0;
                for (int i = oldStart; i < disk.length && blocksCleared < file.length; i++) {
                    if (disk[i] == fid) {
                        disk[i] = -1;
                        blocksCleared++;
                    }
                }

                // Place file blocks at bestStart
                for (int i = bestStart; i < bestStart + file.length; i++) {
                    disk[i] = fid;
                }

                // Update file startIndex
                file.startIndex = bestStart;
            }
        }

        // After all moves, compute the checksum
        long checksum = 0;
        for (int i = 0; i < disk.length; i++) {
            if (disk[i] != -1) {
                checksum += (long) i * disk[i];
            }
        }

        System.out.println(checksum);
    }
}
