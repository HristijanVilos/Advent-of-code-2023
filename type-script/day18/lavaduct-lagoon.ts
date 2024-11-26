import * as fs from "fs";
import { Position } from "../utils";

const f: string = fs.readFileSync("../inputs/day18/input.txt", { encoding: "utf8" });
const digPlanFull: string[][] = f.trim().split("\n").map(x => x.trim().split(/\s+/));
const digPlan: { dir: string, num: number }[] = digPlanFull.map(x => { return { dir: x[0], num: Number(x[1]) } });
const digPlan2: { dir: string, num: number }[] = digPlanFull.map(x => {
    const ins: string = x[2].replace("(#", "").replace(")", "");
    const dir: string = ins[ins.length - 1];
    const numHex: number = Number("0x" + ins.substring(0, ins.length - 1));
    return { dir: directionToDig(dir), num: numHex }
});


function solution(digPlan: { dir: string, num: number }[]): number {
    const nodes: Position[] = [];
    let prevNode: Position = new Position(0, 0);
    nodes.push(prevNode);
    let boundary: number = 0;
    for (const instruction of digPlan) {
        const newNode: Position = createNewNode(prevNode, instruction);
        prevNode = newNode;
        boundary += instruction.num
        nodes.push(prevNode);
    }

    let A = shoelaceFormula(nodes)
    return A + boundary / 2 + 1;
}


function shoelaceFormula(nodes: Position[]): number {
    let shoe: number = 0;
    for (let i = 0; i < nodes.length - 1; i++) {
        const node1: Position = nodes[i];
        const node2: Position = nodes[i + 1];

        shoe += (node1.i * node2.j) - (node2.i * node1.j);
    }
    return Math.abs(shoe / 2);
}


function createNewNode(prevNode: Position, instruction: { dir: string, num: number }): Position {
    let x = prevNode.i;
    let y = prevNode.j;
    if (instruction.dir === "R") {
        y += instruction.num;
    } else if (instruction.dir === "L") {
        y -= instruction.num;
    } else if (instruction.dir === "U") {
        x -= instruction.num;
    } else if (instruction.dir === "D") {
        x += instruction.num;
    }
    return new Position(x, y);
}


function directionToDig(dir: string): string {
    let x: string = ""
    switch (dir) {
        case "0":
            x = "R"
            break;
        case "1":
            x = "D";
            break;
        case "2":
            x = "L";
            break;
        case "3":
            x = "U";
            break;
    }
    return x;
}


console.log("Part 1:", solution(digPlan));
console.log("Part 2:", solution(digPlan2));
