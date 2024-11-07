import * as fs from 'fs';

const f: string = fs.readFileSync("../inputs/day04/input.txt", { encoding: "utf-8" });
const cards: string[] = f.trim().split("\n").map(x => x.split(": ")[1])


function part1(cards: string[]): number {
    let result: number = 0;
    for (const card of cards) {
        const winningFound: number = numberOfWinningNumbers(card);

        let cardValue: number = 0;
        if (winningFound > 0) {
            cardValue = 2 ** (winningFound - 1);
        }
        result += cardValue;
    }

    return result;
}

function part2(cards: string[]): number {
    let idx: number = 0;
    const numOfCards: number[] = new Array(cards.length).fill(1);
    for (const card of cards) {
        const winningFound: number = numberOfWinningNumbers(card);
        const numCard: number = numOfCards[idx];
        idx++;

        for (let i = 0; i < winningFound; i++) {
            numOfCards[idx + i] += numCard;
        }
    }

    return numOfCards.reduce((a, b) => a + b);
}

function numberOfWinningNumbers(card: string): number {
    const splitCard: string[] = card.split("|");
    const myNumbers: number[] = splitCard[0].trim().split(/\s+/).map(x => Number(x));
    const winningNumbers: number[] = splitCard[1].trim().split(/\s+/).map(x => Number(x));

    return myNumbers.filter(num => winningNumbers.includes(num)).length;
}

console.log("Part 1:", part1(cards));
console.log("Part 2:", part2(cards));


