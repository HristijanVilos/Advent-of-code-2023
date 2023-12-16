package day16;

import utils.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TheFloorWillBeLava {
    private static final List<List<Character>> map = new ArrayList<>();
    private static Integer maxHeight;
    private static Integer maxWidth;

    public static void main(String[] args) {
        try {
            getFile("inputs/day16/input.txt");
            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1() {
        Node startingNode = new Node(0, -1);
        Pair startingPair = new Pair(startingNode, Pair.EAST);
        return solution(startingPair);
    }

    private static long part2() {
        List<Pair> startingPos = new ArrayList<>();
        for (int i = 0; i < maxHeight; i++) {
            Node goingEast = new Node(i, -1);
            Pair goingEastPair = new Pair(goingEast, Pair.EAST);
            startingPos.add(goingEastPair);
        }
        for (int i = 0; i < maxHeight; i++) {
            Node goingWest = new Node(i, maxWidth);
            Pair goingWestPair = new Pair(goingWest, Pair.WEST);
            startingPos.add(goingWestPair);
        }
        for (int i = 0; i < maxWidth; i++) {
            Node gointNorth = new Node(maxHeight, i);
            Pair gointNorthPair = new Pair(gointNorth, Pair.NORTH);
            startingPos.add(gointNorthPair);
        }
        for (int i = 0; i < maxWidth; i++) {
            Node goingSouth = new Node(-1, i);
            Pair goingSouthPair = new Pair(goingSouth, Pair.SOUTH);
            startingPos.add(goingSouthPair);
        }
        long result = 0;
        for (Pair pair : startingPos) {
            result = Math.max(result, solution(pair));
        }
        return result;
    }

    private static int solution(Pair startingPair) {
        Set<Node> nodes = new HashSet<>();
        Queue<Pair> queue = new LinkedList<>();
        queue.add(startingPair);
        Set<Pair> seen = new HashSet<>();
        while (!queue.isEmpty()) {
            Pair pair = queue.poll();
            Node node = pair.getNode();
            String going = pair.getGoing();

            if (seen.contains(pair)) {
                continue;
            }

            seen.add(pair);
            nodes.add(pair.getNode());

            int[] dir = Pair.getDirection(going);
            int x = dir[0] + node.getX();
            int y = dir[1] + node.getY();
            if (x >= 0 && x < maxHeight && y >= 0 && y < maxWidth) {
                addNodesToQueue(x, y, going, queue);
            }
        }
        return nodes.size() - 1;
    }

    private static void addNodesToQueue(int x, int y, String going, Queue<Pair> queue) {
        Character atChar = map.get(x).get(y);
        String direction = null;
        String secondDirection = null;
        if (atChar == '/') {
            direction = upwardMirror(going);
        } else if (atChar == '\\') {
            direction = downwardMirror(going);
        } else if (atChar == '|') {
            if (going.equals(Pair.EAST)) {
                direction = Pair.SOUTH;
                secondDirection = Pair.NORTH;
            } else if (going.equals(Pair.NORTH)) {
                direction = Pair.NORTH;
            } else if (going.equals(Pair.WEST)) {
                direction = Pair.NORTH;
                secondDirection = Pair.SOUTH;
            } else if (going.equals(Pair.SOUTH)) {
                direction = Pair.SOUTH;
            }
        } else if (atChar == '-') {
            if (going.equals(Pair.EAST)) {
                direction = Pair.EAST;
            } else if (going.equals(Pair.NORTH) || going.equals(Pair.SOUTH)) {
                direction = Pair.EAST;
                secondDirection = Pair.WEST;
            } else if (going.equals(Pair.WEST)) {
                direction = Pair.WEST;
            }
        } else {
            direction = going;
        }
        Pair newPair = new Pair(new Node(x, y), direction);
        queue.add(newPair);
        if (secondDirection != null) {
            Pair secondPair = new Pair(new Node(x, y), secondDirection);
            queue.add(secondPair);
        }
    }

    private static String downwardMirror(String going) {
        // "\"
        String direction = null;
        if (going.equals(Pair.EAST)) {
            direction = Pair.SOUTH;
        } else if (going.equals(Pair.NORTH)) {
            direction = Pair.WEST;
        } else if (going.equals(Pair.WEST)) {
            direction = Pair.NORTH;
        } else if (going.equals(Pair.SOUTH)) {
            direction = Pair.EAST;
        }
        return direction;
    }

    private static String upwardMirror(String going) {
        // "/"
        String direction = null;
        if (going.equals(Pair.EAST)) {
            direction = Pair.NORTH;
        } else if (going.equals(Pair.NORTH)) {
            direction = Pair.EAST;
        } else if (going.equals(Pair.WEST)) {
            direction = Pair.SOUTH;
        } else if (going.equals(Pair.SOUTH)) {
            direction = Pair.WEST;
        }
        return direction;
    }

    private static void getFile(String file) throws IOException {
        List<String> input = Arrays.stream(Files.readString(Path.of(file)).trim().split("\n")).toList();
        for (String row : input) {
            List<Character> rowsChar = row.chars().mapToObj(e -> (char) e).toList();
            map.add(rowsChar);
            maxWidth = rowsChar.size();
        }
        maxHeight = map.size();
    }
}

class Pair {

    public static final String EAST = "E";
    public static final String NORTH = "N";
    public static final String WEST = "W";
    public static final String SOUTH = "S";

    public static int[] getDirection(String direction) {
        if (direction.equals(EAST)) return new int[]{0, 1};
        else if (direction.equals(NORTH)) return new int[]{-1, 0};
        else if (direction.equals(WEST)) return new int[]{0, -1};
        else return new int[]{1, 0};
    }

    private Node node;
    private String going;

    public Pair(Node node, String going) {
        this.node = node;
        this.going = going;
    }

    public Node getNode() {
        return this.node;
    }

    public String getGoing() {
        return this.going;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(node, pair.node) && Objects.equals(going, pair.going);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, going);
    }
}
