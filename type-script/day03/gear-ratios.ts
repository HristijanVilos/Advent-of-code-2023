import * as fs from 'fs';
import { isCharNumeric } from '../utils';
import { Console } from 'console';

const f: string = fs.readFileSync("../inputs/day03/input.txt", { encoding: 'utf-8' });
const engineSchema: string[][] = f.split("\n").map(x => { return x.trim().split("") });

function part1(engineSchema: string[][]): number {
    let result: number = 0;
    for (let i = 0; i < engineSchema.length; i++) {
        for (let j = 0; j < engineSchema.length; j++) {
            if (!(isCharNumeric(engineSchema[i][j])) && engineSchema[i][j] !== '.') {
                findAdjecentPars(engineSchema, i, j).forEach(x => result += x);
            }
        }
    }
    return result;
}

function part2(engineSchema: string[][]): number {
    let result: number = 0;
    for (let i = 0; i < engineSchema.length; i++) {
        for (let j = 0; j < engineSchema.length; j++) {
            if (!(isCharNumeric(engineSchema[i][j])) && engineSchema[i][j] !== '.' && engineSchema[i][j] === '*') {
                const numArray: number[] = findAdjecentPars(engineSchema, i, j);
                if (numArray.length == 2) {
                    result += numArray.reduce((a, b) => a * b);
                }
            }
        }
    }
    return result;
}

function findAdjecentPars(engineSchema: string[][], i: number, j: number): number[] {
    const adjecentPos: number[][] = [[-1, -1], [-1, 0], [-1, 1], [0, -1], [0, 1], [1, -1], [1, 0], [1, 1]];
    const setFound: Set<string> = new Set();
    const foundNumbers: number[] = new Array<number>;

    for (const pos of adjecentPos) {
        const x: number = pos[0] + i;
        const y: number = pos[1] + j;
        if (inRangeOfSchema(y, engineSchema, x) && isCharNumeric(engineSchema[x][y]) && !setFound.has([x, y].toString())) {
            let num: string = engineSchema[x][y];
            let slider: number = y - 1;
            while (inRangeOfSchema(y, engineSchema, slider) && isCharNumeric(engineSchema[x][slider])) {
                num = engineSchema[x][slider] + num;
                setFound.add([x, slider].toString());
                slider = slider - 1;

            }
            slider = y + 1;
            while (inRangeOfSchema(y, engineSchema, slider) && isCharNumeric(engineSchema[x][slider])) {
                num += engineSchema[x][slider];
                setFound.add([x, slider].toString());
                slider += 1;
            }
            foundNumbers.push(Number(num));
        }
    }
    return foundNumbers;
}

function inRangeOfSchema(y: number, engineSchema: string[][], x: number) {
    return (y >= 0 && y < engineSchema.length) && (x >= 0 && x < engineSchema[0].length);
}

console.log("Part 1:", part1(engineSchema));
console.log("Part 2:", part2(engineSchema));
