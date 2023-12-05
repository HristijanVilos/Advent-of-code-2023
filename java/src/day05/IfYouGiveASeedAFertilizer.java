package day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class IfYouGiveAsourceAFertilizer {
    private static List<Long> sources = new ArrayList<>();

    private static final Map<String, List<List<Long>>> maps = new HashMap<>();

    public static void main(String[] args) {
        try {
            getFile("inputs/day05/input.txt");
            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1() {
        long minLocation = Long.MAX_VALUE;
        for (Long seed : sources) {
            minLocation = locationFromSeed(seed, minLocation);
        }
        return minLocation;
    }

    private static long locationFromSeed(Long seed, long minLocation) {
        Long i = seed;
        List<List<Long>> sourceToSoil = maps.get("seed-to-soil map");
        i = getDestFromSource(sourceToSoil, i);
        List<List<Long>> soilToFertilizer = maps.get("soil-to-fertilizer map");
        i = getDestFromSource(soilToFertilizer, i);
        List<List<Long>> fertilizerToWater = maps.get("fertilizer-to-water map");
        i = getDestFromSource(fertilizerToWater, i);
        List<List<Long>> waterToLight = maps.get("water-to-light map");
        i = getDestFromSource(waterToLight, i);
        List<List<Long>> lightToTemperature = maps.get("light-to-temperature map");
        i = getDestFromSource(lightToTemperature, i);
        List<List<Long>> temperatureToHumidity = maps.get("temperature-to-humidity map");
        i = getDestFromSource(temperatureToHumidity, i);
        List<List<Long>> humidityToLocation = maps.get("humidity-to-location map");
        i = getDestFromSource(humidityToLocation, i);
        return Math.min(minLocation, i);
    }

    private static long part2() {
        long minLocation = Long.MAX_VALUE;
        for (int z = 0; z < sources.size(); z += 2) {
            long start = sources.get(z);
            long finish = sources.get(z) + sources.get(z + 1);
            for (long j = start; j <= finish; j++) {
                minLocation = locationFromSeed(j, minLocation);
            }
        }
        return minLocation;
    }

    private static Long getDestFromSource(List<List<Long>> sources, Long i) {
        for (List<Long> s : sources) {
            Range range = new Range(s);
            if (range.isInRange(i)) {
                i = range.convertFinal(i);
                break;
            }
            i = range.convertFinal(i);
        }
        return i;
    }

    private static void getFile(String file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String key = "sources";
            List<Long> lineOfNum = new ArrayList<>();
            List<List<Long>> listOfNums = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    maps.put(key, listOfNums);
                    listOfNums = new ArrayList<>();
                    key = null;
                } else {
                    if (key != null) {
                        if (key.equals("sources")) {
                            sources = Arrays.stream(line.strip().split(": ")[1].split("\\s+")).map(Long::valueOf).toList();
                        } else {
                            lineOfNum = Arrays.stream(line.strip().split("\\s+")).map(Long::valueOf).toList();
                            listOfNums.add(lineOfNum);
                        }
                    } else {
                        key = line.strip().split(":")[0];
                    }
                }
            }
            maps.put(key, listOfNums);
        }
    }
}

class Range {
    private final Long rangeLength;
    private final Long sourceRangeStart;
    private final Long destinationRangeStart;

    public Range(List<Long> source) {
        this.destinationRangeStart = source.get(0);
        this.sourceRangeStart = source.get(1);
        this.rangeLength = source.get(2);
    }

    public boolean isInRange(Long source) {
        return this.sourceRangeStart <= source && source < this.sourceRangeStart + this.rangeLength;
    }

    public Long convertFinal(Long source) {
        if (!isInRange(source)) {
            return source;
        }
        return (this.destinationRangeStart - this.sourceRangeStart) + source;
    }
}

