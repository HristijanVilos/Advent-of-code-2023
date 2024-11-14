import * as fs from "fs";

const f: string = fs.readFileSync("../inputs/day15/input.txt", { encoding: "utf-8" });
const input: string[] = f.trim().split(",");

function part1(input: string[]): number {
    let result: number = 0;
    for (let str of input) {
        let currentValue: number = hashFunction(str);
        result += currentValue;
    }
    return result;
}

function part2(input: string[]): number {
    const boxesMap: Map<number, Array<Lens>> = new Map();
    for (const se of input) {
        if (se.endsWith("-")) {
            minusSign(se, boxesMap);
        } else {
            equalSign(se, boxesMap);
        }
    }
    return calculateResult(boxesMap);
}

function hashFunction(str: string) {
    let currentValue: number = 0;
    for (let c of str) {
        currentValue += c.charCodeAt(0);
        currentValue *= 17;
        currentValue %= 256;
    }
    return currentValue;
}

function equalSign(se: string, boxesMap: Map<number, Array<Lens>>): void {
    const split: string[] = se.trim().split("=");
    const lens: Lens = new Lens(split[0], Number(split[1]));
    const boxNumber: number = hashFunction(split[0]);
    if (boxesMap.has(boxNumber)) {
        const boxes: Lens[] = boxesMap.get(boxNumber) || []
        for (let idx in boxes) {
            if (boxes[idx].equal(lens)) {
                boxes[idx] = lens;
                return;
            }
        }
        boxes.push(lens);
    } else {
        boxesMap.set(boxNumber, [lens]);
    }
}

function minusSign(se: string, boxesMap: Map<number, Array<Lens>>): void {
    const split: string[] = se.trim().split("-");
    const lens: Lens = new Lens(split[0], 0);
    const boxNumber: number = hashFunction(split[0]);
    if (boxesMap.has(boxNumber)) {
        const boxes: Lens[] = boxesMap.get(boxNumber) || []
        for (let i = 0; i < boxes.length; i++) {
            if (boxes[i].equal(lens)) {
                boxes.splice(i, 1);
                return;
            }
        }
    }
}

function calculateResult(boxesMap: Map<number, Array<Lens>>): number {
    let result: number = 0;
    for (let box of boxesMap.keys()) {
        for (let [idx, lens] of (boxesMap.get(box) || []).entries()) {
            result += (box + 1) * (idx + 1) * lens.focalLength;
        }
    }
    return result;
}

class Lens {
    name: string;
    focalLength: number;

    constructor(name: string, n: number) {
        this.name = name;
        this.focalLength = n;
    }

    equal(o: Lens): boolean {
        return this.name === o.name;
    }
}

console.log("Part 1:", part1(input));
console.log("Part 2:", part2(input));
