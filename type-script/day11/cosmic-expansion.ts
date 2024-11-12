import * as fs from "fs";

const f: string = fs.readFileSync("../inputs/day11/input.txt", { encoding: "utf-8" });
const rows: string[] = f.split("\n");

function solution(emptySpace: number, rows: string[]): number {
    let result: number = 0;
    const galaxyPositions: string[] = Array.from(constructGalaxy(emptySpace, rows));
    for (let i = 0; i < galaxyPositions.length; i++) {
        const galaxy = galaxyPositions[i];
        const galaxyX: number = Number(galaxy.split(": ")[0]);
        const galaxyY: number = Number(galaxy.split(": ")[1]);
        for (let j = i + 1; j < galaxyPositions.length; j++) {
            const otherGalaxy = galaxyPositions[j];
            const otherGalaxyX: number = Number(otherGalaxy.split(": ")[0]);
            const otherGalaxyY: number = Number(otherGalaxy.split(": ")[1]);
            result += Math.abs(otherGalaxyX - galaxyX) + Math.abs(otherGalaxyY - galaxyY);
        }
    }
    return result;
}

function constructGalaxy(emptySpace:number, rows: string[]): Set<string> {
    const galaxyPositions: Set<string> = new Set();
    let idx: number = 0;
    for (let i = 0; i < rows.length; i++) {
        let found: boolean = false;
        for (let j = 0; j < rows[0].length; j++) {
            if (rows[i][j] === "#") {
                found = true;
                const galaxy: Galaxy = new Galaxy(idx, j);
                galaxyPositions.add(galaxy.toString());
            }
        }
        if (!found) {
            idx += emptySpace;
        } else {
            idx++;
        }
    }

    const emptyColums: Set<number> = findEmptyColumns(rows[0].length, galaxyPositions);

    idx = 0;
    for (let val of emptyColums) {
        val += idx;
        const galaxiesToBeRemoved: Set<string> = new Set();
        const galaxiesToBeAdded: Set<string> = new Set();
        for (let galaxy of galaxyPositions) {
            const i: number = Number(galaxy.split(": ")[0]);
            const y: number = Number(galaxy.split(": ")[1]);
            if (y > val) {
                galaxiesToBeRemoved.add(galaxy);
                const newGalaxy: Galaxy = new Galaxy(i, y + emptySpace - 1);
                galaxiesToBeAdded.add(newGalaxy.toString());
            }
        }
        galaxiesToBeRemoved.forEach(x => galaxyPositions.delete(x));
        galaxiesToBeAdded.forEach(y => galaxyPositions.add(y));
        idx += emptySpace - 1;
    }

    return galaxyPositions;
}

function findEmptyColumns(widthOfMap: number, galaxyPositions: Set<string>): Set<number> {
    const emptyColums: Set<number> = new Set();
    for (let j = 0; j < widthOfMap; j++) {
        emptyColums.add(j);
    }
    for (let galaxy of galaxyPositions) {
        const j: number = Number(galaxy.split(": ")[1]);
        emptyColums.delete(j);
    }
    return emptyColums;
}

class Galaxy {
    i: number;
    j: number;

    constructor(i: number, j: number) {
        this.i = i;
        this.j = j;
    }

    toString(): string {
        return `${this.i}: ${this.j}`;
    }
}

console.log("Part 1:", solution(2, rows));
console.log("Part 2:", solution(1_000_000, rows));
