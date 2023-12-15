package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LensLibary {
    private static Map<Integer, List<LensSlot>> boxes = new HashMap<>();

    public static void main(String[] args) {
        try {
            List<String> sequence = getFile("inputs/day15/input.txt");
            System.out.println("Part 1: " + part1(sequence));
            System.out.println("Part 2: " + part2(sequence));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part2(List<String> sequence) {
        for (String se : sequence) {
            if (se.endsWith("-")) {
                dashSign(se);
            } else {
                equalSign(se);
            }
        }
        return focusingPower();
    }

    private static long part1(List<String> sequence) {
        long result = 0;
        for (String se : sequence) {
            int interRes = hashAlgoritham(se);
            result += interRes;
        }
        return result;
    }

    private static int hashAlgoritham(String se) {
        int interRes = 0;
        for (Character ch : se.toCharArray()) {
            int ascii = ch;
            interRes += ascii;
            interRes *= 17;
            interRes %= 256;
        }
        return interRes;
    }

    private static void equalSign(String se) {
        String[] lensAndNum = se.trim().split("=");
        String lens = lensAndNum[0];
        int focalLen = Integer.parseInt(lensAndNum[1]);
        int puttingInBox = hashAlgoritham(lens);
        List<LensSlot> box = boxes.get(puttingInBox);
        LensSlot lensSlot = new LensSlot(lens);
        if (box != null) {
            boolean found = false;
            for (LensSlot b : box) {
                if (b.equals(lensSlot)) {
                    b.setFocalLen(focalLen);
                    found = true;
                    break;
                }
            }
            if (!found) {
                lensSlot.setFocalLen(focalLen);
                box.add(lensSlot);
            }
        } else {
            box = new ArrayList<>();
            lensSlot.setFocalLen(focalLen);
            box.add(lensSlot);
            boxes.put(puttingInBox, box);
        }
    }

    private static void dashSign(String se) {
        String lens = se.trim().split("-")[0];
        List<LensSlot> box = boxes.get(hashAlgoritham(lens));
        if (box != null) {
            LensSlot lensslot = new LensSlot(lens);
            box.remove(lensslot);
        }
    }

    private static long focusingPower() {
        long result = 0;
        for (Map.Entry<Integer, List<LensSlot>> entry : boxes.entrySet()) {
            Integer boxNum = entry.getKey() + 1;
            List<LensSlot> lenses = entry.getValue();
            for (int i = 0; i < lenses.size(); i++) {
                result += ((long) boxNum * (i + 1) * lenses.get(i).getFocalLen());
            }
        }
        return result;
    }

    private static List<String> getFile(String file) throws IOException {
        return Arrays.stream(Files.readString(Path.of(file)).trim().split(",")).toList();
    }
}

class LensSlot {
    private String name;
    private int focalLen;

    public LensSlot(String name) {
        this.name = name;
    }

    public int getFocalLen() {
        return this.focalLen;
    }

    public void setFocalLen(int focalLen) {
        this.focalLen = focalLen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LensSlot lensSlot = (LensSlot) o;
        return Objects.equals(name, lensSlot.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return this.name + " " + this.focalLen;
    }
}