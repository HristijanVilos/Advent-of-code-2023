import * as fs from "fs";

const f: string = fs.readFileSync("../inputs/day08/input.txt", { encoding: "utf8" });
const fullInstructuins: string[] = f.split("\n\n");
const direction: string[] = fullInstructuins[0].trim().split("");
const network: string[][] = fullInstructuins[1].split("\n").map(x => x.trim().split("=").map(y => y.trim()));
const instructins: Map<string, string[]> = new Map(network.map(x => [x[0], x[1].replace("(", " ").replace(")", " ").trim().split(", ")]))

function part1(): number {
    let result: number = 0;
    let going: string = "AAA";
    while (going !== "ZZZ") {
        going = followPath(going, result);
        result++;
    }
    return result;
}

function part2(): number {
    const postions: string[] = [];
    for (let key of instructins.keys()) {
        if (key.endsWith("A")) {
            postions.push(key);
        }
    }
    const seen: Map<string, number> = new Map();
    return findCycle(seen, postions);
}

function followPath(going: string, result: number) {
    const ins: string[] = instructins.get(going) || [];
    const dir: string = direction[result % direction.length];
    going = getDirection(dir, going, ins);
    return going;
}

function findCycle(seen: Map<string, number>, postions: string[]) {
    const cycles: number[] = [];
    for (let position of postions) {
        let result: number = 0;
        while (seen.get(position) === undefined || seen.get(position) != result % direction.length) {
            const ins: string[] = instructins.get(position) || [];
            if (direction[result % direction.length] === "L") {
                position = ins[0];
            } else {
                position = ins[1];
            }
            result += 1;
            if (position.endsWith("Z")) {
                seen.set(position, result % direction.length);
            }
        }
        cycles.push(result);
    }
    let finallResult: number = 1;
    for (const res of cycles) {
        finallResult = lcm(res, finallResult);
    }

    return finallResult;
}

function getDirection(dir: string, going: string, ins: string[]) {
    if (dir === "L") {
        going = ins[0];
    } else {
        going = ins[1];
    }
    return going;
}

function gcd(a: number, b: number): number {
    while (b > 0) {
        const temp: number = b;
        b = a % b;
        a = temp;
    }
    return a;
}

function lcm(a: number, b: number): number {
    return a * (b / gcd(a, b));
}


console.log("Part 1:", part1());
console.log("Part 2:", part2());
