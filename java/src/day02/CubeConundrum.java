package day02;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CubeConundrum {
    public static void main(String[] args) {
        try {
            List<String> games = getFile();
            System.out.println("Part 1: " + part1(games));
            System.out.println("Part 2: " + part2(games));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int part1(List<String> games) {
        int result = 0;
        for (String game : games) {
            String[] gameNumAndBalls = game.strip().split(": ");
            int gameNumber = Integer.parseInt(gameNumAndBalls[0].strip().split(" ")[1]);
            String[] setsOfGameCubes = gameNumAndBalls[1].strip().split(";");
            boolean possibleGame = true;
            for (String gameSet : setsOfGameCubes) {
                String[] cubesInASet = gameSet.strip().split(",");
                for (String cube : cubesInASet) {
                    String[] cubeAndNum = cube.strip().split(" ");
                    Integer numberOfCubes = Integer.parseInt(cubeAndNum[0]);
                    String typeOfCube = cubeAndNum[1].strip();
                    if (!isGamePossible(numberOfCubes, typeOfCube)) {
                        possibleGame = false;
                        break;
                    }
                }
            }
            if (possibleGame) {
                result += gameNumber;
            }
        }
        return result;
    }

    private static int part2(List<String> games) {
        List<Integer> result = new ArrayList<>();
        for (String game : games) {
            String[] gameNumAndBalls = game.strip().split(": ");
            String[] gameCubes = gameNumAndBalls[1].strip().split("[,;]+");
            Map<String, Integer> gameCubesCount = new HashMap<>();
            for (String gameCube : gameCubes) {
                String[] gameCubesOnPull = gameCube.strip().split(" ");
                Integer numOfGameCubes = Integer.parseInt(gameCubesOnPull[0]);
                String typeOfCube = gameCubesOnPull[1].toLowerCase();
                if (gameCubesCount.get(typeOfCube) == null || gameCubesCount.get(typeOfCube) < numOfGameCubes) {
                    gameCubesCount.put(typeOfCube, numOfGameCubes);
                }
            }
            result.add(powerOfSetOfCubes(gameCubesCount));
        }
        return result.stream().mapToInt(Integer::intValue).sum();
    }

    private static boolean isGamePossible(Integer numberOfCubes, String typeOfCube) {
        return ((typeOfCube.equals("red") && numberOfCubes <= 12)
                || (typeOfCube.equals("green") && numberOfCubes <= 13)
                || (typeOfCube.equals("blue") && numberOfCubes <= 14));
    }

    private static int powerOfSetOfCubes(Map<String, Integer> gameCubesCount) {
        int result = 0;
        for (Map.Entry<String, Integer> entry : gameCubesCount.entrySet()) {
            Integer value = entry.getValue();
            if (result == 0) {
                result += value;
            } else {
                result *= value;
            }
        }
        return result;
    }

    private static List<String> getFile() throws IOException {
        Path currentWorkingDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        String relativeFilePath = "inputs/day02/input.txt";
        Path absoluteFilePath = currentWorkingDir.resolve(relativeFilePath);
        try (Stream<String> input = Files.lines(absoluteFilePath)) {
            return input.toList();
        }
    }
}
