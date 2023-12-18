package day18;

import utils.Node;
import utils.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LavaductLagoon {
    private static List<Pair<String, Integer>> diggingInsPart1 = new ArrayList<>();
    private static List<String> diggingInsPart2 = new ArrayList<>();

    private static Map<String, List<Integer>> dig = Map.ofEntries(
            Map.entry("R", List.of(0, 1)),
            Map.entry("L", List.of(0, -1)),
            Map.entry("D", List.of(1, 0)),
            Map.entry("U", List.of(-1, 0))
    );

    private static Map<String, String> decode = Map.ofEntries(
            Map.entry("0", "R"),
            Map.entry("1", "D"),
            Map.entry("2", "L"),
            Map.entry("3", "U")
    );

    public static void main(String[] args) {
        try {
            getFile("inputs/day18/input.txt");
            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part2() {
        List<Pair<String, Integer>> digIns = new ArrayList<>();
        for (String ins : diggingInsPart2) {
            String diggDir = decode.get(ins.substring(ins.length() - 2, ins.length() - 1));
            Integer hexString = Integer.decode(ins.substring(1, ins.length() - 2));
            digIns.add(new Pair<>(diggDir, hexString));
        }
        return solution(digIns);
    }

    private static long part1() {
        return solution(diggingInsPart1);
    }

    private static long solution(List<Pair<String, Integer>> diggingInstructions) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0, 0));
        Long boundryPoints = 0L;
        for (Pair<String, Integer> pair : diggingInstructions) {
            Node node = nodes.getLast();
            boundryPoints += pair.getValue();
            List<Integer> move = dig.get(pair.getKey());
            int x = node.getX() + move.get(0) * pair.getValue();
            int y = node.getY() + move.get(1) * pair.getValue();
            nodes.add(new Node(x, y));
        }

        long result = 0;

        // Shoelace formula
        for (int i = 0; i < nodes.size() - 1; i++) {
            Node node1 = nodes.get(i);
            Node node2 = nodes.get(i + 1);

            result += (((long) node1.getX() * node2.getY()) - ((long) node1.getY() * node2.getX()));
        }
        // Pick's theorem
        Long i = Math.abs(result / 2) - boundryPoints / 2 + 1;
        return i + boundryPoints;
    }

    private static void getFile(String file) throws IOException {
        List<String> input = Files.readAllLines(Path.of(file));
        for (String row : input) {
            String[] rowSplit = row.strip().split(" ");
            diggingInsPart1.add(new Pair<>(rowSplit[0], Integer.valueOf(rowSplit[1])));
            diggingInsPart2.add(rowSplit[2]);
        }
    }
}
