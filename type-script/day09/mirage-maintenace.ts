import * as fs from "fs";

const f: string = fs.readFileSync("../inputs/day09/input.txt", { encoding: "utf-8" });
const histories: number[][] = f.split("\n").map(x => x.split(/\s+/).map(y => Number(y)));

function part1(): number {
    let result: number = 0;
    for (const history of histories) {
        const differences: number[][] = [];
        differences.push(history);

        constructHistory(history, differences);

        for (const dif of differences) {
            result += dif.at(-1) || 0;
        }
    }
    return result;
}

function part2(): number {
    let result: number = 0;
    for (const history of histories) {
        const differences: number[][] = [];
        differences.push(history);

        constructHistory(history, differences);

        let first: number = 0;
        let next: number = 0;
        for (let i = differences.length - 1; i >= 1; i--) {
            first = next;
            next = (differences[i - 1].at(0) || 0) - first;
        }
        result += next;
    }
    return result;
}

function constructHistory(firstStep: number[], differences: number[][]): void {
    let step: number[] = [...firstStep];
    while (!step.every(x => x == 0)) {
        let temp: number[] = [];
        for (let i = 0; i < step.length - 1; i++) {
            temp.push(step[i + 1] - step[i]);
        }
        step = [...temp];
        differences.push(step);
    }
}

console.log("Part 1:", part1());
console.log("Part 2:", part2());
