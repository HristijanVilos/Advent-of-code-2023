package day06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaitForIt {
    private static final String FILEPATH = "inputs/day06/input.txt";

    public static void main(String[] args) {
        try {
            List<List<Long>> timeAndDistance = getFilePart1();
            List<Long> timeAndDistance2 = getFilePart2();
            System.out.println("Part 1: " + part1(timeAndDistance));
            System.out.println("Part 2: " + part2(timeAndDistance2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int part1(List<List<Long>> timeAndDistance) {
        int numberOfRaces = timeAndDistance.get(0).size();
        List<Integer> listOfNumOfWays = new ArrayList<>();
        for (int i = 0; i < numberOfRaces; i++) {
            long time = timeAndDistance.get(0).get(i);
            long distance = timeAndDistance.get(1).get(i);
            int numOfWaysToBeatRecord = numberOfWaysToWin(time, distance);
            listOfNumOfWays.add(numOfWaysToBeatRecord);
        }
        int result = 1;
        for (int num : listOfNumOfWays) {
            result *= num;
        }
        return result;
    }

    private static long part2(List<Long> timeAndDistance) {
        long time = timeAndDistance.get(0);
        long distance = timeAndDistance.get(1);
        return numberOfWaysToWin(time, distance);
    }

    private static int numberOfWaysToWin(long time, long distance) {
        int numOfWaysToBeatRecord = 0;
        for (int j = 1; j < time; j++) {
            long remainingTime = time - j;
            if (j * remainingTime > distance) {
                numOfWaysToBeatRecord += 1;
            }
        }
        return numOfWaysToBeatRecord;
    }

    private static List<List<Long>> getFilePart1() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            String line;
            List<List<Long>> timeAndDistance = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                List<Long> timeOrDistance =
                        Arrays.stream(line.strip().split(": ")[1].strip().split("\\s+"))
                                .map(Long::valueOf).toList();
                timeAndDistance.add(timeOrDistance);
            }
            return timeAndDistance;
        }
    }

    private static List<Long> getFilePart2() throws IOException {
        List<Long> timeAndDistance = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                List<String> test = Arrays.stream(line.split(": ")[1].strip().split("\\s+")).toList();
                Long result = Long.valueOf(String.join("", test));
                timeAndDistance.add(result);
            }
        }
        return timeAndDistance;
    }
}
