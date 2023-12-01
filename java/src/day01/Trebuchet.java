package day01;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Trebuchet {
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
        List<Integer> calibrationValues = new ArrayList<>();
        for (String line : input) {
            Integer twoDigitNumber = getTwoDigitNumber(line);
            calibrationValues.add(twoDigitNumber);
        }
        return calibrationValues.stream().mapToInt(Integer::intValue).sum();
    }

    private static Integer part2(List<String> input) {
        Map<String, String> replacmentNumbers = new HashMap<>();
        replacmentNumbers.put("one", "1");
        replacmentNumbers.put("two", "2");
        replacmentNumbers.put("three", "3");
        replacmentNumbers.put("four", "4");
        replacmentNumbers.put("five", "5");
        replacmentNumbers.put("six", "6");
        replacmentNumbers.put("seven", "7");
        replacmentNumbers.put("eight", "8");
        replacmentNumbers.put("nine", "9");
        List<Integer> calibrationValues = new ArrayList<>();
        for (String line : input) {
            String replacedLine = line;
            for (Map.Entry<String, String> entry : replacmentNumbers.entrySet()) {
                String number = entry.getKey();
                String value = entry.getValue();
                int firstIndex = line.indexOf(number);
                int lastIndex = line.lastIndexOf(number);
                if (firstIndex != -1) {
                    replacedLine = replacedLine.substring(0, firstIndex) + value + replacedLine.substring(firstIndex + 1);
                }
                if (lastIndex != -1) {
                    replacedLine = replacedLine.substring(0, lastIndex) + value + replacedLine.substring(lastIndex + 1);
                }
            }
            Integer twoDigitNumber = getTwoDigitNumber(replacedLine);
            calibrationValues.add(twoDigitNumber);
        }
        return calibrationValues.stream().mapToInt(Integer::intValue).sum();
    }

    private static Integer getTwoDigitNumber(String line) {
        Character firstDigit = null;
        Character lastDigit = null;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isDigit(c)) {
                if (firstDigit == null) {
                    firstDigit = c;
                }
                lastDigit = c;
            }
        }
        return Character.getNumericValue(firstDigit) * 10 + Character.getNumericValue(lastDigit);
    }

    public static List<Integer> findWord(String textString, String word) {
        List<Integer> indexes = new ArrayList<>();
        int index = 0;
        while (index != -1) {
            index = textString.indexOf(word, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }

    private static List<String> getFile() throws IOException {
        Path currentWorkingDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        String relativeFilePath = "inputs/day01/input.txt";
        Path absoluteFilePath = currentWorkingDir.resolve(relativeFilePath);
        try (Stream<String> input = Files.lines(absoluteFilePath)) {
            return input.toList();
        }
    }
}
