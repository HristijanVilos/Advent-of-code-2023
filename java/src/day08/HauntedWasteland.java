package day08;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HauntedWasteland {
    private static List<String> directions = new ArrayList<>();
    private static String going = "";
    private static final Map<String, List<String>> instructions = new HashMap<>();

    public static void main(String[] args) {
        try {
            processFile("inputs/day08/input.txt");
            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1() {
        long result = 0;
        going = "AAA";
        while (!going.equals("ZZZ")) {
            List<String> instruction = instructions.get(going);
            result = followInstructions(instruction, result);
        }
        return result;
    }

    private static long part2() {
        List<String> positions = new ArrayList<>();
        for (String key : instructions.keySet()) {
            if (key.endsWith("A")) {
                positions.add(key);
            }
        }
        Integer dirSize = directions.size();
        Map<String, Long> seen = new HashMap<>();
        return findCycle(positions, seen, dirSize);
    }

    private static long findCycle(List<String> positions, Map<String, Long> seen, Integer dirSize) {
        Map<String, Long> cycle = new HashMap<>();
        for (String position : positions) {
            List<String> instruction = instructions.get(position);
            long result = 0;
            while (seen.get(position) == null || seen.get(position) != result % dirSize) {
                if (directions.get((int) (result % dirSize)).equals("L")) {
                    position = instruction.get(0);
                } else {
                    position = instruction.get(1);
                }
                result += 1;
                if (position.endsWith("Z"))
                    seen.put(position, result % dirSize);
                instruction = instructions.get(position);
            }
            cycle.put(position, result);
        }
        Long finalRes = 1L;
        for (Long value : cycle.values()) {
            finalRes = lcm(finalRes, value);
        }
        return finalRes;
    }

    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static long followInstructions(List<String> instruction, long result) {
        for (String direction : directions) {
            if (direction.equals("L")) {
                going = instruction.get(0);
            } else {
                going = instruction.get(1);
            }
            result += 1;
            if (going.equals("ZZZ")) {
                return result;
            }
            instruction = instructions.get(going);
        }
        return result;
    }

    private static void processFile(String file) throws IOException {
        Path currentWorkingDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        Path absoluteFilePath = currentWorkingDir.resolve(file);
        String content = new String(Files.readAllBytes(absoluteFilePath));
        String[] separateDirFromIns = content.strip().split("\n\n");
        directions = Arrays.stream(separateDirFromIns[0].strip().split("")).toList();

        List<String> ins = Arrays.stream(separateDirFromIns[1].strip().split("\n")).toList();

        for (String line : ins) {
            String[] insProc = line.strip().split(" = ");
            String key = insProc[0];
            List<String> value =
                    Arrays.stream(insProc[1].replace("(", " ").replace(")", " ")
                            .strip().split(", ")).toList();
            instructions.put(key, value);
        }
    }
}
