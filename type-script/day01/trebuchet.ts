import * as fs from 'fs';
import { isCharNumeric } from '../utils';

const f: string = fs.readFileSync("../inputs/day01/input.txt", { encoding: "utf-8" });
const rows: string[] = f.trim().split("\n");

function part1(rows: string[]): number {
    let result: number = 0;
    rows.forEach(row => {
        result += fromStringToNumber(row);
    })
    return result;
}

function part2(rows: string[]): number {
    let result: number = 0;
    rows.forEach(row => {
        // first and last letter so that if they overlap
        row = row.replaceAll("one", "o1e");
        row = row.replaceAll("two", "t2o");
        row = row.replaceAll("three", "t3e");
        row = row.replaceAll("four", "f4r");
        row = row.replaceAll("five", "f5e");
        row = row.replaceAll("six", "s6x");
        row = row.replaceAll("seven", "s7n");
        row = row.replaceAll("eight", "e8t");
        row = row.replaceAll("nine", "n9e");
        result += fromStringToNumber(row);
    })
    return result;
}

function fromStringToNumber(row: string): number {
    let firstChar: string = "";
    let lastChar: string = "";
    for (let c of row) {
        if (isCharNumeric(c)) {
            if (firstChar === "") {
                firstChar = c;
            }
            lastChar = c;
        }
    }
    return Number(firstChar + lastChar);
}

console.log("Part 1:", part1(rows));
console.log("Part 1:", part2(rows));
