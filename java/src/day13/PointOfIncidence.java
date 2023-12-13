package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointOfIncidence {
    public static void main(String[] args) {
        try {
            List<String> inputs = getFile("inputs/day13/input.txt");
            System.out.println("Part 1: " + part1(inputs));
            System.out.println("Part 2: " + part2(inputs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1(List<String> inputs) {
        long result = 0;
        for (String input : inputs) {
            result += 100L * reflectionAcrossHorizontal(input);
            result += reflectionAcrossVertical(input);
        }

        return result;
    }

    private static long part2(List<String> inputs) {
        long result = 0;
        for (String input : inputs) {
            int verLine = 0;
            verLine = 100 * reflectionAcrossHorizontal2(input);
            int horLine = 0;
            horLine = reflectionAcrossVertical2(input);
            result += Math.max(verLine, horLine);
        }
        return result;
    }

    private static int reflectionAcrossVertical(String input) {
        List<String> columns = convertFromRowsToColumns(input);
        return reflectionAcrossLine(columns);
    }

    private static int reflectionAcrossHorizontal(String input) {
        List<String> rows = Arrays.stream(input.trim().split("\n")).toList();
        return reflectionAcrossLine(rows);
    }

    private static int reflectionAcrossVertical2(String input) {
        List<String> columns = convertFromRowsToColumns(input);
        return reflectionAcrossLineWithSmudge(columns);
    }

    private static int reflectionAcrossHorizontal2(String input) {
        List<String> rows = Arrays.stream(input.trim().split("\n")).toList();
        return reflectionAcrossLineWithSmudge(rows);
    }

    private static List<String> convertFromRowsToColumns(String input) {
        List<String> rows = Arrays.stream(input.trim().split("\n")).toList();
        List<List<Character>> columns = new ArrayList<>();
        for (int i = 0; i < rows.get(0).length(); i++) {
            List<Character> column = new ArrayList<>();
            columns.add(column);
        }

        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);
            for (int j = 0; j < row.length(); j++) {
                columns.get(j).add(row.charAt(j));
            }
        }
        List<String> convertToColumns = new ArrayList<>();
        for (List<Character> column : columns) {
            StringBuilder builder = new StringBuilder(column.size());
            for (Character ch : column) {
                builder.append(ch);
            }
            convertToColumns.add(builder.toString());
        }
        return convertToColumns;
    }

    private static int reflectionAcrossLine(List<String> rows) {
        for (int i = 1; i < rows.size(); i++) {
            boolean allEquals = true;
            for (int j = 0; j < i; j++) {
                if ((i + j) >= rows.size()) {
                    break;
                }
                if (!rows.get(i - j - 1).equals(rows.get(i + j))) {
                    allEquals = false;
                    break;
                }
            }
            if (allEquals) {
                return i;
            }
        }
        return 0;
    }

    private static int reflectionAcrossLineWithSmudge(List<String> rows) {
        int line = 0;
        for (int i = 1; i < rows.size(); i++) {
            boolean allEquals = false;
            int mistakesAllowed = 1;
            for (int j = 0; j < i; j++) {
                if ((i + j) >= rows.size()) {
                    break;
                }
                String str1 = rows.get(i - j - 1);
                String str2 = rows.get(i + j);
                if (!str1.equals(str2)) {
                    allEquals = true;
                    for (int z = 0; z < str1.length(); z++) {
                        if (str1.charAt(z) != str2.charAt(z)) {
                            mistakesAllowed--;
                            if (mistakesAllowed < 0) {
                                allEquals = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (allEquals) {
                return i;
            }
        }
        return line;
    }

    private static List<String> getFile(String file) throws IOException {
        String wholeInput = Files.readString(Path.of(file));
        return Arrays.stream(wholeInput.strip().split("\n\n")).toList();
    }
}
