package day04;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Scratchcards {
    public static void main(String[] args) {
        try {
            List<String> cards = getFile("inputs/day04/input.txt");
            System.out.println("Part 1: " + part1(cards));
            System.out.println("Part 2: " + part2(cards));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int part1(List<String> cards) {
        int result = 0;
        for (String card : cards) {
            int numberOfWinningNumbers = getNumberOfWinningNumbers(card);
            if (numberOfWinningNumbers != 0) {
                if (numberOfWinningNumbers == 1) {
                    result += 1;
                } else {
                    int pointsSystem = numberOfWinningNumbers - 1;
                    result += (int) Math.pow(2, pointsSystem);
                }
            }
        }
        return result;
    }

    private static int getNumberOfWinningNumbers(String card) {
        String parsedCard = card.strip().split(": ")[1];
        String[] winningAndMyNumbers = parsedCard.strip().split("\\|");
        List<Integer> winningNumbers = Arrays.stream(winningAndMyNumbers[0].strip().split("\\s+")).map(Integer::valueOf).toList();
        List<Integer> myNumbers = Arrays.stream(winningAndMyNumbers[1].strip().split("\\s+")).map(Integer::valueOf).toList();
        return winningNumbers.stream().filter(myNumbers::contains).toList().size();
    }

    private static int part2(List<String> cards) {
        Map<Integer, Integer> numOfCards = new HashMap<>();
        int numberOfCards = cards.size();
        for (String card : cards) {
            String[] parsedCardAndNumbers = card.strip().split(": ");
            Integer cardNum = Integer.parseInt(parsedCardAndNumbers[0].strip().split("\\s+")[1]);
            int numberOfWinningNumbers = getNumberOfWinningNumbers(card);
            numOfCards.merge(cardNum, 1, Integer::sum);
            int loopForNumOfCards = numOfCards.get(cardNum);
            for (int i = 1; i <= numberOfWinningNumbers; i++) {
                cardNum += 1;
                if (cardNum > numberOfCards) {
                    break;
                }
                numOfCards.merge(cardNum, loopForNumOfCards, Integer::sum);
            }
        }
        return sumOfCards(numOfCards);
    }

    private static int sumOfCards(Map<Integer, Integer> numOfCards) {
        int result = 0;
        for (Integer value: numOfCards.values()) {
            result += value;
        }
        return result;
    }

    private static List<String> getFile(String file) throws IOException {
        Path currentWorkingDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        Path absoluteFilePath = currentWorkingDir.resolve(file);
        try (Stream<String> input = Files.lines(absoluteFilePath)) {
            return input.toList();
        }
    }
}
