import * as fs from "fs";
import { Position, CustomSet, DIRECTION, DirectionType } from "../utils";

const f: string = fs.readFileSync("../inputs/day16/input.txt", { encoding: "utf-8" });
const input: string[][] = f.trim().split("\n").map(x => x.trim().split(""));

function part1(startingPos: Position, startDir: DirectionType): number {
    const queue: Array<[Position, DirectionType]> = [];
    const seen: Set<string> = new Set();
    const tileEnergized: CustomSet<Position> = new CustomSet();

    queue.push([startingPos, startDir]);
    while (queue) {
        const unpack: [Position, DirectionType] | undefined = queue.shift();
        if (!unpack) break;

        const [pos, dir]: [Position, DirectionType] = unpack;
        const posDir: string = [pos, dir].toString();

        if (seen.has(posDir)) {
            continue;
        }

        seen.add(posDir);
        tileEnergized.add(pos);

        moveBeam(queue, pos, dir);
    }

    return tileEnergized.size - 1;
}


function part2(): number {
    let result: number = 0;
    for (let j = 0; j < input[0].length; j++) {
        result = Math.max(part1(new Position(-1, j), DIRECTION.SOUTH), result);
        result = Math.max(part1(new Position(input.length, j), DIRECTION.NORTH), result);
    }
    for (let i = 0; i < input.length; i++) {
        result = Math.max(part1(new Position(i, -1), DIRECTION.NORTH), result);
        result = Math.max(part1(new Position(i, input[0].length), DIRECTION.NORTH), result);
    }
    return result;
}


function moveBeam(queue: Array<[Position, DirectionType]>, pos: Position, dir: DirectionType): void {
    const x: number = pos.i + dir[0];
    const y: number = pos.j + dir[1];

    if ((x < 0 || x >= input.length) || (y < 0 || y >= input[0].length)) {
        return;
    }

    const newPos: Position = new Position(x, y);
    const newPosType: string = input[x][y];

    if (newPosType === ".") {
        queue.push([newPos, dir]);
    } else if (newPosType === "|") {
        verticalSpliter(dir, queue, newPos);
    } else if (newPosType === "-") {
        horizontalSpliter(dir, queue, newPos);
    } else if (newPosType === "\\") {
        backSlashMirror(dir, queue, newPos);
    } else if (newPosType === "/") {
        forwardSlashMirror(dir, queue, newPos);
    }
}


function forwardSlashMirror(dir: DirectionType, queue: [Position, DirectionType][], newPos: Position): void {
    if (dir === DIRECTION.EAST) {
        queue.push([newPos, DIRECTION.NORTH]);
    } else if (dir === DIRECTION.NORTH) {
        queue.push([newPos, DIRECTION.EAST]);
    } else if (dir === DIRECTION.SOUTH) {
        queue.push([newPos, DIRECTION.WEST]);
    } else if (dir === DIRECTION.WEST) {
        queue.push([newPos, DIRECTION.SOUTH]);
    }
}


function backSlashMirror(dir: DirectionType, queue: [Position, DirectionType][], newPos: Position): void {
    if (dir === DIRECTION.EAST) {
        queue.push([newPos, DIRECTION.SOUTH]);
    } else if (dir === DIRECTION.NORTH) {
        queue.push([newPos, DIRECTION.WEST]);
    } else if (dir === DIRECTION.SOUTH) {
        queue.push([newPos, DIRECTION.EAST]);
    } else if (dir === DIRECTION.WEST) {
        queue.push([newPos, DIRECTION.NORTH]);
    }
}


function horizontalSpliter(dir: DirectionType, queue: [Position, DirectionType][], newPos: Position): void {
    if (dir === DIRECTION.SOUTH || dir === DIRECTION.NORTH) {
        queue.push([newPos, DIRECTION.EAST]);
        queue.push([newPos, DIRECTION.WEST]);
    } else {
        queue.push([newPos, dir]);
    }
}


function verticalSpliter(dir: DirectionType, queue: [Position, DirectionType][], newPos: Position): void {
    if (dir === DIRECTION.EAST || dir === DIRECTION.WEST) {
        queue.push([newPos, DIRECTION.SOUTH]);
        queue.push([newPos, DIRECTION.NORTH]);
    } else {
        queue.push([newPos, dir]);
    }
}


console.log("Part 1:", part1(new Position(0, -1), DIRECTION.EAST));
console.log("Part 1:", part2());
