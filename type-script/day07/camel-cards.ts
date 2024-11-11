import * as fs from "fs";

const f: string = fs.readFileSync("../inputs/day07/input.txt", { encoding: "utf-8" });
const valueOfCardsPart1: Map<string, number> = new Map([
    ["A", 14],
    ["K", 13],
    ["Q", 12],
    ["J", 11],
    ["T", 10],
    ["9", 9],
    ["8", 8],
    ["7", 7],
    ["6", 6],
    ["5", 5],
    ["4", 4],
    ["3", 3],
    ["2", 2],
]);

const valueOfCardsPart2: Map<string, number> = new Map([
    ["A", 14],
    ["K", 13],
    ["Q", 12],
    ["T", 10],
    ["9", 9],
    ["8", 8],
    ["7", 7],
    ["6", 6],
    ["5", 5],
    ["4", 4],
    ["3", 3],
    ["2", 2],
    ["J", 1]
]);


function part1(file: string): number {
    const handsBids: Map<string, number> = getHandsFromFile(file);
    const hands: HandBid[] = [];
    for (let [k, v] of handsBids.entries()) {
        hands.push(new HandBid(k, v, valueOfCardsPart1));
    }
    hands.sort((a, b) => a.sortBy(b));
    const values: number[] = hands.map((hand, idx) => {
        return (idx + 1) * hand.bid;
    });

    return values.reduce((a, b) => a + b);
}

function part2(file: string): number {
    const handsBids: Map<string, number> = getHandsFromFile(file);
    const hands: HandBid[] = [];
    for (let [k, v] of handsBids.entries()) {
        hands.push(new HandBid(k, v, valueOfCardsPart2, true));
    }
    hands.sort((a, b) => a.sortBy(b));
    const values: number[] = hands.map((hand, idx) => {
        return (idx + 1) * hand.bid;
    });

    return values.reduce((a, b) => a + b);
}

function getHandsFromFile(file: string): Map<string, number> {
    const handsBids: Map<string, number> = new Map();
    const hands: string[][] = file.split("\n").map(x => x.trim().split(/\s+/));
    for (const hand of hands) {
        handsBids.set(hand[0], Number(hand[1]));
    }
    return handsBids;
}

class HandBid {
    private hand: string;
    bid: number;
    private valueOfCards: Map<string, number>;
    private countCards: Map<string, number>;
    private type: number;
    private jokers: boolean;

    constructor(k: string, v: number, valueOfCards: Map<string, number>, jokers: boolean = false) {
        this.hand = k;
        this.bid = v;
        this.jokers = jokers;
        this.valueOfCards = valueOfCards;
        this.countCards = this.getCountCards();
        this.type = this.typeOfHand();
    }

    private getCountCards(): Map<string, number> {
        const card: Map<string, number> = new Map();
        for (const char of this.hand) {
            if (card.get(char)) {
                card.set(char, (card.get(char) || 1) + 1);
                continue;
            }
            card.set(char, 1);
        }
        return card;
    }

    private typeOfHand(): number {
        let count: number = this.countCards.size;
        if (this.jokers) {
            this.correctValueForJokers(count);
        }

        // count === 0 for part2
        if (count === 1 || count === 0) {
            // Five of a kind
            return 6;
        } else if (count === 2) {
            for (let cnt of this.countCards.values()) {
                if (cnt === 4) {
                    // Four of a kind
                    return 5;
                }
            }
            // Full house
            return 4;
        } else if (count === 3) {
            for (let cnt of this.countCards.values()) {
                if (cnt === 3) {
                    // Three of a kind
                    return 3;
                }
            }
            // Two pair
            return 2
        } else if (count === 4) {
            // One pair
            return 1;
        }
        return 0;
    }

    private correctValueForJokers(count: number) {
        const valueOfJoker: number = this.countCards.get("J") || 0;
        if (valueOfJoker > 0) {
            this.countCards.delete("J");
            if (this.countCards.size > 0) {
                const [key, value]: [string, number] = [...this.countCards.entries()].reduce((a, e) => e[1] > a[1] ? e : a);
                this.countCards.set(key, value + valueOfJoker);
                count--;
            }
        }
        return count;
    }

    sortBy(other: HandBid): number {
        if (this.type !== other.type) {
            return this.type - other.type;
        }

        for (let i = 0; i < this.hand.length; i++) {
            if (this.hand[i] !== other.hand[i]) {
                return (this.valueOfCards.get(this.hand[i]) || 0)
                    - (this.valueOfCards.get(other.hand[i]) || 0);
            }
        }

        return 0;
    }
}

console.log("Part 1:", part1(f));
console.log("Part 2:", part2(f));
