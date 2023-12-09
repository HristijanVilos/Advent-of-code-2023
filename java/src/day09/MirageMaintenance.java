package day09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MirageMaintenance {
    private static final String FILEPATH = "inputs/day09/input.txt";

    public static void main(String[] args) {
        try {
            List<List<Integer>> input = getFile();
            System.out.println("Part 1: " + part1(input));
            System.out.println("Part 2: " + part2(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1(List<List<Integer>> input) {
        long result = 0;
        for (List<Integer> listInt : input) {
            List<Integer> resultList = new ArrayList<>();
            List<Integer> current = listInt;
            resultList.add(current.get(current.size() - 1));
            boolean notAllZeros = true;
            while (notAllZeros) {
                notAllZeros = false;
                List<Integer> newList = new ArrayList<>();
                for (int i = 0; i < current.size() - 1; i++) {
                    int element = current.get(i + 1) - current.get(i);
                    newList.add(element);
                    if (element != 0) {
                        notAllZeros = true;
                    }
                }
                resultList.add(newList.get(newList.size() - 1));
                current = newList;
            }
            for (Integer res : resultList) {
                result += res;
            }
        }
        return result;
    }

    private static long part2(List<List<Integer>> input) {
        long result = 0;
        for (List<Integer> listInt : input) {
            List<Integer> resultList = new ArrayList<>();
            List<Integer> current = listInt;
            resultList.add(current.get(0));
            boolean notAllZeros = true;
            while (notAllZeros) {
                notAllZeros = false;
                List<Integer> newList = new ArrayList<>();
                for (int i = 0; i < current.size() - 1; i++) {
                    int element = current.get(i + 1) - current.get(i);
                    newList.add(element);
                    if (element != 0) {
                        notAllZeros = true;
                    }
                }
                resultList.add(newList.get(0));
                current = newList;
            }
            resultList = resultList.reversed();
            int curFirst = 0;
            int next = 0;
            for (Integer res : resultList) {
                next = res - curFirst;
                curFirst = next;
            }
            result += next;
        }
        return result;
    }


    private static List<List<Integer>> getFile() throws IOException {
        List<List<Integer>> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                input.add(Arrays.stream(line.strip().split("\\s+")).map(Integer::valueOf).toList());
            }
        }
        return input;
    }
}
