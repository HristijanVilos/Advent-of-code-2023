import * as fs from "fs";
import { Position, CustomSet, DIRECTION, DirectionType, MinHeap } from "../utils";


const f: string = fs.readFileSync("../inputs/day17/input.txt", { encoding: "utf-8" });
const input: number[][] = f.trim().split("\n").map(x => x.trim().split("").map(y => Number(y)));


function solution(input: number[][], minSteps: number, maxSteps: number): number {

    const maxHeight: number = input.length;
    const maxWidth: number = input[0].length;

    const minHeap: MinHeap<Tuple> = new MinHeap();
    const startingPoint: Tuple = new Tuple(new Position(0, 0), 0, DIRECTION.EAST, DIRECTION.EAST, 0);
    const startingPoint2: Tuple = new Tuple(new Position(0, 0), 0, DIRECTION.SOUTH, DIRECTION.SOUTH, 0);
    minHeap.insert(startingPoint);
    minHeap.insert(startingPoint2);

    const seen: CustomSet<Tuple> = new CustomSet();

    while (minHeap.length > 0) {
        const tuple: Tuple | null = minHeap.pop();
        if (tuple === null) break;

        if (exitPoint(tuple, maxHeight, maxWidth, minSteps)) {
            return tuple.distance;
        }

        if (seen.has(tuple)) {
            continue;
        }

        seen.add(tuple);
        nextPoints(minHeap, input, tuple, maxHeight, maxWidth, minSteps, maxSteps);
    }

    return -1;
}


function exitPoint(tuple: Tuple, maxHeight: number, maxWidth: number, minSteps: number): boolean {
    return (tuple.point.i === maxHeight - 1 && tuple.point.j === maxWidth - 1) && (tuple.movmentInOneDir >= minSteps);
}


function nextPoints(minHeap: MinHeap<Tuple>, input: number[][], tuple: Tuple, maxHeight: number, maxWidth: number, minSteps: number, maxSteps: number): void {
    const x = tuple.point.i + tuple.goindDir[0];
    const y = tuple.point.j + tuple.goindDir[1];

    if (isOutsideOfGrid(x, maxHeight, y, maxWidth)) {
        return;
    }

    const mov: number = moveInSameDirection(tuple);
    if (mov > maxSteps) {
        return;
    }

    for (let dir of Object.values(DIRECTION)) {
        if ((mov < minSteps && tuple.goindDir[0] !== dir[0] && tuple.goindDir[1] !== dir[1])
            || (tuple.goindDir[0] + dir[0] === 0 && tuple.goindDir[1] + dir[1] === 0)) {
            continue;
        }
        minHeap.insert(new Tuple(new Position(x, y), tuple.distance + input[x][y], tuple.goindDir, dir, mov));
    }
}


function isOutsideOfGrid(x: number, maxHeight: number, y: number, maxWidth: number): boolean {
    return x < 0 || x >= maxHeight || y < 0 || y >= maxWidth;
}


function moveInSameDirection(tuple: Tuple): number {
    let mov: number = tuple.movmentInOneDir;
    if (tuple.comingDir.toString() === tuple.goindDir.toString()) {
        mov++;
    } else {
        mov = 1;
    }
    return mov;
}


class Tuple {
    point: Position;
    distance: number;
    comingDir: DirectionType;
    goindDir: DirectionType;
    movmentInOneDir: number;

    constructor(p: Position, d: number, cD: DirectionType, gD: DirectionType, m: number) {
        this.point = p;
        this.distance = d;
        this.comingDir = cD;
        this.goindDir = gD;
        this.movmentInOneDir = m;
    }

    hashCode(): string {
        return `${this.point}-${this.comingDir}-${this.goindDir}-${this.movmentInOneDir}`;
    }

    sort(other: Tuple): number {
        return this.distance - other.distance;
    }

    valueOf(): number {
        return this.distance;
    }
}


console.log("Part 1:", solution(input, 0, 3));
console.log("Part 2:", solution(input, 4, 10));
