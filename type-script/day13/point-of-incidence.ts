import * as fs from "fs";

const f: string = fs.readFileSync("../inputs/day13/test_input.txt", { encoding: "utf-8" });
const maps: string[][] = f.split("\n\n").map(x => x.trim().split("\n"));

function part1(maps: string[][]): number {
    let result: number = 0;
    for (let map of maps) {
        result += 100 * reflectAcrossHorizontalLine(map);
        result += reflectAcrossVerticalLine(map);
    }
    return result;
}

function part2(maps: string[][]): number {
    let result: number = 0;
    for (let map of maps) {
        const horLine: number = 100 * reflectAcrossHorizontalLineWithSmudge(map);
        const verLine: number = reflectAcrossVerticalLineWithSmudge(map);
        result += Math.max(horLine, verLine);
    }
    return result;
}

function reflectAcrossHorizontalLine(map: string[]): number {
    for (let i = 1; i < map.length; i++) {
        let allMatch: boolean = true;
        for (let j = 0; j < i; j++) {
            if (i + j >= map.length) {
                break;
            }

            if (map[i + j] !== map[i - j - 1]) {
                allMatch = false;
                break;
            }
        }

        if (allMatch) {
            return i;
        }
    }
    return 0;
}

function reflectAcrossHorizontalLineWithSmudge(map: string[]): number {
    for (let i = 1; i < map.length; i++) {
        let allMatch: boolean = false;
        let mistakes: number = 1;
        for (let j = 0; j < i; j++) {
            if (i + j >= map.length) {
                break;
            }

            const str1: string = map[i + j];
            const str2: string = map[i - j - 1];

            if (str1 !== str2) {
                allMatch = true;
                for (let z = 0; z < str1.length; z++) {
                    if (str1[z] !== str2[z]) {
                        mistakes--;
                    }
                }

                if (mistakes < 0) {
                    allMatch = false;
                    break;
                }
            }
        }

        if (allMatch) {
            return i;
        }
    }
    return 0;
}

function reflectAcrossVerticalLine(map: string[]): number {
    const switchRowsForColumns: string[] = convertRowsToColumns(map);
    return reflectAcrossHorizontalLine(switchRowsForColumns);
}

function reflectAcrossVerticalLineWithSmudge(map: string[]): number {
    const switchRowsForColumns: string[] = convertRowsToColumns(map);
    return reflectAcrossHorizontalLineWithSmudge(switchRowsForColumns);
}

function convertRowsToColumns(map: string[]): string[] {
    const rowsToColumns: string[][] = Array.from({ length: map[0].length }, () => Array(map.length));
    for (let i = 0; i < map.length; i++) {
        for (let j = 0; j < map[0].length; j++) {
            rowsToColumns[j][i] = map[i][j];
        }
    }

    const convert: string[] = rowsToColumns.map(x => x.join(""));
    return convert;
}

console.log("Part 1:", part1(maps));
console.log("Part 2:", part2(maps));
