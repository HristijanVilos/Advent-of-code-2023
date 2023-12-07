package day07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;

public class CamelCards {

    private static final Map<String, Integer> handsBids = new HashMap<>();

    public static void main(String[] args) {
        try {
            getFile("inputs/day07/input.txt");
            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long part1() {
        List<Pair> rankHand = new ArrayList<>();
        for (String hand : handsBids.keySet()) {
            Map<Character, Integer> cards = getCountCards(hand);
            Integer typeOfHand = rankHand(cards);
            rankHand.add(new Pair(hand, typeOfHand));
        }
        rankHand.sort(Pair::sortPairs);
        return calculateWinnings(rankHand);
    }

    private static long part2() {
        List<Pair> rankHand = new ArrayList<>();
        for (String hand : handsBids.keySet()) {
            Map<Character, Integer> cards = getCountCardsWithJokers(hand);
            Integer typeOfHand = rankHand(cards);
            rankHand.add(new Pair(hand, typeOfHand));
        }
        rankHand.sort(Pair::sortPairs2);
        return calculateWinnings(rankHand);
    }

    private static long calculateWinnings(List<Pair> rankHand) {
        long result = 0;
        for (int i = 0; i < rankHand.size(); i++) {
            result += (long) handsBids.get(rankHand.get(i).hand) * (i + 1);
        }
        return result;
    }

    private static int rankHand(Map<Character, Integer> cards) {
        int combination = cards.values().size();
        // combination zero part2
        if (combination == 1 || combination == 0) {
            // Five of a kind
            return 7;
        } else if (combination == 2) {
            for (int value : cards.values()) {
                if (value == 4) {
                    // Four of a kind
                    return 6;
                }
            }
            // Full house
            return 5;
        } else if (combination == 3) {
            for (int value : cards.values()) {
                if (value == 3) {
                    // Three of a kind
                    return 4;
                }
            }
            // Two pair
            return 3;
        } else if (combination == 4) {
            // One pair
            return 2;
        }
        return 1;
    }

    private static Map<Character, Integer> getCountCards(String hand) {
        Map<Character, Integer> cards = new HashMap<>();

        for (char card : hand.toCharArray()) {
            cards.merge(card, 1, Integer::sum);
        }
        return cards;
    }

    private static Map<Character, Integer> getCountCardsWithJokers(String hand) {
        Map<Character, Integer> cards = new HashMap<>();
        List<Pair> sortedCards = new ArrayList<>();

        for (char card : hand.toCharArray()) {
            cards.merge(card, 1, Integer::sum);
        }
        for (Map.Entry<Character, Integer> card : cards.entrySet()) {
            sortedCards.add(new Pair(String.valueOf(card.getKey()), card.getValue()));
        }
        sortedCards.sort((par1, par2) -> par2.rank - par1.rank);
        Pair jokeR = null;
        for (int i = 0; i < sortedCards.size(); i++) {
            if (sortedCards.get(i).hand.equals("J")) {
                if (i == 0 && sortedCards.size() > 1) {
                    sortedCards.get(1).rank += sortedCards.get(i).rank;
                    jokeR = sortedCards.get(i);
                } else if (sortedCards.size() > 1) {
                    sortedCards.get(0).rank += sortedCards.get(i).rank;
                    jokeR = sortedCards.get(i);
                }
                break;
            }
        }
        if (jokeR != null) {
            sortedCards.remove(jokeR);
        }
        Map<Character, Integer> cardsWithJokers = new HashMap<>();
        for (Pair card : sortedCards) {
            cardsWithJokers.put(card.hand.charAt(0), card.rank);
        }
        return cardsWithJokers;
    }


    private static void getFile(String file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] handBid = line.strip().split("\\s+");
                String hand = handBid[0];
                Integer bid = Integer.parseInt(handBid[1]);
                handsBids.put(hand, bid);
            }
        }
    }
}

class Pair {
    private static final Map<Character, Integer> valueOfCard = Map.ofEntries(
            entry('A', 13),
            entry('K', 12),
            entry('Q', 11),
            entry('J', 10),
            entry('T', 9),
            entry('9', 8),
            entry('8', 7),
            entry('7', 6),
            entry('6', 5),
            entry('5', 4),
            entry('4', 3),
            entry('3', 2),
            entry('2', 1)
    );

    private static final Map<Character, Integer> valueOfCard2 = Map.ofEntries(
            entry('A', 13),
            entry('K', 12),
            entry('Q', 11),
            entry('T', 9),
            entry('9', 8),
            entry('8', 7),
            entry('7', 6),
            entry('6', 5),
            entry('5', 4),
            entry('4', 3),
            entry('3', 2),
            entry('2', 1),
            entry('J', 0)
    );

    public String hand;
    public Integer rank;

    public Pair(String hand, Integer rank) {
        this.hand = hand;
        this.rank = rank;
    }

    public int sortPairs(Pair other) {
        if (Objects.equals(other.rank, this.rank)) {
            for (int i = 0; i < 5; i++) {
                char otherCard = other.hand.charAt(i);
                char thisCard = this.hand.charAt(i);
                if (Objects.equals(valueOfCard.get(otherCard), valueOfCard.get(thisCard))) {
                    continue;
                }
                return valueOfCard.get(thisCard) - valueOfCard.get(otherCard);
            }
        }
        return this.rank - other.rank;
    }

    public int sortPairs2(Pair other) {
        if (Objects.equals(other.rank, this.rank)) {
            for (int i = 0; i < 5; i++) {
                char otherCard = other.hand.charAt(i);
                char thisCard = this.hand.charAt(i);
                if (Objects.equals(valueOfCard2.get(otherCard), valueOfCard2.get(thisCard))) {
                    continue;
                }
                return valueOfCard2.get(thisCard) - valueOfCard2.get(otherCard);
            }
        }
        return this.rank - other.rank;
    }

    @Override
    public String toString() {
        return this.hand + ": " + this.rank;
    }
}
