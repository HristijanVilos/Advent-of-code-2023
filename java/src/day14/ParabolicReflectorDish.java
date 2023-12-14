package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ParabolicReflectorDish {
    private static final Map<String, Integer> seen = new HashMap<>();

    public static void main(String[] args) {
        try {
            String file = "inputs/day14/input.txt";
            System.out.println("Part 1: " + part1(file));
            System.out.println("Part 2: " + part2(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1(String file) throws IOException {
        List<List<Character>> rows = getFile(file);
        List<List<Character>> columns = rotateRowsToColumnsAndCoulmnsToRows(rows);
        return loadOnBeamNorth(columns);
    }

    private static int part2(String file) throws IOException {
        int result = 0;
        List<List<Character>> dish = getFile(file);
        boolean notChanged = true;
        for (int i = 1; i <= 1_000_000_000; i++) {
            dish = rotateRowsToColumnsAndCoulmnsToRows(dish);
            loadOnBeamNorth(dish);
            dish = rotateRowsToColumnsAndCoulmnsToRows(dish);
            loadOnBeamWest(dish);
            dish = rotateRowsToColumnsAndCoulmnsToRows(dish);
            loadOnBeamSouth(dish);
            dish = rotateRowsToColumnsAndCoulmnsToRows(dish);
            result = loadOnBeamEast(dish);
            String dishStr = convertToString(dish);
            if (seen.get(dishStr) != null && notChanged) {
                int x = (1_000_000_000 - i) % (i - seen.get(dishStr));
                i = 1_000_000_000 - x;
                notChanged = false;
            } else {
                seen.put(dishStr, i);
            }
        }
        return result;
    }

    private static int loadOnBeamEast(List<List<Character>> rows) {
        int result = 0;
        for (int z = 0; z < rows.size(); z++) {
            List<Character> row = rows.get(z);
            for (int i = row.size() - 1; i > 0; i--) {
                Character thing = row.get(i);
                if (thing == 'O' || thing == '#') {
                    if (thing == 'O') {
                        result += rows.size() - z;
                    }
                    continue;
                }
                for (int j = i - 1; j >= 0; j--) {
                    Character secondThing = row.get(j);
                    if (secondThing == 'O') {
                        row.set(i, secondThing);
                        row.set(j, thing);
                        result += rows.size() - z;
                        break;
                    } else if (secondThing == '#') {
                        break;
                    }
                }
            }
            if (row.getFirst() == 'O') {
                result += rows.size() - z;
            }
        }
        return result;
    }

    private static void loadOnBeamSouth(List<List<Character>> columns) {
        for (List<Character> column : columns) {
            for (int i = column.size() - 1; i > 0; i--) {
                Character thing = column.get(i);
                if (thing == 'O' || thing == '#') {
                    continue;
                }
                for (int j = i - 1; j >= 0; j--) {
                    Character secondThing = column.get(j);
                    if (secondThing == 'O') {
                        column.set(i, secondThing);
                        column.set(j, thing);
                        break;
                    } else if (secondThing == '#') {
                        break;
                    }
                }
            }
        }
    }

    private static void loadOnBeamWest(List<List<Character>> rows) {
        for (int z = 0; z < rows.size(); z++) {
            List<Character> row = rows.get(z);
            for (int i = 0; i < row.size() - 1; i++) {
                Character thing = row.get(i);
                if (thing == 'O' || thing == '#') {
                    continue;
                }
                for (int j = i + 1; j < row.size(); j++) {
                    Character secondThing = row.get(j);
                    if (secondThing == 'O') {
                        row.set(i, secondThing);
                        row.set(j, thing);
                        break;
                    } else if (secondThing == '#') {
                        break;
                    }
                }
            }
        }
    }

    private static long loadOnBeamNorth(List<List<Character>> columns) {
        long result = 0;
        for (List<Character> column : columns) {
            for (int i = 0; i < column.size() - 1; i++) {
                Character thing = column.get(i);
                if (thing == 'O' || thing == '#') {
                    if (thing == 'O') {
                        result += column.size() - i;
                    }
                    continue;
                }
                for (int j = i + 1; j < column.size(); j++) {
                    Character secondThing = column.get(j);
                    if (secondThing == 'O') {
                        column.set(i, secondThing);
                        column.set(j, thing);
                        result += column.size() - i;
                        break;
                    } else if (secondThing == '#') {
                        break;
                    }
                }
            }
            if (column.getLast() == 'O') {
                result += 1;
            }
        }
        return result;
    }

    private static String convertToString(List<List<Character>> lists) {
        StringBuilder builder = new StringBuilder();
        for (List<Character> characters : lists) {
            for (char ch : characters) {
                builder.append(ch);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static List<List<Character>> rotateRowsToColumnsAndCoulmnsToRows(List<List<Character>> rows) {
        List<List<Character>> columns = new ArrayList<>();
        for (int i = 0; i < rows.get(0).size(); i++) {
            columns.add(new ArrayList<>());
        }
        for (int i = 0; i < rows.size(); i++) {
            List<Character> row = rows.get(i);
            for (int j = 0; j < columns.size(); j++) {
                columns.get(j).add(row.get(j));
            }
        }
        return columns;
    }

    private static List<List<Character>> getFile(String file) throws IOException {
        String wholeInput = Files.readString(Path.of(file));
        List<List<Character>> rowsAsChar = new ArrayList<>();
        List<String> rows = Arrays.stream(wholeInput.trim().split("\n")).toList();
        for (String row : rows) {
            List<Character> rowAsChar = new ArrayList<>();
            for (char ch : row.toCharArray()) {
                rowAsChar.add(ch);
            }
            rowsAsChar.add(rowAsChar);
        }
        return rowsAsChar;
    }
}

