package day11;

import utils.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CosmicExpansion {
    private static Set<Node> setOfGalaxies = new HashSet<>();

    public static void main(String[] args) {
        try {
            System.out.println("Part 1: " + solution(2));
            System.out.println("Part 2: " + solution(1_000_000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long solution(int emptyRowMult) throws IOException {
        getFile("inputs/day11/input.txt", emptyRowMult);
        long result = 0;
        List<Node> allNodes = setOfGalaxies.stream().toList();
        for (int i = 0; i < allNodes.size(); i++) {
            Node startNode = allNodes.get(i);
            for (int j = i + 1; j < allNodes.size(); j++) {
                Node otherNode = allNodes.get(j);
                result += Math.abs(otherNode.getX() - startNode.getX()) + Math.abs(otherNode.getY() - startNode.getY());
            }
        }
        return result;
    }


    private static void getFile(String file, int emptyRowMult) throws IOException {
        setOfGalaxies = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int idx = 0;
            int widthOfMap = 0;
            while ((line = br.readLine()) != null) {
                boolean found = false;
                for (int j = 0; j < line.length(); j++) {
                    widthOfMap = line.length();
                    if (line.charAt(j) == '#') {
                        found = true;
                        setOfGalaxies.add(new Node(idx, j));
                    }
                }
                if (!found) {
                    idx += emptyRowMult;
                } else {
                    idx++;
                }
            }
            Set<Integer> emptyColums = findEmptyColumns(widthOfMap);

            idx = 0;
            for (int val : emptyColums) {
                val += idx;
                for (Node node : setOfGalaxies) {
                    int y = node.getY();
                    if (y > val) {
                        node.setY(y + emptyRowMult - 1);
                    }
                }
                idx += emptyRowMult - 1;
            }
        }
    }

    private static Set<Integer> findEmptyColumns(int widthOfMap) {
        Set<Integer> emptyColums = new HashSet<>();
        for (int j = 0; j < widthOfMap; j++) {
            emptyColums.add(j);
        }
        for (Node node : setOfGalaxies) {
            emptyColums.remove(node.getY());
        }
        return emptyColums;
    }
}
