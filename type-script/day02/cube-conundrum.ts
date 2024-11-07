import * as fs from 'fs';

const f: string = fs.readFileSync("../inputs/day02/input.txt", { encoding: 'utf-8' });
const games: string[] = f.trim().split("\n");


function part1(games: string[]): number {
    let result: number = 0;
    games.forEach((game, idx) => {
        const cubes: string = game.split(": ")[1];
        const rounds: string[] = cubes.trim().split(";");
        let isRoundPosibleFlag: boolean = true;
        rounds.forEach(round => {
            const balls: string[] = round.trim().split(", ");
            if (!isRoundPosible(balls)) {
                isRoundPosibleFlag = false;
                return;
            }
        });
        if (isRoundPosibleFlag) {
            result += idx + 1;
        }
    });
    return result;
}

function part2(games: string[]): number {
    let result: number = 0;
    games.forEach(game => {
        const cubes: string = game.split(": ")[1];
        const rounds: string[] = cubes.trim().split(";");
        let cubed: number = powerOfCubes(rounds);
        result += cubed;
    })
    return result;
}

function powerOfCubes(rounds: string[]): number {
    const maxMap: Map<string, number> = new Map();
    rounds.forEach(round => {
        const balls: string[] = round.trim().split(", ");
        balls.forEach(hand => {
            const numberAndColor: string[] = hand.trim().split(" ");
            const numberOfBalls: number = Number(numberAndColor[0]);
            const colorOfBalls: string = numberAndColor[1];
            maxMap.set(colorOfBalls, Math.max(maxMap.get(colorOfBalls) || 0, numberOfBalls));
        });
    });

    let cubed: number = 1;
    for (let values of maxMap.values()) {
        cubed *= values;
    }
    return cubed;
}

function isRoundPosible(balls: string[]): boolean {
    let isRoundPosible: boolean = true;
    balls.forEach(hand => {
        const numberAndColor: string[] = hand.trim().split(" ");
        const numberOfBalls: number = Number(numberAndColor[0]);
        const colorOfBalls: string = numberAndColor[1];
        if (!isHandPossible(numberOfBalls, colorOfBalls)) {
            isRoundPosible = false;
            return;
        }
    });
    return isRoundPosible;
}

function isHandPossible(count: number, color: string): boolean {
    if ((color === "red" && count <= 12)
        || (color === "green" && count <= 13)
        || (color === "blue" && count <= 14)) {
        return true;
    }
    return false;
}

console.log("Part 1:", part1(games));
console.log("Part 2:", part2(games));
