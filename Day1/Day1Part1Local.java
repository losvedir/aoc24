package Day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1Part1Local {

    private static final int TARGET_DISTANCE = 50;
    private static final String INPUT_FILE_NAME = "input/day1.txt";

    public static void main(String[] args) throws FileNotFoundException {
        List<String> inputList = readInput(INPUT_FILE_NAME);
        int totalDistance = calculateTotalDistance(inputList);
        System.out.println("The total distance is: " + totalDistance);

        if (totalDistance >= TARGET_DISTANCE) {
            System.out.println("We've reached the target distance of " + TARGET_DISTANCE);
        }
    }

    private static List<String> readInput(String fileName) throws FileNotFoundException {
        List<String> inputList = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                inputList.add(scanner.nextLine().trim());
            }
        } catch (Exception e) {
            // throw new FileNotFoundException("Could not read file: " + fileName, e);
        }
        return inputList;
    }

    private static int calculateTotalDistance(List<String> inputList) {
        return calculateTotalDistance(inputList, 0);
    }

    private static int calculateTotalDistance(List<String> inputList, int index) {
        if (index >= inputList.size() / 2 - 1 || index < 0) {
            return 0;
        }
        String left = inputList.get(index);
        String right = inputList.get(2 * index + 1);

        int totalDistance = Math.abs(Integer.parseInt(left) - Integer.parseInt(right));

        return totalDistance + calculateTotalDistance(inputList, index + 1);
    }
}
