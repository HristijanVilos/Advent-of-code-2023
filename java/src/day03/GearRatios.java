package day03;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class GearRatios {
    public static void main(String[] args) {
        try {
            List<String> input = getFile();
            System.out.println("Part 1: " + part1(input));
            System.out.println("Part 2: " + part2(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Integer part1(List<String> input) {
        int result = 0;
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            StringBuilder number = new StringBuilder();
            List<int[]> indexes = new ArrayList<>();
            for (int j = 0; j < line.length(); j++) {
                char digit = line.charAt(j);
                if (Character.isDigit(digit)) {
                    number.append(digit);
                    int[] ind = {i, j};
                    indexes.add(ind);
                } else if (!number.isEmpty()) {
                    if (isAdjacentToSymbol(input, indexes)) {
                        result += Integer.parseInt(number.toString());
                    }
                    number = new StringBuilder();
                    indexes = new ArrayList<>();
                }
            }
            if (isAdjacentToSymbol(input, indexes)) {
                result += Integer.parseInt(number.toString());
            }
        }
        return result;
    }

    private static Integer part2(List<String> input) {
        int result = 0;
        Map<String, List<Integer>> numbNextToStars = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            StringBuilder number = new StringBuilder();
            List<int[]> indexes = new ArrayList<>();
            for (int j = 0; j < line.length(); j++) {
                char digit = line.charAt(j);
                if (Character.isDigit(digit)) {
                    number.append(digit);
                    int[] ind = {i, j};
                    indexes.add(ind);
                } else if (!number.isEmpty()) {
                    indexNextToStar(input, indexes, numbNextToStars, Integer.parseInt(number.toString()));
                    number = new StringBuilder();
                    indexes = new ArrayList<>();
                }
            }
            if (!number.isEmpty()) {
                indexNextToStar(input, indexes, numbNextToStars, Integer.parseInt(number.toString()));
            }
        }
        for (List<Integer> numbers : numbNextToStars.values()) {
            if (numbers.size() == 2) {
                result += numbers.get(0) * numbers.get(1);
            }
        }
        return result;
    }

    private static boolean isAdjacentToSymbol(List<String> input, List<int[]> indexes) {
        Set<List<Integer>> setAdjPos = setAdjacentPositions(input, indexes);
        for (List<Integer> pos : setAdjPos) {
            Character sym = input.get(pos.get(0)).charAt(pos.get(1));
            Character dot = '.';
            if (!Character.isDigit(sym) && !sym.equals(dot)) {
                return true;
            }
        }
        return false;
    }

    private static void indexNextToStar(List<String> input, List<int[]> indexes, Map<String, List<Integer>> numbNextToStars, Integer number) {
        Set<List<Integer>> setAdjPos = setAdjacentPositions(input, indexes);
        for (List<Integer> pos : setAdjPos) {
            Character sym = input.get(pos.get(0)).charAt(pos.get(1));
            Character star = '*';
            if (sym.equals(star)) {
                if (numbNextToStars.get(pos.toString()) != null) {
                    numbNextToStars.get(pos.toString()).add(number);
                } else {
                    List<Integer> numbers = new ArrayList<>();
                    numbers.add(number);
                    numbNextToStars.put(pos.toString(), numbers);
                }
            }
        }
    }

    private static Set<List<Integer>> setAdjacentPositions(List<String> input, List<int[]> indexes) {
        int mapHeight = input.size();
        int mapWidth = input.size();
        Set<List<Integer>> setAdjPos = new HashSet<>();
        for (int[] position : indexes) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int x = i + position[0];
                    int y = j + position[1];
                    if (x >= 0 && x < mapHeight
                            && y >= 0 && y < mapWidth) {
                        List<Integer> adjPos = new ArrayList<>();
                        adjPos.add(x);
                        adjPos.add(y);
                        setAdjPos.add(adjPos);
                    }
                }
            }
        }
        return setAdjPos;
    }

    private static List<String> getFile() throws IOException {
        Path currentWorkingDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        String relativeFilePath = "inputs/day03/input.txt";
        Path absoluteFilePath = currentWorkingDir.resolve(relativeFilePath);
        try (Stream<String> input = Files.lines(absoluteFilePath)) {
            return input.toList();
        }
    }
}
