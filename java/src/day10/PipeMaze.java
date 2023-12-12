package day10;

import utils.Node;
import utils.NodeAndDist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PipeMaze {
    private static final int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static Map<String, int[][]> tilesMoves = Map.ofEntries(
            Map.entry("|", new int[][]{{-1, 0}, {1, 0}}),
            Map.entry("-", new int[][]{{0, -1}, {0, 1}}),
            Map.entry("L", new int[][]{{-1, 0}, {0, 1}}),
            Map.entry("J", new int[][]{{-1, 0}, {0, -1}}),
            Map.entry("7", new int[][]{{1, 0}, {0, -1}}),
            Map.entry("F", new int[][]{{1, 0}, {0, 1}}),
            Map.entry("S", new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}),
            Map.entry("X", new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}})
    );
    private static Map<Node, String> mapOfPipes = new HashMap<>();
    private static Integer heightPfMap;
    private static Integer widthOfMap;
    private static Node startPos;

    public static void main(String[] args) {
        try {
            String file = "inputs/day10/input.txt";
            System.out.println("Part 1: " + part1(file));
            System.out.println("Part 2: " + part2(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1(String file) throws IOException {
        getFile(file);
        long result = 0;
        Queue<NodeAndDist> queue = new LinkedList<>();
        queue.add(new NodeAndDist(startPos, 0));
        Map<Node, Integer> seen = new HashMap<>();
        while (!queue.isEmpty()) {
            NodeAndDist nodeAndDist = queue.poll();
            Node node = nodeAndDist.getNode();
            int dist = nodeAndDist.getDist();

            if (seen.get(node) != null) {
                continue;
            }

            seen.put(node, dist);
            dist++;
            addNextNodes(node, dist, queue);
        }

        for (Integer value : seen.values()) {
            result = Math.max(result, value);
        }
        return result;
    }

    private static long part2(String file) throws IOException {
        getFileExpanded(file);
        Queue<NodeAndDist> queue = new LinkedList<>();
        queue.add(new NodeAndDist(startPos, 0));
        Map<Node, Integer> seen = new HashMap<>();
        while (!queue.isEmpty()) {
            NodeAndDist nodeAndDist = queue.poll();
            Node node = nodeAndDist.getNode();
            int dist = nodeAndDist.getDist();

            if (seen.get(node) != null) {
                continue;
            }

            seen.put(node, dist);
            dist++;
            addNextNodes(node, dist, queue);
        }
        Node pos = findEndNode(seen);

        Node otherSideOfLoop = findOtherNodeThatConnectsToEnd(pos, seen);

        Set<Node> mainLoop = constructMainLoop(pos, otherSideOfLoop);

        Set<Node> otherTiles = getOtherTiles(mainLoop);

        List<Set<Node>> connectedTiles = getConnectedTiles(otherTiles, mainLoop);

        Set<Node> tilesInside = new HashSet<>();

        for (Set<Node> nodes : connectedTiles) {
            boolean outside = areNodesOutside(nodes, mainLoop);
            if (!outside) {
                tilesInside.addAll(nodes);
            }
        }

        int result = 0;
        for (Node tile : tilesInside) {
            String typeOfNode = mapOfPipes.get(tile);
            if (typeOfNode != null && typeOfNode.equals("X")) {
                continue;
            }
            result += 1;
        }
        return result;
    }

    private static List<Set<Node>> getConnectedTiles(Set<Node> otherTiles, Set<Node> mainLoop) {
        List<Set<Node>> connectedTiles = new ArrayList<>();
        while (!otherTiles.isEmpty()) {
            Queue<Node> queue = new LinkedList<>();
            Node startingTile = otherTiles.stream().iterator().next();
            queue.add(startingTile);
            Set<Node> seen = new HashSet<>();
            while (!queue.isEmpty()) {
                Node node = queue.poll();

                if (seen.contains(node)) {
                    continue;
                }
                seen.add(node);
                for (int[] move : moves) {
                    Node newNode = new Node(node.getX() + move[0], node.getY() + move[1]);
                    if (!mainLoop.contains(newNode) && otherTiles.contains(newNode)) {
                        queue.add(newNode);
                    }
                }
            }
            otherTiles.removeAll(seen);
            connectedTiles.add(seen);
        }
        return connectedTiles;
    }

    private static boolean areNodesOutside(Set<Node> nodes, Set<Node> mainLoop) {
        for (Node node : nodes) {
            Queue<Node> queue = new LinkedList<>();
            queue.add(node);
            Set<Node> seen = new HashSet<>();
            while (!queue.isEmpty()) {
                Node node2 = queue.poll();

                if (seen.contains(node2)) {
                    continue;
                }
                seen.add(node2);

                for (int[] move : moves) {
                    Node moveTo = new Node(node2.getX() + move[0], node2.getY() + move[1]);
                    if (!mainLoop.contains(moveTo) && !nodes.contains(moveTo)) {
                        return true;
                    } else if (mainLoop.contains(moveTo)) {
                        continue;
                    }
                    queue.add(moveTo);
                }
            }
        }
        return false;
    }

    private static Set<Node> getOtherTiles(Set<Node> mainLoop) {
        Set<Node> otherTiles = new HashSet<>();
        for (int i = 0; i < heightPfMap; i++) {
            for (int j = 0; j < widthOfMap; j++) {
                Node node = new Node(i, j);
                if (!mainLoop.contains(node)) {
                    otherTiles.add(node);
                }
            }
        }
        return otherTiles;
    }

    private static Set<Node> constructMainLoop(Node pos, Node otherSideOfLoop) {
        Set<Node> mainLoop = new HashSet<>();
        while (pos.getCameFrom() != null) {
            mainLoop.add(pos);
            pos = pos.getCameFrom();
        }
        while (otherSideOfLoop.getCameFrom() != null) {
            mainLoop.add(otherSideOfLoop);
            otherSideOfLoop = otherSideOfLoop.getCameFrom();
        }
        mainLoop.add(startPos);
        return mainLoop;
    }

    private static Node findOtherNodeThatConnectsToEnd(Node pos, Map<Node, Integer> seen) {
        Node otherSideOfLoop = null;
        String endNode = mapOfPipes.get(pos);
        int[][] possibleMoves = tilesMoves.get(endNode);
        for (int[] move : possibleMoves) {
            Node posibleNode = new Node(pos.getX() + move[0], pos.getY() + move[1]);
            if (!posibleNode.equals(pos.getCameFrom())) {
                for (Node key : seen.keySet()) {
                    if (key.equals(posibleNode)) {
                        otherSideOfLoop = key;
                    }
                }
            }
        }
        return otherSideOfLoop;
    }

    private static Node findEndNode(Map<Node, Integer> seen) {
        long endVal = 0;
        Node pos = null;
        for (Map.Entry<Node, Integer> entry : seen.entrySet()) {
            Integer value = entry.getValue();
            long prevRes = endVal;
            endVal = Math.max(endVal, value);
            if (prevRes != endVal) {
                pos = entry.getKey();
            }
        }
        return pos;
    }


    private static void addNextNodes(Node node, int dist, Queue<NodeAndDist> queue) {
        String startPyp = mapOfPipes.get(node);
        int[][] canGoTo = tilesMoves.get(startPyp);
        for (int[] move : canGoTo) {
            Node newNode = new Node(node.getX() + move[0], node.getY() + move[1]);
            if (mapOfPipes.get(newNode) != null) {
                String pipeToGoTo = mapOfPipes.get(newNode);
                int[][] canComeFrom = tilesMoves.get(pipeToGoTo);
                for (int[] pipeNode : canComeFrom) {
                    if (node.getX() == newNode.getX() + pipeNode[0] && node.getY() == newNode.getY() + pipeNode[1]
                            && !areBothNodesX(node, newNode)) {
                        queue.add(new NodeAndDist(newNode, dist));
                        newNode.setCameFrom(node);
                    }
                }
            }
        }
    }

    private static boolean areBothNodesX(Node node, Node newNode) {
        String nodeType = mapOfPipes.get(node);
        String newNodeType = mapOfPipes.get(newNode);
        return nodeType.equals("X") && newNodeType.equals("X");
    }

    private static void getFile(String file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int idx = 0;
            while ((line = br.readLine()) != null) {
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '.') {
                        continue;
                    }
                    if (line.charAt(j) == 'S') {
                        startPos = new Node(idx, j);
                    }
                    mapOfPipes.put(new Node(idx, j), String.valueOf(line.charAt(j)));
                    widthOfMap = line.length();
                }
                idx++;
            }
            heightPfMap = idx;
        }
    }

    private static void getFileExpanded(String file) throws IOException {
        List<List<Character>> expandedMap = new ArrayList<>();
        mapOfPipes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<Character> row = new ArrayList<>();
                for (int j = 0; j < line.length(); j++) {
                    row.add(line.charAt(j));
                    row.add('X');
                }
                expandedMap.add(row);
                List<Character> xRow = Collections.nCopies(row.size(), 'X');
                expandedMap.add(xRow);
            }
        }
        for (int i = 0; i < expandedMap.size(); i++) {
            List<Character> row = expandedMap.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) == '.') {
                    continue;
                }
                if (row.get(j) == 'S') {
                    startPos = new Node(i, j);
                }
                mapOfPipes.put(new Node(i, j), String.valueOf(row.get(j)));
                widthOfMap = row.size();
            }
        }
        heightPfMap = expandedMap.size();
    }
}