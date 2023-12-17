package day17;

import utils.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class ClumsyCrucible {
    private static final Map<String, List<Integer>> moves = Map.ofEntries(
            Map.entry(">", List.of(0, 1)),
            Map.entry("<", List.of(0, -1)),
            Map.entry("v", List.of(1, 0)),
            Map.entry("^", List.of(-1, 0))
    );

    private static final Map<String, String> opposite = Map.ofEntries(
            Map.entry(">", "<"),
            Map.entry("<", ">"),
            Map.entry("v", "^"),
            Map.entry("^", "v")
    );

    private static final String START = "start";
    private static List<List<Integer>> map = new ArrayList<>();
    private static Integer maxHeight;
    private static Integer maxWidth;

    public static void main(String[] args) {
        try {
            getFile("inputs/day17/input.txt");
            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int part2() {
        Node startingNode = new Node(0, 0);
        PriorityQueue<Tuple> queue = new PriorityQueue<>(new TupleComparator());
        Tuple startTuple = new Tuple(startingNode, 0, START, START, 0);
        queue.add(startTuple);
        Set<Tuple> seen = new HashSet<>();
        while (!queue.isEmpty()) {
            Tuple tuple = queue.poll();
            Node node = tuple.getNode();
            String direction = tuple.getDirection();
            Integer distance = tuple.getDistance();
            int movement = tuple.getMovement();

            if (isExitPoint(node)) {
                return distance;
            }

            if (seen.contains(tuple)) {
                continue;
            }

            seen.add(tuple);

            for (Map.Entry<String, List<Integer>> entry : moves.entrySet()) {
                String goingDirection = entry.getKey();
                List<Integer> move = entry.getValue();
                int newMovement = movement;
                int x = node.getX() + move.get(0);
                int y = node.getY() + move.get(1);
                addNextNodePart2(x, y, direction, goingDirection, newMovement, distance, tuple, queue);
            }
        }
        return -1;
    }

    private static void addNextNodePart2(int x, int y, String direction, String goingDirection, int newMovement, Integer distance, Tuple tuple, PriorityQueue<Tuple> queue) {
        if (isOutsideOfGrid(x, y) || isTurningInOppositeDirection(direction, goingDirection)) {
            return;
        }
        if (!START.equals(direction) && (!direction.equals(goingDirection) && newMovement < 4)) {
            return;
        }
        newMovement = setNewmovement(direction, goingDirection, newMovement);
        if (newMovement > 10) {
            return;
        }
        Node newNode = new Node(x, y);
        Tuple newTuple = new Tuple(newNode, distance + map.get(x).get(y), direction, goingDirection, newMovement);
        newTuple.setComingFrom(tuple);
        queue.add(newTuple);
    }

    private static int part1() {
        Node startingNode = new Node(0, 0);
        PriorityQueue<Tuple> queue = new PriorityQueue<>(new TupleComparator());
        Tuple startTuple = new Tuple(startingNode, 0, START, START, 0);
        queue.add(startTuple);
        Set<Tuple> seen = new HashSet<>();
        while (!queue.isEmpty()) {
            Tuple tuple = queue.poll();
            Node node = tuple.getNode();
            String direction = tuple.getDirection();
            Integer distance = tuple.getDistance();
            int movement = tuple.getMovement();

            if (isExitPoint(node)) {
                return distance;
            }

            if (seen.contains(tuple)) {
                continue;
            }

            seen.add(tuple);

            for (Map.Entry<String, List<Integer>> entry : moves.entrySet()) {
                String goingDirection = entry.getKey();
                List<Integer> move = entry.getValue();
                int newMovement = movement;
                int x = node.getX() + move.get(0);
                int y = node.getY() + move.get(1);
                addNewNodePart1(x, y, direction, goingDirection, newMovement, distance, tuple, queue);
            }
        }
        return -1;
    }

    private static void addNewNodePart1(int x, int y, String direction, String goingDirection, int newMovement, Integer distance, Tuple tuple, PriorityQueue<Tuple> queue) {
        if (isOutsideOfGrid(x, y) || isTurningInOppositeDirection(direction, goingDirection)) {
            return;
        }
        newMovement = setNewmovement(direction, goingDirection, newMovement);
        if (newMovement > 3) {
            return;
        }
        Node newNode = new Node(x, y);
        Tuple newTuple = new Tuple(newNode, distance + map.get(x).get(y), direction, goingDirection, newMovement);
        newTuple.setComingFrom(tuple);
        queue.add(newTuple);
    }

    private static int setNewmovement(String direction, String goingDirection, int newMovement) {
        if (direction.equals(goingDirection)) {
            newMovement++;
        } else {
            newMovement = 1;
        }
        return newMovement;
    }

    private static boolean isTurningInOppositeDirection(String direction, String goingDirection) {
        return opposite.get(direction) != null && opposite.get(direction).equals(goingDirection);
    }

    private static boolean isOutsideOfGrid(int x, int y) {
        return x < 0 || x >= maxHeight || y < 0 || y >= maxWidth;
    }

    private static boolean isExitPoint(Node node) {
        return node.getX() == maxHeight - 1 && node.getY() == maxWidth - 1;
    }

    private static void getFile(String file) throws IOException {
        List<String> input = Files.readAllLines(Path.of(file));
        for (String row : input) {
            List<Integer> rowInt = Arrays.stream(row.trim().split("")).map(Integer::valueOf).toList();
            map.add(rowInt);
            maxWidth = row.length();
        }
        maxHeight = map.size();
    }
}

class TupleComparator implements Comparator<Tuple> {
    public int compare(Tuple tup1, Tuple tup2) {
        return tup1.getDistance() - tup2.getDistance();
    }
}

class Tuple {
    private Node node;
    private Integer distance;
    private String comingDir;
    private String direction;
    private Integer movement;
    private Tuple comingFrom;

    public Tuple(Node node, Integer distance, String comingDir, String direction, Integer movement) {
        this.node = node;
        this.distance = distance;
        this.comingDir = comingDir;
        this.direction = direction;
        this.movement = movement;
    }

    public Node getNode() {
        return node;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getMovement(){
        return this.movement;
    }


    public Tuple getComingFrom() {
        return comingFrom;
    }

    public void setComingFrom(Tuple comingFrom) {
        this.comingFrom = comingFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Objects.equals(node, tuple.node) && Objects.equals(comingDir, tuple.comingDir) && Objects.equals(direction, tuple.direction) && Objects.equals(movement, tuple.movement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, comingDir, direction, movement);
    }

    @Override
    public String toString() {
        return this.node + ": " + this.distance + " " + this.direction + " " + this.movement;
    }
}