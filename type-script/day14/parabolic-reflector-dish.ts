import * as fs from "fs";
import { Position, CustomSet } from "../utils";

const f: string = fs.readFileSync("../inputs/day14/input.txt", { encoding: "utf-8" });
const map: string[][] = f.trim().split("\n").map(x => x.trim().split(""));

function part1(map: string[][]): number {
    const maxLength: number = map.length;
    const mapWidth: number = map[0].length;
    const [circleRocksPosition, cubeRockPosition]: [CustomSet<Position>, CustomSet<Position>] = setRocks(map);
    cycleNorth(circleRocksPosition, cubeRockPosition, maxLength, mapWidth);
    return calculateLoadOnNBeam(circleRocksPosition, maxLength);
}

function part2(map: string[][]): number {
    const maxLength: number = map.length;
    const mapWidth: number = map[0].length;
    const [circleRocksPosition, cubeRockPosition]: [CustomSet<Position>, CustomSet<Position>] = setRocks(map);
    const seen: Map<string, number> = new Map();
    for (let i = 0; i < 1_000_000_000; i++) {
        cycleNorth(circleRocksPosition, cubeRockPosition, maxLength, mapWidth);
        cycleWest(circleRocksPosition, cubeRockPosition, maxLength, mapWidth);
        cycleSouth(circleRocksPosition, cubeRockPosition, maxLength, mapWidth);
        cycleEast(circleRocksPosition, cubeRockPosition, maxLength, mapWidth);
        const circleRocksSorted: Position[] = Array.from(circleRocksPosition).sort((a, b) => a.toSort(b));
        const hash: string = circleRocksSorted.toString();
        if (seen.has(hash)) {
            const x = (1_000_000_000 - i) % (i - (seen.get(hash) || 0))
            i = 1_000_000_000 - x;
        } else {
            seen.set(hash, i);
        }
    }
    return calculateLoadOnNBeam(circleRocksPosition, maxLength);
}

function calculateLoadOnNBeam(circleRocksPosition: CustomSet<Position>, maxLength: number): number {
    let result: number = 0;
    for (let rock of circleRocksPosition) {
        result += (maxLength - rock.i)
    }
    return result
}

function cycleNorth(circleRocksPosition: CustomSet<Position>, cubeRockPosition: CustomSet<Position>, maxLength: number, maxWidth: number): void {
    for (let i = 1; i < maxLength; i++) {
        for (let j = 0; j < maxWidth; j++) {
            moveNorthIfPossible(i, j, circleRocksPosition, cubeRockPosition);
        }
    }
}

function moveNorthIfPossible(i: number, j: number, circleRocksPosition: CustomSet<Position>, cubeRockPosition: CustomSet<Position>): void {
    const checkRock: Position = new Position(i, j);
    if (circleRocksPosition.has(checkRock)) {
        let idx = i;
        while (idx > 0) {
            idx--;
            const movePosition: Position = new Position(idx, j);
            if (circleRocksPosition.has(movePosition) || cubeRockPosition.has(movePosition)) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(idx + 1, j));
                break;
            } else if (idx === 0) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(idx, j));
            }
        }
    }
}

function cycleWest(circleRocksPosition: CustomSet<Position>, cubeRockPosition: CustomSet<Position>, maxLength: number, maxWidth: number): void {
    for (let i = 0; i < maxLength; i++) {
        for (let j = 0; j < maxWidth; j++) {
            moveWestIfPossible(i, j, circleRocksPosition, cubeRockPosition);
        }
    }
}

function moveWestIfPossible(i: number, j: number, circleRocksPosition: CustomSet<Position>, cubeRockPosition: CustomSet<Position>): void {
    const checkRock: Position = new Position(i, j);
    if (circleRocksPosition.has(checkRock)) {
        let idx = j;
        while (idx > 0) {
            idx--;
            const movePosition: Position = new Position(i, idx);
            if (circleRocksPosition.has(movePosition) || cubeRockPosition.has(movePosition)) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(i, idx + 1));
                break;
            } else if (idx === 0) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(i, idx));
            }
        }
    }
}

function cycleSouth(circleRocksPosition: CustomSet<Position>, cubeRockPosition: CustomSet<Position>, maxLength: number, maxWidth: number): void {
    for (let i = maxLength - 1; i >= 0; i--) {
        for (let j = 0; j < maxWidth; j++) {
            moveSoutIfPossible(i, j, circleRocksPosition, maxLength, cubeRockPosition);
        }
    }
}

function moveSoutIfPossible(i: number, j: number, circleRocksPosition: CustomSet<Position>, maxLength: number, cubeRockPosition: CustomSet<Position>): void {
    const checkRock: Position = new Position(i, j);
    if (circleRocksPosition.has(checkRock)) {
        let idx = i;
        while (idx < maxLength) {
            idx++;
            const movePosition: Position = new Position(idx, j);
            if (circleRocksPosition.has(movePosition) || cubeRockPosition.has(movePosition)) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(idx - 1, j));
                break;
            } else if (idx === maxLength - 1) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(idx, j));
            }
        }
    }
}

function cycleEast(circleRocksPosition: CustomSet<Position>, cubeRockPosition: CustomSet<Position>, maxLength: number, maxWidth: number): void {
    for (let i = 0; i < maxLength; i++) {
        for (let j = maxWidth - 1; j >= 0; j--) {
            moveEastIfPossible(i, j, circleRocksPosition, maxWidth, cubeRockPosition);
        }
    }
}

function moveEastIfPossible(i: number, j: number, circleRocksPosition: CustomSet<Position>, maxWidth: number, cubeRockPosition: CustomSet<Position>): void {
    const checkRock: Position = new Position(i, j);
    if (circleRocksPosition.has(checkRock)) {
        let idx = j;
        while (idx < maxWidth) {
            idx++;
            const movePosition: Position = new Position(i, idx);
            if (circleRocksPosition.has(movePosition) || cubeRockPosition.has(movePosition)) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(i, idx - 1));
                break;
            } else if (idx === maxWidth - 1) {
                circleRocksPosition.delete(checkRock);
                circleRocksPosition.add(new Position(i, idx));
            }
        }
    }
}

function setRocks(map: string[][]): [CustomSet<Position>, CustomSet<Position>] {
    const circleRocksPosition: CustomSet<Position> = new CustomSet<Position>();
    const cubeRockPosition: CustomSet<Position> = new CustomSet<Position>();
    for (let i = 0; i < map.length; i++) {
        for (let j = 0; j < map[i].length; j++) {
            if (map[i][j] === 'O') {
                circleRocksPosition.add(new Position(i, j));
            } else if (map[i][j] === '#') {
                cubeRockPosition.add(new Position(i, j));
            }
        }
    }
    return [circleRocksPosition, cubeRockPosition];
}

console.log("Part 1:", part1(map));
console.log("Part 2:", part2(map));
