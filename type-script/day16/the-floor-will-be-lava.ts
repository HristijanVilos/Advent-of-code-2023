import * as fs from "fs";
import { Position, PositionSet } from "../utils";

const f: string = fs.readFileSync("../inputs/day16/input.txt", { encoding: "utf-8" });
const input: string[][] = f.trim().split("\n").map(x => x.trim().split(""));
const Direction = {
    EAST: [0, 1] as const,
    WEST: [0, -1] as const,
    SOUTH: [1, 0] as const,
    NORTH: [-1, 0] as const,
};
type DirectionType = typeof Direction[keyof typeof Direction];


function part1(startingPos: Position, startDir: DirectionType): number {
    const queue: Array<[Position, DirectionType]> = [];
    const seen: Set<string> = new Set();
    const tileEnergized: PositionSet = new PositionSet();

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
        result = Math.max(part1(new Position(-1, j), Direction.SOUTH), result);
        result = Math.max(part1(new Position(input.length, j), Direction.NORTH), result);
    }
    for (let i = 0; i < input.length; i++) {
        result = Math.max(part1(new Position(i, -1), Direction.NORTH), result);
        result = Math.max(part1(new Position(i, input[0].length), Direction.NORTH), result);
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
    if (dir === Direction.EAST) {
        queue.push([newPos, Direction.NORTH]);
    } else if (dir === Direction.NORTH) {
        queue.push([newPos, Direction.EAST]);
    } else if (dir === Direction.SOUTH) {
        queue.push([newPos, Direction.WEST]);
    } else if (dir === Direction.WEST) {
        queue.push([newPos, Direction.SOUTH]);
    }
}


function backSlashMirror(dir: DirectionType, queue: [Position, DirectionType][], newPos: Position): void {
    if (dir === Direction.EAST) {
        queue.push([newPos, Direction.SOUTH]);
    } else if (dir === Direction.NORTH) {
        queue.push([newPos, Direction.WEST]);
    } else if (dir === Direction.SOUTH) {
        queue.push([newPos, Direction.EAST]);
    } else if (dir === Direction.WEST) {
        queue.push([newPos, Direction.NORTH]);
    }
}


function horizontalSpliter(dir: DirectionType, queue: [Position, DirectionType][], newPos: Position): void {
    if (dir === Direction.SOUTH || dir === Direction.NORTH) {
        queue.push([newPos, Direction.EAST]);
        queue.push([newPos, Direction.WEST]);
    } else {
        queue.push([newPos, dir]);
    }
}


function verticalSpliter(dir: DirectionType, queue: [Position, DirectionType][], newPos: Position): void {
    if (dir === Direction.EAST || dir === Direction.WEST) {
        queue.push([newPos, Direction.SOUTH]);
        queue.push([newPos, Direction.NORTH]);
    } else {
        queue.push([newPos, dir]);
    }
}


console.log("Part 1:", part1(new Position(0, -1), Direction.EAST));
console.log("Part 1:", part2());
